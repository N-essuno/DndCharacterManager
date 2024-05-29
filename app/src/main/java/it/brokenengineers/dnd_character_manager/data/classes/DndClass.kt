package it.brokenengineers.dnd_character_manager.data.classes

data class DndClass (
    val name: String,
    // TODO add parameters isMagicUser in class
    val possibleSkillsProficiencies: List<Skill>,
    val savingThrowProficiencies: List<Ability>,
    val primaryAbility: Ability,
)