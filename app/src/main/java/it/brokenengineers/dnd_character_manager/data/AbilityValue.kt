package it.brokenengineers.dnd_character_manager.data

data class AbilityValue (
    val ability: Ability,
    val value: Int,
    val modifier: Int,
    val savingThrow: Boolean
)
