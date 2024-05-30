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
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
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
    private val skillDao: SkillDao
) {
    private val tag: String = DndCharacterManagerRepository::class.java.simpleName
    var allCharacters: MutableStateFlow<MutableList<DndCharacter>> = MutableStateFlow(mutableListOf())
    val selectedDndCharacter: MutableStateFlow<DndCharacter?> = MutableStateFlow(null)

    fun init() {
        viewModel.viewModelScope.launch {
            getAllCharacters()
        }
    }
    fun insertCharacter(dndCharacter: DndCharacter) {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val race = raceDao.getRaceByName(dndCharacter.race!!.name)
                val dndClass = dndClassDao.getDndClassByName(dndCharacter.dndClass!!.name)
                dndCharacter.raceId = race.id
                dndCharacter.dndClassId = dndClass.id

//                val newId = dndCharacterDao.insertDndCharacter(dndCharacter)

//                dndCharacter.id = newId.toInt()
                val id = dndCharacterDao.insertDndCharacterSkillProficiencies(dndCharacter)
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
                    character.raceId = race.id
                    character.dndClassId = dndClass.id
                }

                dndCharacterDao.insertAll(dndCharacters)
                allCharacters.value.addAll(dndCharacters)
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
                    character.race = characterRace
                    character.dndClass = characterDndClass
                    character.skillProficiencies = characterSkills.toSet()
                }
                allCharacters.value = dbCharacters
            }
        }
    }

    fun fetchCharacterByName(name: String) {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val dbCharacter = dndCharacterDao.getCharacterByName(name)
                val characterRace = raceDao.getRaceById(dbCharacter.raceId)

                dbCharacter.race = characterRace
                dbCharacter.dndClass = fetchDndClassBlocking(dbCharacter.dndClassId)
                dbCharacter.skillProficiencies =
                    fetchCharacterSkillsBlocking(dbCharacter.id).toSet()
                selectedDndCharacter.value = dbCharacter
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
                dndCharacterDao.deleteCharacter(dndCharacter)
                allCharacters.value.removeIf { it.id == dndCharacter.id }
            }
        }
    }

    fun insertRace(race: Race) {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                raceDao.insert(race)
            }
        }
    }

    fun insertAllRaces(races: List<Race>){
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                raceDao.insertAll(races)
            }
        }
    }


    // Blocking functions used for DB testing to wait for results (no UI)

    suspend fun fetchAllCharactersBlocking(): List<DndCharacter> {
        val dbCharacters = dndCharacterDao.getAllCharacters().toMutableList()

        for (character in dbCharacters) {
            val characterRace = raceDao.getRaceById(character.raceId)
            val characterDndClass = fetchDndClassBlocking(character.dndClassId)
            val characterSkills = fetchCharacterSkillsBlocking(character.id)
            character.race = characterRace
            character.dndClass = characterDndClass
            character.skillProficiencies = characterSkills.toSet()
        }
        return dbCharacters
    }

    fun fetchCharacterSkillsBlocking(characterId: Int): List<Skill> {
        val dbSkills = skillDao.getSkillsForCharacter(characterId)
        dbSkills.forEach { skill ->
            val ability = abilityDao.getAbility(skill.abilityId)
            skill.ability = ability
        }
        return dbSkills
    }

    fun fetchDndClassBlocking(id: Int): DndClass {
        val dbDndClass = dndClassDao.getDndClassById(id)
        val primaryAbility = abilityDao.getAbility(dbDndClass.primaryAbilityId)
        val savingThrowProficiencies =
            dndClassDao.getSavingThrowsProficienciesForDndClass(dbDndClass.id)
        dbDndClass.primaryAbility = primaryAbility
        dbDndClass.savingThrowProficiencies = savingThrowProficiencies
        return dbDndClass
    }

    suspend fun fetchCharacterByNameBlocking(name: String): DndCharacter = withContext(Dispatchers.IO){
        val dbCharacter = dndCharacterDao.getCharacterByName(name)
        val characterRace = raceDao.getRaceById(dbCharacter.raceId)

        dbCharacter.race = characterRace
        dbCharacter.dndClass = fetchDndClassBlocking(dbCharacter.dndClassId)
        dbCharacter.skillProficiencies = fetchCharacterSkillsBlocking(dbCharacter.id).toSet()
        return@withContext dbCharacter
    }

    suspend fun fetchAllRacesBlocking(): List<Race> {
        val dbRaces = raceDao.getAllRaces()
        return dbRaces
    }

    suspend fun insertAllCharactersBlocking(dndCharacters: List<DndCharacter>) {
        for (character in dndCharacters) {
            val race = raceDao.getRaceByName(character.race!!.name)
            val dndClass = dndClassDao.getDndClassByName(character.dndClass!!.name)
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




    // No interaction with DB below this line


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
        val fireball = Spell("Fireball", 3, "Evocation")
        val magicMissile = Spell("Magic Missile", 1, "Evocation")

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

        val weapon1 = Weapon(1, "Sword", "1d6")

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
                1 to 2,
                2 to 1,
                3 to 6,
                4 to 2,
                5 to 1
            ),
            inventoryItems = setOf(item1, item2),
            image = null,
            weapon = null
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