package it.brokenengineers.dnd_character_manager.data

data class DndClass (
    val name: String,
    val possibleSkillsProficiencies: List<Skill>,
    val savingThrowProficiencies: List<Ability>,
    val primaryAbility: Ability,
)
