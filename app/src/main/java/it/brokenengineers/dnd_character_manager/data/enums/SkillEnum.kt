package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.Skill

enum class SkillEnum(val skill: Skill) {
    ACROBATICS(Skill("Acrobatics", AbilityEnum.STRENGTH.ability)),
    ANIMAL_HANDLING(Skill("Animal Handling", AbilityEnum.WISDOM.ability)),
    ARCANA(Skill("Arcana", AbilityEnum.INTELLIGENCE.ability)),
    ATHLETICS(Skill("Athletics", AbilityEnum.STRENGTH.ability)),
    DECEPTION(Skill("Deception", AbilityEnum.CHARISMA.ability)),
    HISTORY(Skill("History", AbilityEnum.INTELLIGENCE.ability)),
    INSIGHT(Skill("Insight", AbilityEnum.WISDOM.ability)),
    INTIMIDATION(Skill("Intimidation", AbilityEnum.CHARISMA.ability)),
    INVESTIGATION(Skill("Investigation", AbilityEnum.INTELLIGENCE.ability)),
    MEDICINE(Skill("Medicine", AbilityEnum.WISDOM.ability)),
    NATURE(Skill("Nature", AbilityEnum.INTELLIGENCE.ability)),
    PERCEPTION(Skill("Perception", AbilityEnum.WISDOM.ability)),
    PERFORMANCE(Skill("Performance", AbilityEnum.CHARISMA.ability)),
    PERSUASION(Skill("Persuasion", AbilityEnum.CHARISMA.ability)),
    RELIGION(Skill("Religion", AbilityEnum.INTELLIGENCE.ability)),
    SLEIGHT_OF_HAND(Skill("Sleight of Hand", AbilityEnum.DEXTERITY.ability)),
    STEALTH(Skill("Stealth", AbilityEnum.DEXTERITY.ability)),
    SURVIVAL(Skill("Survival", AbilityEnum.WISDOM.ability))
}