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
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.classes.Skill
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.classes.Weapon
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
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

        fun getDatabase(context: Context): DndCharacterManagerDB? {
            // TODO remove later. Used to test the database population
//            context.deleteDatabase("dnd_character_manager_database")

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

                        val fireball = Spell(1, "Fireball", 3, "Evocation")
                        val magicMissile = Spell(2, "Magic Missile", 1, "Evocation")

                        Log.i("DndCharacterManagerDB", "Start populating database")

                        raceDao.insertAll(races)
                        abilityDao.insertAll(abilities)
                        dndClassDao.insertAllDndClassesWithAbilities(dndClasses)
                        skillDao.insertAll(skills)
                        weaponDao.insertAll(listOf(weapon, nullWeapon))
                        spellDao.insertAll(listOf(fireball, magicMissile))

                        Log.i("DndCharacterManagerDB", "DB Populated, calling countDown()")
                        latch.countDown()
                    }
                }
            }
    }

}