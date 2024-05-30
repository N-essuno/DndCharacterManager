package it.brokenengineers.dnd_character_manager.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.classes.Skill
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.classes.Weapon
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.data.getMaxSpellSlots
import kotlin.math.ceil
import kotlin.math.min

@TypeConverters(Converters::class)
@Entity
data class DndCharacter (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val race: Race,
    val dndClass: DndClass,
    val image: String? = null,
    val level: Int,
    val abilityValues: Map<Ability, Int>,
    val skillProficiencies: Set<Skill>,
    var remainingHp: Int,
    var tempHp: Int,
    val spellsKnown: Set<Spell>?,
    val preparedSpells: Set<Spell>?,
    val availableSpellSlots: Map<Int, Int>?, // TODO refactor name to remainingSpellSlots
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

    fun getAbilityValue(abilityEnum: AbilityEnum): Int {
        return abilityValues[abilityEnum.ability] ?: 0
    }

    fun getArmorClass(): Int {
        val dexterityModifier = getAbilityModifier(AbilityEnum.DEXTERITY)
        if (dndClass == DndClassEnum.BARBARIAN.dndClass) {
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
        return getMaxHpStatic(dndClass, level, abilityValues)
    }

    fun getAbilityModifier(abilityEnum: AbilityEnum): Int {
        return (abilityValues[abilityEnum.ability]!! - 10) / 2
    }

    private fun getAbilityModifier(ability: Ability): Int {
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

    fun getMaxCarryWeight(): Double {
        return abilityValues[AbilityEnum.STRENGTH.ability]!! * 15.0
    }

    fun getCurrentCarryWeight(): Double {
        var totalWeight = 0.0
        inventoryItems?.forEach {
            totalWeight += it.weight * it.quantity
        }
        return totalWeight
    }

    fun getAttackBonus(): Int {
        return abilityValues[dndClass.primaryAbility]!! + getProficiencyBonus()
    }

    fun getSpellDcSavingThrow(): Int {
        return 8 + getProficiencyBonus() + getAbilityModifier(dndClass.primaryAbility)
    }

    fun isSpellPrepared(spell: Spell): Boolean {
        return preparedSpells?.map { it.name }?.contains(spell.name) ?: false
    }

    fun canRecoverSpells(): Boolean {
        return getRecoverableSpellSlots().values.sum() > 0
//                && dndClass.isMagicUser TODO after adding isMagicUser to DndClass remove comment
    }

    fun getNumRecoverableSlotsForShortRest(): Int {
        // level divided by two, rounded up
        return ceil(level.toDouble() / 2).toInt()
    }

    fun getMaxSpellSlotsForSpellLevel(spellLevel: Int): Int {
        return getMaxSpellSlots(spellLevel, dndClass, level)
    }

    fun getRecoverableSpellSlots(): Map<Int, Int> {
        val recoverableSlots = mutableMapOf<Int, Int>()
        for (i in 1..5) {
            val maxSlots = getMaxSpellSlotsForSpellLevel(i)
            val remainingSlots = availableSpellSlots?.get(i) ?: maxSlots
            val recoverableSlotsForLevel = maxSlots - remainingSlots
            recoverableSlots[i] = recoverableSlotsForLevel
        }
        return recoverableSlots
    }

    fun getNumPrepareableSpells(): Int {
        return getAbilityModifier(getPrimaryAbility())
    }

    fun getPrimaryAbility(): Ability {
        return dndClass.primaryAbility
    }

    fun getSpellLevelsOfPreparedSpells(): Set<Int> {
        return preparedSpells?.map { it.level }?.toSet() ?: setOf()
    }

    fun getMaxSpellLevelInPreparedSpells(): Int {
        return getSpellLevelsOfPreparedSpells().maxOrNull() ?: 0
    }

    fun getHpAfterShortRest(): Int {
        return min(remainingHp + getMaxHp() / 2, getMaxHp())
    }
}