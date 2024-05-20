package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.DndClass

enum class DndClassEnum(val dndClass: DndClass) {
    BARBARIAN(
        DndClass(
            name = "Barbarian",
            possibleSkillsProficiencies =  listOf(
                SkillEnum.ANIMAL_HANDLING.skill,
                SkillEnum.ATHLETICS.skill,
                SkillEnum.INTIMIDATION.skill,
                SkillEnum.NATURE.skill,
                SkillEnum.PERCEPTION.skill,
                SkillEnum.SURVIVAL.skill
            ),
            savingThrowsProficiencies = listOf(
                AbilityEnum.STRENGTH.ability,
                AbilityEnum.CONSTITUTION.ability),
            primaryAbility = AbilityEnum.STRENGTH.ability
        )
    ),
    WIZARD(
        DndClass(
            name = "Wizard",
            possibleSkillsProficiencies = listOf(
                SkillEnum.ARCANA.skill,
                SkillEnum.HISTORY.skill,
                SkillEnum.INSIGHT.skill,
                SkillEnum.INVESTIGATION.skill,
                SkillEnum.MEDICINE.skill,
                SkillEnum.PERCEPTION.skill,
                SkillEnum.RELIGION.skill
            ),
            savingThrowsProficiencies = listOf(
                AbilityEnum.INTELLIGENCE.ability,
                AbilityEnum.WISDOM.ability),
            primaryAbility = AbilityEnum.INTELLIGENCE.ability
        )
    )
}