package it.brokenengineers.dnd_character_manager.data

data class Character (
    val id: Int,
    val name: String,
    val race: Race,
    val DndClass: DndClass,
    val level: Int,
    val abilityValues: List<AbilityValue>,
    val proficiencies: List<Skill>,
)