package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.brokenengineers.dnd_character_manager.data.database.Converters
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
import it.brokenengineers.dnd_character_manager.data.getAbilityValueStatic
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.data.getMaxSpellSlots
import kotlin.math.ceil
import kotlin.math.min

@TypeConverters(Converters::class)
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Race::class,
            parentColumns = ["id"],
            childColumns = ["raceId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DndClass::class,
            parentColumns = ["id"],
            childColumns = ["dndClassId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Weapon::class,
            parentColumns = ["id"],
            childColumns = ["weaponId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DndCharacter(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    @Ignore var race: Race?,
    var raceId: Int,
    @Ignore var dndClass: DndClass?,
    var dndClassId: Int,
    val image: String? = null,
    val level: Int,
    val abilityValues: Map<Ability, Int>,
    @Ignore var skillProficiencies: Set<Skill>?,
    var remainingHp: Int,
    var tempHp: Int,
    @Ignore var spellsKnown: Set<Spell>?,
    @Ignore var preparedSpells: Set<Spell>?,
    val availableSpellSlots: Map<Int, Int>?, // TODO refactor name to remainingSpellSlots
    val inventoryItems: Set<InventoryItem>?,
    @Ignore var weapon: Weapon?,
    var weaponId: Int
) {
    // Secondary constructor without the ignored field
    constructor(
        id: Int = 0,
        name: String,
        raceId: Int,
        dndClassId: Int,
        image: String? = null,
        level: Int,
        abilityValues: Map<Ability, Int>,
        remainingHp: Int,
        tempHp: Int,
        availableSpellSlots: Map<Int, Int>?,
        inventoryItems: Set<InventoryItem>?,
        weaponId: Int
    ) : this(
        id,
        name,
        null, // Default value for the ignored field
        raceId,
        null,
        dndClassId,
        image,
        level,
        abilityValues,
        null,
        remainingHp,
        tempHp,
        null,
        null,
        availableSpellSlots,
        inventoryItems,
        null,
        weaponId
    )

    // TODO check in all class that abilityValues are correctly retrieved, now there are also ids

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
        return getAbilityValueStatic(abilityEnum, abilityValues) ?: 0
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
        return race!!.walkSpeed
    }

    fun getMaxHp(): Int {
        return getMaxHpStatic(dndClass!!, level, abilityValues)
    }

    fun getAbilityModifier(abilityEnum: AbilityEnum): Int {
        return (getAbilityValueStatic(abilityEnum, abilityValues)!! - 10) / 2
    }

    private fun getAbilityModifier(ability: Ability): Int {
        val abilityEnum = AbilityEnum.valueOf(ability.name.uppercase())
        return (getAbilityValue(abilityEnum) - 10) / 2
    }

    fun getSkills(): List<Pair<Skill, Int>> {
        val skills = mutableListOf<Pair<Skill, Int>>()
        for (enumEntry in SkillEnum.entries) {
            var skillValue = getAbilityModifier(enumEntry.skill.ability!!)

            if (skillProficiencies!!.map{it.name}.contains(enumEntry.skill.name)) {
                skillValue += getProficiencyBonus()
            }
            val pair = Pair(enumEntry.skill, skillValue)
            skills.add(pair)
        }
        return skills
    }

    fun getSavingThrowBonus(abilityEnum: AbilityEnum): Int {
        val abilityModifier = getAbilityModifier(abilityEnum)
        if (dndClass!!.savingThrowProficiencies.contains(abilityEnum.ability)) {
            return abilityModifier + getProficiencyBonus()
        } else {
            return abilityModifier
        }
    }

    fun isProficientInAbility(abilityEnum: AbilityEnum): Boolean {
        return dndClass!!.savingThrowProficiencies.map { it.name }.contains(abilityEnum.ability.name)
    }

    fun getMaxCarryWeight(): Double {
        return getAbilityValue(AbilityEnum.STRENGTH) * 15.0
    }

    fun getCurrentCarryWeight(): Double {
        var totalWeight = 0.0
        inventoryItems?.forEach {
            totalWeight += it.weight * it.quantity
        }
        return totalWeight
    }

    fun getAttackBonus(): Int {
        val primaryAbilityUppercase = dndClass!!.primaryAbility!!.name.uppercase()
        val primaryAbilityValue = getAbilityValue(AbilityEnum.valueOf(primaryAbilityUppercase))
        return primaryAbilityValue + getProficiencyBonus()
    }

    fun getSpellDcSavingThrow(): Int {
        return 8 + getProficiencyBonus() + getAbilityModifier(dndClass!!.primaryAbility!!)
    }

    fun isSpellPrepared(spell: Spell): Boolean {
        return preparedSpells?.map { it.name }?.contains(spell.name) ?: false
    }

    fun canUseSpells(): Boolean {
        return dndClass!!.canUseSpells
    }

    fun getNumRecoverableSlotsForShortRest(): Int {
        // level divided by two, rounded up
        return ceil(level.toDouble() / 2).toInt()
    }

    fun getMaxSpellSlotsForSpellLevel(spellLevel: Int): Int {
        return getMaxSpellSlots(spellLevel, dndClass!!, level)
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

    fun getMaxRecoverableSpellSlots(): Map<Int, Int> {
        val recoverableSlots = mutableMapOf<Int, Int>()
        for (i in 1..5) {
            val maxSlots = getMaxSpellSlotsForSpellLevel(i)
            recoverableSlots[i] = maxSlots
        }
        return recoverableSlots
    }

    fun getNumPrepareableSpells(): Int {
        return getAbilityModifier(getPrimaryAbility())
    }

    fun getPrimaryAbility(): Ability {
        return dndClass!!.primaryAbility!!
    }

    /**
     * Returns the levels of the spells that are prepared
     */
    fun getSpellLevelsOfPreparedSpells(): Set<Int> {
        return preparedSpells?.map { it.level }?.toSet() ?: setOf()
    }

    fun getMaxSpellLevelInPreparedSpells(): Int {
        return getSpellLevelsOfPreparedSpells().maxOrNull() ?: 0
    }

    fun getHpAfterShortRest(): Int {
        return min(remainingHp + getMaxHp() / 2, getMaxHp())
    }

    /**
     * Returns the max level of spell that can be learned by the character.
     */
    fun getMaxSpellLevel(): Int {
        // Get the map of recoverable spell slots, the key is the spell level and the value is the number of slots
        val spellSlotsRecoverable = getMaxRecoverableSpellSlots()
        // Filter out the entries with value > 0 and get the max key, this tells us the highest spell level that can be learned
        return spellSlotsRecoverable.entries
            .filter { it.value > 0 }
            .maxByOrNull { it.key }?.key ?: 0
    }
}