package it.brokenengineers.dnd_character_manager

import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.data.initAbilityValuesForRace
import it.brokenengineers.dnd_character_manager.data.initProficienciesForClass
import it.brokenengineers.dnd_character_manager.data.initSpellSlotsForClass
import org.junit.Test

class DndCharacterTest {
    @Test
    fun testAutoBuildCharacter() {
        val name = "Vincentium"
        val race = RaceEnum.DWARF.race
        val dndClass = DndClassEnum.BARBARIAN.dndClass

        // get other params from Character Utils
        val abilityValues = initAbilityValuesForRace(race)
        val spellSlots = initSpellSlotsForClass(dndClass)
        val proficiencies = initProficienciesForClass(dndClass)
        val maxHp = getMaxHpStatic(dndClass, 1, abilityValues)

        val newDndCharacter = DndCharacter(
            name = name,
            race = race,
            dndClass = dndClass,
            level = 1,
            abilityValues = abilityValues,
            skillProficiencies = proficiencies,
            remainingHp = maxHp,
            tempHp = 0,
            spellsKnown = emptySet(),
            preparedSpells = emptySet(),
            availableSpellSlots = spellSlots,
            inventoryItems = emptySet(),
            weapon = null
        )

        println(newDndCharacter)

        assert(newDndCharacter.name == name)
        assert(newDndCharacter.race == race)
        assert(newDndCharacter.dndClass == dndClass)
        assert(newDndCharacter.level == 1)
        assert(newDndCharacter.abilityValues == abilityValues)
        assert(newDndCharacter.skillProficiencies == proficiencies)
        assert(newDndCharacter.remainingHp == maxHp)
        assert(newDndCharacter.tempHp == 0)
        assert(newDndCharacter.spellsKnown == emptySet<Spell>())
        assert(newDndCharacter.preparedSpells == emptySet<Spell>())
        assert(newDndCharacter.availableSpellSlots == spellSlots)
        assert(newDndCharacter.inventoryItems == emptySet<String>())
        assert(newDndCharacter.weapon == null)
    }
}