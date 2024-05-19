package it.brokenengineers.dnd_character_manager.data

data class DndClass (
    val name: String,
    val hitDie: Int,
    val proficiencies: List<Skill>,
    val savingThrows: List<Ability>,
    val spellcastingAbility: Ability,
)
