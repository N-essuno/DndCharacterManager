package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.classes.Ability

enum class AbilityEnum(val ability: Ability) {
    STRENGTH(Ability("Strength")),
    DEXTERITY(Ability("Dexterity")),
    CONSTITUTION(Ability("Constitution")),
    INTELLIGENCE(Ability("Intelligence")),
    WISDOM(Ability("Wisdom")),
    CHARISMA(Ability("Charisma"))
}
