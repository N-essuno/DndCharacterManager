package it.brokenengineers.dnd_character_manager.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.data.Character
import it.brokenengineers.dnd_character_manager.data.Spell
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DndCharacterManagerRepository(val viewModel: DndCharacterManagerViewModel/* TODO add DAOs */) {
    val TAG = "MY_TAG"
    var allCharacters: MutableStateFlow<List<Character>> = MutableStateFlow(emptyList())
    val selectedCharacter: MutableStateFlow<Character?> = MutableStateFlow(null)

    val characters = getAllCharacters()

    fun init() {
        getAllCharacters()
    }

    private fun getAllCharacters() {
        viewModel.viewModelScope.launch {
            val characters = createMockCharacters()
            allCharacters.value = characters
        }
    }

    fun getCharacterById(id: Int) {
        viewModel.viewModelScope.launch {
            selectedCharacter.value = allCharacters.value.find { it.id == id }
            Log.i(TAG, "Repository: selectedCharacter updated: $selectedCharacter")
        }
    }

    private fun createMockCharacters(): List<Character> {
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

        val character1 = Character(
            id = 5,
            name = "Sam",
            proficiencyBonus = 2,
            race = eladrin,
            dndClass = wizard,
            level = 1,
            abilityValues = abilityValues1,
            skillProficiencies = setOf(arcana, history),
            remainingHp = 8,
            tempHp = 0,
            spellsKnown = setOf(fireball, magicMissile),
            preparedSpells = setOf(fireball),
            availableSpellSlots = mapOf(1 to 2)
        )

        val character2 = Character(
            id = 6,
            name = "Frodo",
            proficiencyBonus = 2,
            race = dwarf,
            dndClass = barbarian,
            level = 1,
            abilityValues = abilityValues2,
            skillProficiencies = setOf(athletics, acrobatics),
            remainingHp = 12,
            tempHp = 0,
            spellsKnown = null,
            preparedSpells = null,
            availableSpellSlots = null
        )

        return listOf(
            character1,
            character2
        )
    }
}