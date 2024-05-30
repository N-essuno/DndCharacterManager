package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.classes.Skill

enum class SkillEnum(val skill: Skill) {
    // TODO remember later to add the Ability Id
    ACROBATICS(Skill(1, "Acrobatics", AbilityEnum.STRENGTH.ability, AbilityEnum.STRENGTH.ability.id)),
    ANIMAL_HANDLING(Skill(2, "Animal Handling", AbilityEnum.WISDOM.ability, AbilityEnum.WISDOM.ability.id)),
    ARCANA(Skill(3, "Arcana", AbilityEnum.INTELLIGENCE.ability, AbilityEnum.INTELLIGENCE.ability.id)),
    ATHLETICS(Skill(4, "Athletics", AbilityEnum.STRENGTH.ability, AbilityEnum.STRENGTH.ability.id)),
    DECEPTION(Skill(5, "Deception", AbilityEnum.CHARISMA.ability, AbilityEnum.CHARISMA.ability.id)),
    HISTORY(Skill(6, "History", AbilityEnum.INTELLIGENCE.ability, AbilityEnum.INTELLIGENCE.ability.id)),
    INSIGHT(Skill(7, "Insight", AbilityEnum.WISDOM.ability, AbilityEnum.WISDOM.ability.id)),
    INTIMIDATION(Skill(8, "Intimidation", AbilityEnum.CHARISMA.ability, AbilityEnum.CHARISMA.ability.id)),
    INVESTIGATION(Skill(9, "Investigation", AbilityEnum.INTELLIGENCE.ability, AbilityEnum.INTELLIGENCE.ability.id)),
    MEDICINE(Skill(10, "Medicine", AbilityEnum.WISDOM.ability, AbilityEnum.WISDOM.ability.id)),
    NATURE(Skill(11, "Nature", AbilityEnum.INTELLIGENCE.ability, AbilityEnum.INTELLIGENCE.ability.id)),
    PERCEPTION(Skill(12, "Perception", AbilityEnum.WISDOM.ability, AbilityEnum.WISDOM.ability.id)),
    PERFORMANCE(Skill(13,"Performance", AbilityEnum.CHARISMA.ability, AbilityEnum.CHARISMA.ability.id)),
    PERSUASION(Skill(14, "Persuasion", AbilityEnum.CHARISMA.ability, AbilityEnum.CHARISMA.ability.id)),
    RELIGION(Skill(15, "Religion", AbilityEnum.INTELLIGENCE.ability, AbilityEnum.INTELLIGENCE.ability.id)),
    SLEIGHT_OF_HAND(Skill(16, "Sleight of Hand", AbilityEnum.DEXTERITY.ability, AbilityEnum.DEXTERITY.ability.id)),
    STEALTH(Skill(17, "Stealth", AbilityEnum.DEXTERITY.ability, AbilityEnum.DEXTERITY.ability.id)),
    SURVIVAL(Skill(18, "Survival", AbilityEnum.WISDOM.ability, AbilityEnum.WISDOM.ability.id))
}