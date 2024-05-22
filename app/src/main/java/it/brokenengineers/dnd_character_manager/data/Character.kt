package it.brokenengineers.dnd_character_manager.data

import android.net.Uri
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum

class Character (
    val name: String,
    val dndClass: DndClass,
    val race: Race,
    var image: Uri? = null
) {
    var id: Int = computeId()
    var proficiencyBonus: Int = 3
    var level: Int = 1
    var abilityValues: Map<Ability, Int> = getAbilityValuesForRace(race)
    var skillProficiencies: Set<Skill> = getProficienciesForClass(dndClass)
    var maxHp: Int = getMaxHp()
    var remainingHp: Int = maxHp
    var tempHp: Int = 0
    var spellsKnown: Set<Spell>? = emptySet()
    var preparedSpells: Set<Spell>? = emptySet()
    var availableSpellSlots: Map<Int, Int>? = initializeSpellSlots(dndClass)

    fun getArmorClass(): Int {
        val dexterityModifier = getAbilityModifier(AbilityEnum.DEXTERITY)
        if (this.dndClass.name == "Barbarian") {
            val constitutionModifier = getAbilityModifier(AbilityEnum.CONSTITUTION)
            return 10 + constitutionModifier + dexterityModifier
        } else {
            return 10 + dexterityModifier
        }
    }

    fun getInitiative(): Int {
        return getAbilityModifier(AbilityEnum.DEXTERITY)
    }

    fun getMaxHp(): Int {
        if (dndClass.name == "Barbarian") {
            if (level == 1) {
                return 12 + getAbilityModifier(AbilityEnum.CONSTITUTION)
            } else {
                return 12 + 7 + getAbilityModifier(AbilityEnum.CONSTITUTION) * level
            }
        } else if (dndClass.name == "Wizard") {
            if (level == 1) {
                return 6 + getAbilityModifier(AbilityEnum.CONSTITUTION)
            } else {
                return 6 + 4 + getAbilityModifier(AbilityEnum.CONSTITUTION) * level
            }
        } else {
            return -1
        }
    }

    fun getAbilityModifier(abilityEnum: AbilityEnum): Int{
        return (abilityValues[abilityEnum.ability]!! - 10) / 2
    }
}

private fun initializeSpellSlots(dndClass: DndClass): Map<Int, Int> {
    return when (dndClass) {
        DndClassEnum.BARBARIAN.dndClass -> mapOf()
        DndClassEnum.WIZARD.dndClass -> mapOf(
            1 to 2
        )
        else -> mapOf()
    }
}

private fun getAbilityValuesForRace(race: Race): Map<Ability, Int> {
    when (race) {
        RaceEnum.DWARF.race -> return mapOf(
            AbilityEnum.STRENGTH.ability to 14,
            AbilityEnum.DEXTERITY.ability to 10,
            AbilityEnum.CONSTITUTION.ability to 16,
            AbilityEnum.INTELLIGENCE.ability to 8,
            AbilityEnum.WISDOM.ability to 12,
            AbilityEnum.CHARISMA.ability to 10
        )
        RaceEnum.ELADRIN.race -> return mapOf(
            AbilityEnum.STRENGTH.ability to 10,
            AbilityEnum.DEXTERITY.ability to 12,
            AbilityEnum.CONSTITUTION.ability to 8,
            AbilityEnum.INTELLIGENCE.ability to 14,
            AbilityEnum.WISDOM.ability to 10,
            AbilityEnum.CHARISMA.ability to 16
        )
        else -> return mapOf(
            AbilityEnum.STRENGTH.ability to 10,
            AbilityEnum.DEXTERITY.ability to 10,
            AbilityEnum.CONSTITUTION.ability to 10,
            AbilityEnum.INTELLIGENCE.ability to 10,
            AbilityEnum.WISDOM.ability to 10,
            AbilityEnum.CHARISMA.ability to 10
        )
    }
}

fun computeId(): Int {
    // TODO when database is implemented, return the id from the database
    return 0
}

private fun getProficienciesForClass(dndClass: DndClass): Set<Skill> {
    return when (dndClass) {
        DndClassEnum.BARBARIAN.dndClass -> setOf(
            SkillEnum.ANIMAL_HANDLING.skill,
            SkillEnum.ATHLETICS.skill,
            SkillEnum.INTIMIDATION.skill,
        )
        DndClassEnum.WIZARD.dndClass -> setOf(
            SkillEnum.ARCANA.skill,
            SkillEnum.HISTORY.skill,
            SkillEnum.INSIGHT.skill,
        )
        else -> setOf()
    }
}

