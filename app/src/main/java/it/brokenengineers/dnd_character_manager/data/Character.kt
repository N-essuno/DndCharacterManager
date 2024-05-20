package it.brokenengineers.dnd_character_manager.data

import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum

class Character (
    val id: Int,
    val name: String,
    val proficiencyBonus: Int,
    val race: Race,
    val dndClass: DndClass,
    val level: Int,
    val abilityValues: Map<Ability, Int>,
    val skillProficiencies: Set<Skill>,
    val remainingHp: Int,
    val tempHp: Int,
    val spellsKnown: Set<Spell>?,
    val preparedSpells: Set<Spell>?,
    val availableSpellSlots: Map<Int, Int>?
) {
    fun getArmorClass(): Int {
        val dexterityModifier = getAbilityModifier(AbilityEnum.DEXTERITY)
        if (dndClass.name == "Barbarian") {
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