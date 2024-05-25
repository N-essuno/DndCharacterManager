package it.brokenengineers.dnd_character_manager.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.classes.Weapon
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterDao
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DndCharacterManagerRepository(
    private val viewModel: DndCharacterManagerViewModel,
    private val dndCharacterDao: DndCharacterDao
) {
    private val tag: String = DndCharacterManagerRepository::class.java.simpleName
    var allCharacters: MutableStateFlow<MutableList<DndCharacter>> = MutableStateFlow(mutableListOf())
    val selectedDndCharacter: MutableStateFlow<DndCharacter?> = MutableStateFlow(null)

    fun init() {
        viewModel.viewModelScope.launch {
            getAllCharacters()
        }
    }

    fun addCharacter(dndCharacter: DndCharacter) {
        viewModel.viewModelScope.launch {
            allCharacters.value.add(dndCharacter)

            // Insert character into database
            dndCharacterDao.insert(dndCharacter)
        }
    }

    private fun getAllCharacters() {
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
            dndClass = wizard,
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
            dndClass = barbarian,
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
            weapon = weapon1
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