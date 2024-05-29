package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum

@Entity
data class Ability (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
) {
    override fun toString(): String {
        return "Ability id: $id name: $name"
    }

    companion object {
        fun valueOf(name: String): Ability {
            return when (name.lowercase()) {
                "strength" -> AbilityEnum.STRENGTH.ability
                "dexterity" -> AbilityEnum.DEXTERITY.ability
                "constitution" -> AbilityEnum.CONSTITUTION.ability
                "intelligence" -> AbilityEnum.INTELLIGENCE.ability
                "wisdom" -> AbilityEnum.WISDOM.ability
                "charisma" -> AbilityEnum.CHARISMA.ability
                else -> throw IllegalArgumentException("Invalid ability name: $name")
            }
        }
    }
}