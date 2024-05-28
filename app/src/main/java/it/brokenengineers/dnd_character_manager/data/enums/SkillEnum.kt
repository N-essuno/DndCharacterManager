package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.classes.Skill

enum class SkillEnum(val skill: Skill) {
    ACROBATICS(Skill(0, "Acrobatics", AbilityEnum.STRENGTH.ability)),
    ANIMAL_HANDLING(Skill(1, "Animal Handling", AbilityEnum.WISDOM.ability)),
    ARCANA(Skill(2, "Arcana", AbilityEnum.INTELLIGENCE.ability)),
    ATHLETICS(Skill(3, "Athletics", AbilityEnum.STRENGTH.ability)),
    DECEPTION(Skill(4, "Deception", AbilityEnum.CHARISMA.ability)),
    HISTORY(Skill(5, "History", AbilityEnum.INTELLIGENCE.ability)),
    INSIGHT(Skill(6, "Insight", AbilityEnum.WISDOM.ability)),
    INTIMIDATION(Skill(7, "Intimidation", AbilityEnum.CHARISMA.ability)),
    INVESTIGATION(Skill(8, "Investigation", AbilityEnum.INTELLIGENCE.ability)),
    MEDICINE(Skill(9, "Medicine", AbilityEnum.WISDOM.ability)),
    NATURE(Skill(10, "Nature", AbilityEnum.INTELLIGENCE.ability)),
    PERCEPTION(Skill(11, "Perception", AbilityEnum.WISDOM.ability)),
    PERFORMANCE(Skill(12,"Performance", AbilityEnum.CHARISMA.ability)),
    PERSUASION(Skill(13, "Persuasion", AbilityEnum.CHARISMA.ability)),
    RELIGION(Skill(14, "Religion", AbilityEnum.INTELLIGENCE.ability)),
    SLEIGHT_OF_HAND(Skill(15, "Sleight of Hand", AbilityEnum.DEXTERITY.ability)),
    STEALTH(Skill(16, "Stealth", AbilityEnum.DEXTERITY.ability)),
    SURVIVAL(Skill(17, "Survival", AbilityEnum.WISDOM.ability))
}