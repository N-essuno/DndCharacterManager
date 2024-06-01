package it.brokenengineers.dnd_character_manager.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.classes.Skill
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.classes.Weapon
import it.brokenengineers.dnd_character_manager.data.database.AbilityDao
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterDao
import it.brokenengineers.dnd_character_manager.data.database.DndClassDao
import it.brokenengineers.dnd_character_manager.data.database.RaceDao
import it.brokenengineers.dnd_character_manager.data.database.SkillDao
import it.brokenengineers.dnd_character_manager.data.database.SpellDao
import it.brokenengineers.dnd_character_manager.data.database.WeaponDao
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
import it.brokenengineers.dnd_character_manager.screens.levelup.MockSpells
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DndCharacterManagerRepository(
    private val viewModel: DndCharacterManagerViewModel,
    private val dndCharacterDao: DndCharacterDao,
    private val raceDao: RaceDao,
    private val abilityDao: AbilityDao,
    private val dndClassDao: DndClassDao,
    private val skillDao: SkillDao,
    private val weaponDao: WeaponDao,
    private val spellDao: SpellDao
) {
    private val tag: String = DndCharacterManagerRepository::class.java.simpleName
    var allCharacters: MutableStateFlow<MutableList<DndCharacter>> = MutableStateFlow(mutableListOf())
    val selectedDndCharacter: MutableStateFlow<DndCharacter?> = MutableStateFlow(null)

    fun init() {
        viewModel.viewModelScope.launch {
            // TODO use the following for loading only DB characters instead of Mock characters
            fetchAllCharacters()
//            getAllCharacters()

        }
    }

    fun insertCharacter(dndCharacter: DndCharacter) {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val race = raceDao.getRaceByName(dndCharacter.race!!.name)
                val dndClass = dndClassDao.getDndClassByName(dndCharacter.dndClass!!.name)
                dndCharacter.raceId = race.id
                dndCharacter.dndClassId = dndClass.id
                if (dndCharacter.weapon != null) {
                    val weapon = weaponDao.getWeaponByName(dndCharacter.weapon!!.name)
                    dndCharacter.weaponId = weapon.id
                } else {
                    // 99 is the id of the "None" weapon
                    dndCharacter.weaponId = 99
                }

                val id = dndCharacterDao.insert(dndCharacter)
                dndCharacter.id = id
                allCharacters.value.add(dndCharacter)
            }
        }
    }

    fun insertAllCharacters(dndCharacters: List<DndCharacter>) {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                for (character in dndCharacters) {
                    val race = raceDao.getRaceByName(character.race!!.name)
                    val dndClass = dndClassDao.getDndClassByName(character.dndClass!!.name)
                    if (character.weapon != null) {
                        val weapon = weaponDao.getWeaponByName(character.weapon!!.name)
                        character.weaponId = weapon.id
                    } else {
                        character.weaponId = 99
                    }
                    character.raceId = race.id
                    character.dndClassId = dndClass.id
                }

                dndCharacterDao.insertAll(dndCharacters)
                allCharacters.value.addAll(dndCharacters)
            }
        }
    }

    fun fetchCharacterByName(name: String) {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val dbCharacter = dndCharacterDao.getCharacterByName(name)
                val characterRace = raceDao.getRaceById(dbCharacter.raceId)
                val characterDndClass = fetchDndClassBlocking(dbCharacter.dndClassId)
                val characterSkills = fetchCharacterSkillsBlocking(dbCharacter.id)
                val characterWeapon: Weapon = weaponDao.getWeapon(dbCharacter.weaponId)
                val characterKnownSpells: List<Spell> =
                    fetchCharacterKnownSpellsBlocking(dbCharacter.id)
                val characterPreparedSpells: List<Spell> =
                    fetchCharacterPreparedSpellsBlocking(dbCharacter.id)

                dbCharacter.race = characterRace
                dbCharacter.dndClass = characterDndClass
                dbCharacter.skillProficiencies = characterSkills.toSet()
                dbCharacter.weapon = characterWeapon
                dbCharacter.spellsKnown = characterKnownSpells.toSet()
                dbCharacter.preparedSpells = characterPreparedSpells.toSet()
                selectedDndCharacter.value = dbCharacter
            }
        }
    }

    fun fetchAllCharacters() {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val dbCharacters = dndCharacterDao.getAllCharacters().toMutableList()

                for (character in dbCharacters) {
                    val characterRace = raceDao.getRaceById(character.raceId)
                    val characterDndClass = fetchDndClassBlocking(character.dndClassId)
                    val characterSkills = fetchCharacterSkillsBlocking(character.id)
                    val characterWeapon: Weapon = weaponDao.getWeapon(character.weaponId)
                    val characterKnownSpells: List<Spell> =
                        fetchCharacterKnownSpellsBlocking(character.id)
                    val characterPreparedSpells: List<Spell> =
                        fetchCharacterPreparedSpellsBlocking(character.id)

                    character.race = characterRace
                    character.dndClass = characterDndClass
                    character.skillProficiencies = characterSkills.toSet()
                    character.weapon = characterWeapon
                    character.spellsKnown = characterKnownSpells.toSet()
                    character.preparedSpells = characterPreparedSpells.toSet()
                }
                allCharacters.value = dbCharacters
            }
        }
    }

    fun deleteAllCharacters() {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dndCharacterDao.deleteAll()
                allCharacters.value.clear()
            }
        }
    }

    fun deleteCharacter(dndCharacter: DndCharacter) {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val id = dndCharacterDao.deleteCharacter(dndCharacter)
                allCharacters.value.removeIf { it.id == id }
            }
        }
    }


    /** --------- Blocking functions used for DB testing to wait for results (no UI) --------- */

    fun fetchAllCharactersBlocking(): List<DndCharacter> {
        val dbCharacters = dndCharacterDao.getAllCharacters().toMutableList()

        for (dbCharacter in dbCharacters) {
            val characterRace = raceDao.getRaceById(dbCharacter.raceId)
            val characterDndClass = fetchDndClassBlocking(dbCharacter.dndClassId)
            val characterSkills = fetchCharacterSkillsBlocking(dbCharacter.id)
            var characterWeapon: Weapon? = null
            val characterKnownSpells: List<Spell> = fetchCharacterKnownSpellsBlocking(dbCharacter.id)
            val characterPreparedSpells: List<Spell> = fetchCharacterPreparedSpellsBlocking(dbCharacter.id)

            // 99 is the id of the "None" weapon
            if (dbCharacter.weaponId != 99)
                characterWeapon = weaponDao.getWeapon(dbCharacter.weaponId)

            dbCharacter.race = characterRace
            dbCharacter.dndClass = characterDndClass
            dbCharacter.skillProficiencies = characterSkills.toSet()
            dbCharacter.weapon = characterWeapon
            dbCharacter.spellsKnown = characterKnownSpells.toSet()
            dbCharacter.preparedSpells = characterPreparedSpells.toSet()
        }
        return dbCharacters
    }

    suspend fun fetchCharacterByNameBlocking(name: String): DndCharacter = withContext(Dispatchers.IO){
        val dbCharacter = dndCharacterDao.getCharacterByName(name)
        val characterRace = raceDao.getRaceById(dbCharacter.raceId)
        val characterDndClass = fetchDndClassBlocking(dbCharacter.dndClassId)
        val characterSkills = fetchCharacterSkillsBlocking(dbCharacter.id)
        var characterWeapon: Weapon? = null
        val characterKnownSpells: List<Spell> = fetchCharacterKnownSpellsBlocking(dbCharacter.id)
        val characterPreparedSpells: List<Spell> = fetchCharacterPreparedSpellsBlocking(dbCharacter.id)

        // 99 is the id of the "None" weapon
        if (dbCharacter.weaponId != 99)
            characterWeapon = weaponDao.getWeapon(dbCharacter.weaponId)

        dbCharacter.race = characterRace
        dbCharacter.dndClass = characterDndClass
        dbCharacter.skillProficiencies = characterSkills.toSet()
        dbCharacter.weapon = characterWeapon
        dbCharacter.spellsKnown = characterKnownSpells.toSet()
        dbCharacter.preparedSpells = characterPreparedSpells.toSet()
        return@withContext dbCharacter
    }

    private fun fetchCharacterSkillsBlocking(characterId: Int): List<Skill> {
        val dbSkills = skillDao.getSkillsForCharacter(characterId)
        dbSkills.forEach { skill ->
            val ability = abilityDao.getAbility(skill.abilityId)
            skill.ability = ability
        }
        return dbSkills
    }

    private fun fetchCharacterKnownSpellsBlocking(characterId: Int): List<Spell> {
        val dbSpells = spellDao.getKnownSpellsForCharacter(characterId)

        return dbSpells
    }

    private fun fetchCharacterPreparedSpellsBlocking(characterId: Int): List<Spell> {
        val dbSpells = spellDao.getPreparedSpellsForCharacter(characterId)

        return dbSpells
    }

    private fun fetchDndClassBlocking(id: Int): DndClass {
        val dbDndClass = dndClassDao.getDndClassById(id)
        val primaryAbility = abilityDao.getAbility(dbDndClass.primaryAbilityId)
        val savingThrowProficiencies =
            dndClassDao.getSavingThrowsProficienciesForDndClass(dbDndClass.id)
        dbDndClass.primaryAbility = primaryAbility
        dbDndClass.savingThrowProficiencies = savingThrowProficiencies
        return dbDndClass
    }

    suspend fun fetchAllRacesBlocking(): List<Race> {
        return raceDao.getAllRaces()
    }

    fun fetchAllSpellsBlocking(): List<Spell> {
        return spellDao.getAllSpells()
    }

    fun insertAllCharactersBlocking(dndCharacters: List<DndCharacter>) {
        for (character in dndCharacters) {
            val race = raceDao.getRaceByName(character.race!!.name)
            val dndClass = dndClassDao.getDndClassByName(character.dndClass!!.name)
            if (character.weapon != null) {
                val weapon = weaponDao.getWeaponByName(character.weapon!!.name)
                character.weaponId = weapon.id
            } else {
                character.weaponId = 99
            }

            character.raceId = race.id
            character.dndClassId = dndClass.id
        }

        dndCharacterDao.insertAll(dndCharacters)
    }

    fun fetchAllAbilitiesBlocking(): List<Ability> {
        val dbAbilities = abilityDao.getAllAbilities()
        return dbAbilities
    }

    fun fetchAllDndClassesBlocking(): List<DndClass> {
        val dbDndClasses = dndClassDao.getAllDndClasses()
        dbDndClasses.forEach { dndClass ->
            val primaryAbility = abilityDao.getAbility(dndClass.primaryAbilityId)
            val savingThrowProficiencies = dndClassDao.getSavingThrowsProficienciesForDndClass(dndClass.id)
            dndClass.primaryAbility = primaryAbility
            dndClass.savingThrowProficiencies = savingThrowProficiencies
        }
        return dbDndClasses
    }

    fun fetchAllSkillsBlocking(): List<Skill> {
        val dbSkills = skillDao.getAllASkills()
        dbSkills.forEach { skill ->
            val ability = abilityDao.getAbility(skill.abilityId)
            skill.ability = ability
        }
        return dbSkills
    }

    fun fetchAllWeaponsBlocking(): List<Weapon> {
        val dbWeapons = weaponDao.getAllWeapons()
        return dbWeapons
    }


    /** --------------------- Functions to load Mock Characters --------------------- **/

    fun getAllCharacters() {
        viewModel.viewModelScope.launch {
            val characters = createMockCharacters()
            allCharacters.value = characters
        }
    }

    fun getCharacterById(id: Int) {
        viewModel.viewModelScope.launch {
            selectedDndCharacter.value = allCharacters.value.find { it.id == id }
            Log.i(tag, "Repository: selectedCharacter updated: $selectedDndCharacter")
        }
    }

    fun createMockCharacters(): MutableList<DndCharacter> {
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
        // TODO refactor wth Mock Spells
        val fireball = Spell(1, "Fireball", 3, "Evocation")
        val magicMissile = Spell(2, "Magic Missile", 1, "Evocation")

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

        val item1 = InventoryItem(1, "Health potion", 5, 1.5)
        val item2 = InventoryItem(2, "Paper", 1, 0.2)
        val item3 = InventoryItem(3, "Brick", 1, 2.5)
        val item4 = InventoryItem(4, "Book", 1, 2.0)

        val weapon1 = Weapon(1, "Hammer", "1d12")

        val dndCharacter1 = DndCharacter(
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
            spellsKnown = setOf(fireball, magicMissile),
            preparedSpells = setOf(fireball),
            availableSpellSlots = mapOf(
                1 to 0,
            ),
            inventoryItems = setOf(item1, item2),
            image = null,
            weapon = null,
            weaponId = 99
        )

        val dndCharacter2 = DndCharacter(
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

        Log.i(tag, "Repository: created mock characters")

        val gson = com.google.gson.Gson()

        Log.i(tag, "Repository: dndCharacter1 race JSON: ${gson.toJson(dndCharacter1.race)}")
        Log.i(tag, "Repository: dndCharacter1 abilityValues JSON: ${gson.toJson(dndCharacter1.abilityValues)}")
        Log.i(tag, "Repository: dndCharacter2 race JSON: ${gson.toJson(dndCharacter2.race)}")
        Log.i(tag, "Repository: dndCharacter2 abilityValues JSON: ${gson.toJson(dndCharacter2.abilityValues)}")

        return mutableListOf(
            dndCharacter1,
            dndCharacter2
        )
    }
}