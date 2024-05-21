package it.brokenengineers.dnd_character_manager.data

import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum

data class Character (
    val id: Int,
    val name: String,
    val race: Race,
    val dndClass: DndClass,
    val level: Int,
    val abilityValues: Map<Ability, Int>,
    val skillProficiencies: Set<Skill>,
    var remainingHp: Int,
    var tempHp: Int,
    val spellsKnown: Set<Spell>?,
    val preparedSpells: Set<Spell>?,
    val availableSpellSlots: Map<Int, Int>?,
    val inventoryItems: Set<InventoryItem>?,
    val weapon: Weapon?
) {
    fun getProficiencyBonus(): Int {
        return when (level) {
            in 1..4 -> 2
            in 5..8 -> 3
            in 9..12 -> 4
            in 13..16 -> 5
            in 17..20 -> 6
            else -> -1
        }
    }

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

    fun getWalkSpeed(): Int {
        return race.walkSpeed
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

    private fun getAbilityModifier(ability: Ability): Int{
        return (abilityValues[ability]!! - 10) / 2
    }

    fun getSkills(): List<Pair<Skill, Int>> {
        val skills = mutableListOf<Pair<Skill, Int>>()
        for (enumEntry in SkillEnum.entries) {
            var skillValue = getAbilityModifier(enumEntry.skill.ability)
            if (skillProficiencies.contains(enumEntry.skill)) {
                skillValue += getProficiencyBonus()
            }
            val pair = Pair(enumEntry.skill, skillValue)
            skills.add(pair)
        }
        return skills
    }

    fun getSavingThrowBonus(abilityEnum: AbilityEnum): Int {
        val abilityModifier = getAbilityModifier(abilityEnum)
        if (dndClass.savingThrowProficiencies.contains(abilityEnum.ability)) {
            return abilityModifier + getProficiencyBonus()
        } else {
            return abilityModifier
        }
    }

    fun isProficientInAbility(abilityEnum: AbilityEnum): Boolean {
        return dndClass.savingThrowProficiencies.contains(abilityEnum.ability)
    }

    fun getMaxCarryWeight (): Double {
        return abilityValues[AbilityEnum.STRENGTH.ability]!! * 15.0
    }

    fun getCurrentCarryWeight (): Double {
        var totalWeight = 0.0
        inventoryItems?.forEach {
            totalWeight += it.weight * it.quantity
        }
        return totalWeight
    }
}