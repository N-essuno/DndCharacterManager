package it.brokenengineers.dnd_character_manager.data

import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.classes.Skill
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum

fun initAbilityValuesForRace(race: Race): Map<Ability, Int> {
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

fun initEmptySpellSlots(): Map<Int, Int> {
    return mapOf(
        1 to 0,
        2 to 0,
        3 to 0,
        4 to 0,
        5 to 0
    )
}

/**
 * Initializes the spell slots for a level 1 character of the given class
 * @param dndClass the class to initialize the spell slots for
 * @return a map of spell level to number of spell slots
 */
fun initSpellSlotsForClass(dndClass: DndClass): Map<Int, Int> {
    return when (dndClass) {
        DndClassEnum.BARBARIAN.dndClass -> mapOf()
        DndClassEnum.WIZARD.dndClass -> mapOf(
            1 to 2
        )

        else -> mapOf()
    }
}

fun initProficienciesForClass(dndClass: DndClass): Set<Skill> {
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

fun getMaxHpStatic(dndClass: DndClass, level: Int, abilityValues: Map<Ability, Int>): Int {
    return if (dndClass.name == DndClassEnum.BARBARIAN.dndClass.name) {
        if (level == 1) {
            12 + getAbilityModifierStatic(AbilityEnum.CONSTITUTION, abilityValues)
        } else {
            12 + (7 + getAbilityModifierStatic(AbilityEnum.CONSTITUTION, abilityValues)) * (level - 1)
        }
    } else if (dndClass.name == DndClassEnum.WIZARD.dndClass.name) {
        if (level == 1) {
            6 + getAbilityModifierStatic(AbilityEnum.CONSTITUTION, abilityValues)
        } else {
            6 + (4 + getAbilityModifierStatic(AbilityEnum.CONSTITUTION, abilityValues)) * (level - 1)
        }
    } else {
        -1
    }
}

fun getAbilityModifierStatic(
    abilityEnum: AbilityEnum,
    abilityValues: Map<Ability, Int>
): Int {
    // get ability value from abilityValues filtering by name
    val abilityValue = getAbilityValueStatic(abilityEnum, abilityValues)
    return (abilityValue!! - 10) / 2
}

fun getMaxPreparedSpells(dndClass: DndClass, level: Int, abilityValues: Map<Ability, Int>): Int {
    val abilityEnum = AbilityEnum.valueOf(dndClass.primaryAbility!!.name)

    return if (dndClass.name == DndClassEnum.WIZARD.dndClass.name) {
        level + getAbilityModifierStatic(abilityEnum, abilityValues)
    } else {
        0
    }
}

fun getAbilityValueStatic(abilityEnum: AbilityEnum, abilityValues: Map<Ability, Int>): Int? {
    return abilityValues.entries.find { it.key.name == abilityEnum.ability.name }?.value
}

fun getRagesPerDay(level: Int): Int {
    return when (level) {
        1, 2 -> {
            2
        }
        3,4,5 -> {
            3
        }
        else -> {
            0
        }
    }
}

/**
 * Returns the maximum number of spell slots for a given spell level, dnd class and character level
 * @param spellLevel the level of the spell
 * @param dndClass the class of the character
 * @param level the level of the character
 * @return the maximum number of spell slots
 */
fun getMaxSpellSlots(spellLevel: Int, dndClass: DndClass, level: Int): Int {
    if (dndClass.name == DndClassEnum.WIZARD.dndClass.name) {
        when (level) {
            1 -> return when (spellLevel) {
                1 -> 2
                else -> 0
            }

            2 -> return when (spellLevel) {
                1 -> 3
                else -> 0
            }

            3 -> return when (spellLevel) {
                1 -> 4
                2 -> 2
                else -> 0
            }

            4 -> return when (spellLevel) {
                1 -> 4
                2 -> 3
                else -> 0
            }

            5 -> return when (spellLevel) {
                1 -> 4
                2 -> 3
                3 -> 2
                else -> 0
            }

            6 -> return when (spellLevel) {
                1 -> 4
                2 -> 3
                3 -> 3
                else -> 0
            }

            7 -> return when (spellLevel) {
                1 -> 4
                2 -> 3
                3 -> 3
                4 -> 1
                else -> 0
            }

            8 -> return when (spellLevel) {
                1 -> 4
                2 -> 3
                3 -> 3
                4 -> 2
                else -> 0
            }

            9 -> return when (spellLevel) {
                1 -> 4
                2 -> 3
                3 -> 3
                4 -> 3
                5 -> 1
                else -> 0
            }

            10 -> return when (spellLevel) {
                1 -> 4
                2 -> 3
                3 -> 3
                4 -> 3
                5 -> 2
                else -> 0
            }

            else -> return 0
        }
    } else {
        return 0
    }
}
