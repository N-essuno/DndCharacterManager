package it.brokenengineers.dnd_character_manager.data.classes

import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum

data class Ability (
    val name: String,
) {
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