package it.brokenengineers.dnd_character_manager.data.classes

data class Race (
    val name: String,
    val walkSpeed: Int,
    val size: String,
    val abilityScoreIncrease: List<AbilityScoreIncrease>,
)