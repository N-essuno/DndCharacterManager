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
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic

@TypeConverters(Converters::class)
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Race::class,
            parentColumns = ["id"],
            childColumns = ["raceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DndCharacter (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @Ignore var race: Race?,
    var raceId: Int,
    @Ignore var dndClass: DndClass?,
    var dndClassId: Int,
    val image: String? = null,
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
    // Secondary constructor without the ignored field
    constructor(
        id: Int = 0,
        name: String,
        raceId: Int,
        dndClassId: Int,
        image: String? = null,
        level: Int,
        abilityValues: Map<Ability, Int>,
        skillProficiencies: Set<Skill>,
        remainingHp: Int,
        tempHp: Int,
        spellsKnown: Set<Spell>?,
        preparedSpells: Set<Spell>?,
        availableSpellSlots: Map<Int, Int>?,
        inventoryItems: Set<InventoryItem>?,
        weapon: Weapon?
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
        skillProficiencies,
        remainingHp,
        tempHp,
        spellsKnown,
        preparedSpells,
        availableSpellSlots,
        inventoryItems,
        weapon
    )

    override fun toString(): String {
        return "id: $id\n" +
                "Name: $name\n" +
                "Race: $race\n" +
                "RaceId: $raceId\n" +
                "DndClass: $dndClass\n" +
                "Level: $level\n" +
                "Ability Values: ${abilityValues.entries.joinToString { "${it.key.name}: ${it.value}" }}\n" +
                "Skill Proficiencies: ${skillProficiencies.joinToString { it.name }}\n" +
                "Remaining HP: $remainingHp\n" +
                "Temp HP: $tempHp\n" +
                "Spells Known: ${spellsKnown?.joinToString { it.name }}\n" +
                "Prepared Spells: ${preparedSpells?.joinToString { it.name }}\n" +
                "Available Spell Slots: ${availableSpellSlots?.entries?.joinToString { "${it.key}: ${it.value}" }}\n" +
                "Inventory Items: ${inventoryItems?.joinToString { it.name }}\n" +
                "Weapon: $weapon"
    }

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
        if (dndClass!!.savingThrowProficiencies.contains(abilityEnum.ability)) {
            return abilityModifier + getProficiencyBonus()
        } else {
            return abilityModifier
        }
    }

    fun isProficientInAbility(abilityEnum: AbilityEnum): Boolean {
        return dndClass!!.savingThrowProficiencies.contains(abilityEnum.ability)
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
        return abilityValues[dndClass!!.primaryAbility]!! + getProficiencyBonus()
    }

    fun getSpellDcSavingThrow(): Int {
        return 8 + getProficiencyBonus() + getAbilityModifier(dndClass!!.primaryAbility!!)
    }

    fun isSpellPrepared(spell: Spell): Boolean {
        return preparedSpells?.map { it.name }?.contains(spell.name) ?: false
    }
}