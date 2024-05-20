package it.brokenengineers.dnd_character_manager.data

data class DndClass (
    val name: String,
    val possibleSkillsProficiencies: List<Skill>,
    val savingThrowsProficiencies: List<Ability>,
    val primaryAbility: Ability,
)
