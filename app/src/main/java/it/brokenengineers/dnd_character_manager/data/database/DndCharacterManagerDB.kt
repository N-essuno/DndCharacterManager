package it.brokenengineers.dnd_character_manager.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterKnownSpellCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterPreparedSpellCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterSkillCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.DndClassAbilityCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.classes.Skill
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.classes.Weapon
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.MockSpells
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch

@Database(
    entities = [
        DndCharacter::class,
        Race::class,
        Ability::class,
        DndClassAbilityCrossRef::class,
        DndClass::class,
        Skill::class,
        DndCharacterSkillCrossRef::class,
        Weapon::class,
        Spell::class,
        DndCharacterKnownSpellCrossRef::class,
        DndCharacterPreparedSpellCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DndCharacterManagerDB: RoomDatabase() {
    abstract fun characterDao(): DndCharacterDao
    abstract fun raceDao(): RaceDao
    abstract fun abilityDao(): AbilityDao
    abstract fun dndClassDao(): DndClassDao
    abstract fun skillDao(): SkillDao
    abstract fun weaponDao(): WeaponDao
    abstract fun spellDao(): SpellDao

    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: DndCharacterManagerDB? = null
        // latch to wait for the database to be populated
        // It delays the DB return until the population callback is completed (roomDatabaseCallback)
        private val latch = CountDownLatch(1)
        private var initCharacters: Boolean = true

        fun getDatabase(context: Context, initCharacters: Boolean = true, usingUI: Boolean = true): DndCharacterManagerDB? {
            if (usingUI) {
                val dbDelete = context.deleteDatabase("dnd_character_manager_database")
                Log.i("CharacterSheet", "DB deleted: $dbDelete")
            }


            this.initCharacters = initCharacters

            if (INSTANCE == null) {
                // ensure only one instance of the database is created even if multiple threads try to create an instance
                synchronized(DndCharacterManagerDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = androidx.room.Room.databaseBuilder(
                            context.applicationContext,
                            DndCharacterManagerDB::class.java,
                            "dnd_character_manager_database"
                        )
                            // how to add a migration
                            .addMigrations(

                            )
                            .addCallback(roomDatabaseCallback)
                            .build()

                    }
                }
            }
            assert(INSTANCE != null)
            // roomDatabaseCallback will be called only after the first use of the DB
            // The following is a Dummy query to trigger the callback
            runBlocking { INSTANCE?.raceDao()?.getAllRaces() }
            latch.await()
            return INSTANCE
        }

        /**
         * Override the onOpen method to populate the database.
         */
        private val roomDatabaseCallback: Callback =
            object : Callback() {
                // TODO Need to populate also other tables
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Use a new thread to insert the data
                    CoroutineScope(Dispatchers.IO).launch {
                        val raceDao = INSTANCE!!.raceDao()
                        val abilityDao = INSTANCE!!.abilityDao()
                        val dndClassDao = INSTANCE!!.dndClassDao()
                        val skillDao = INSTANCE!!.skillDao()
                        val weaponDao = INSTANCE!!.weaponDao()
                        val spellDao = INSTANCE!!.spellDao()

                        val races = RaceEnum.entries.map { it.race }
                        val abilities = AbilityEnum.entries.map { it.ability }
                        val dndClasses = DndClassEnum.entries.map { it.dndClass }
                        val skills = SkillEnum.entries.map { it.skill }
                        val weapon = Weapon(1, "Hammer", "1d12")
                        val nullWeapon = Weapon(99, "", "0")
                        val spells = MockSpells.getAllSpells()

                        Log.i("DndCharacterManagerDB", "Start populating database")

                        raceDao.insertAll(races)
                        abilityDao.insertAll(abilities)
                        dndClassDao.insertAllDndClassesWithAbilities(dndClasses)
                        skillDao.insertAll(skills)
                        weaponDao.insertAll(listOf(weapon, nullWeapon))
                        spellDao.insertAll(spells)

                        if (initCharacters)
                            insertMockCharacters()

                        Log.i("DndCharacterManagerDB", "DB Populated, calling countDown()")
                        latch.countDown()
                    }
                }
            }

        private fun insertMockCharacters() {
            // Mock Abilities
            val strength = AbilityEnum.STRENGTH.ability
            val dexterity = AbilityEnum.DEXTERITY.ability
            val constitution = AbilityEnum.CONSTITUTION.ability
            val intelligence = AbilityEnum.INTELLIGENCE.ability
            val wisdom = AbilityEnum.WISDOM.ability
            val charisma = AbilityEnum.CHARISMA.ability

            // Mock Skills
            val athletics = SkillEnum.ATHLETICS.skill
            val acrobatics = SkillEnum.ACROBATICS.skill
            val arcana = SkillEnum.ARCANA.skill
            val history = SkillEnum.HISTORY.skill

            // Mock DndClasses
            val barbarian = DndClassEnum.BARBARIAN.dndClass
            val wizard = DndClassEnum.WIZARD.dndClass

            // Mock Races
            val eladrin = RaceEnum.ELADRIN.race
            val dwarf = RaceEnum.DWARF.race

            // Mock Spells
            val magicMissile: Spell = MockSpells.getSpellByName("Magic Missile")!!

            // Mock Ability Values
            val abilityValues1 = mapOf(
                strength to 15,
                dexterity to 14,
                constitution to 13,
                intelligence to 12,
                wisdom to 10,
                charisma to 8
            )

            val abilityValues2 = mapOf(
                strength to 10,
                dexterity to 12,
                constitution to 14,
                intelligence to 16,
                wisdom to 13,
                charisma to 11
            )

            val item1 = InventoryItem("Health potion", 5, 1.5)
            val item2 = InventoryItem("Paper", 1, 0.2)
            val item3 = InventoryItem("Brick", 1, 2.5)
            val item4 = InventoryItem("Book", 1, 2.0)

            val weapon1 = Weapon(1, "Hammer", "1d12")

            val silvano = DndCharacter(
                id = 1,
                name = "Silvano",
                race = eladrin,
                raceId = eladrin.id,
                dndClass = wizard,
                dndClassId = wizard.id,
                level = 1,
                abilityValues = abilityValues1,
                skillProficiencies = setOf(arcana, history),
                remainingHp = 7,
                tempHp = 0,
                spellsKnown = setOf(magicMissile),
                preparedSpells = setOf(magicMissile),
                availableSpellSlots = mapOf(
                    1 to 1,
                ),
                inventoryItems = setOf(item1, item2),
                image = null,
                weapon = null,
                weaponId = 99
            )

            val broken = DndCharacter(
                id = 2,
                name = "Broken",
                race = dwarf,
                raceId = dwarf.id,
                dndClass = barbarian,
                dndClassId = barbarian.id,
                level = 1,
                image = null,
                abilityValues = abilityValues2,
                skillProficiencies = setOf(athletics, acrobatics),
                remainingHp = 12,
                tempHp = 0,
                spellsKnown = null,
                preparedSpells = null,
                availableSpellSlots = null,
                inventoryItems = setOf(item3, item4),
                weapon = weapon1,
                weaponId = weapon1.id
            )

            INSTANCE?.characterDao()?.insertAll(listOf(silvano, broken))
        }
    }

}