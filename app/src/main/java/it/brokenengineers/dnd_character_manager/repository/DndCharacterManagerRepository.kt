package it.brokenengineers.dnd_character_manager.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.data.Ability
import it.brokenengineers.dnd_character_manager.data.AbilityScoreIncrease
import it.brokenengineers.dnd_character_manager.data.AbilityValue
import it.brokenengineers.dnd_character_manager.data.Character
import it.brokenengineers.dnd_character_manager.data.DndClass
import it.brokenengineers.dnd_character_manager.data.Race
import it.brokenengineers.dnd_character_manager.data.Skill
import it.brokenengineers.dnd_character_manager.data.Spell
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
        val strength = Ability("Strength")
        val dexterity = Ability("Dexterity")
        val constitution = Ability("Constitution")
        val intelligence = Ability("Intelligence")
        val charisma = Ability("Charisma")

        // Mock Ability Score Increases
        val strengthIncrease = AbilityScoreIncrease(strength, 2)
        val dexterityIncrease = AbilityScoreIncrease(dexterity, 1)
        val constitutionIncrease = AbilityScoreIncrease(constitution, 2)
        val charismaIncrease = AbilityScoreIncrease(charisma, 1)

        // Mock Skills
        val athletics = Skill("Athletics", strength)
        val acrobatics = Skill("Acrobatics", dexterity)
        val arcana = Skill("Arcana", intelligence)

        // Mock Races
        val eladrin = Race("Eladrin", 30, "Medium", listOf(dexterityIncrease, charismaIncrease))
        val dwarf = Race("Dwarf", 25, "Medium", listOf(strengthIncrease, constitutionIncrease))

        // Mock DndClasses
        val wizard = DndClass("Wizard", 6, listOf(arcana), listOf(intelligence, charisma), intelligence)
        val barbarian = DndClass("Barbarian", 12, listOf(athletics), listOf(strength, constitution), strength)

        // Mock Spells
        val fireball = Spell("Fireball", 3, "Evocation")
        val magicMissile = Spell("Magic Missile", 1, "Evocation")

        // Mock Ability Values
        val strengthValue = AbilityValue(strength, 15, 2, true)
        val dexterityValue = AbilityValue(dexterity, 14, 2, false)
        val constitutionValue = AbilityValue(constitution, 13, 1, false)
        val intelligenceValue = AbilityValue(intelligence, 12, 1, false)
        val charismaValue = AbilityValue(charisma, 10, 0, false)

        val character1 = Character(1, "John", eladrin, wizard, 1, listOf(intelligenceValue, charismaValue), listOf(arcana), listOf(fireball, magicMissile))
        val character2 = Character(2, "Jane", eladrin, barbarian, 2, listOf(strengthValue, dexterityValue), listOf(athletics, acrobatics), null)
        val character3 = Character(3, "Bob", dwarf, wizard, 3, listOf(intelligenceValue, constitutionValue), listOf(arcana), listOf(fireball, magicMissile))
        val character4 = Character(4, "Alice", dwarf, barbarian, 4, listOf(strengthValue, constitutionValue), listOf(athletics, acrobatics), null)

        return listOf(
            character1,
            character2,
            character3,
            character4
        )
    }
}