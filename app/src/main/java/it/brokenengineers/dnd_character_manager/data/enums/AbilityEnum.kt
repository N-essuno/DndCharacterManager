package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.classes.Ability

enum class AbilityEnum(val ability: Ability) {
    STRENGTH(Ability(1, "Strength")),
    DEXTERITY(Ability(2, "Dexterity")),
    CONSTITUTION(Ability(3, "Constitution")),
    INTELLIGENCE(Ability(4, "Intelligence")),
    WISDOM(Ability(5, "Wisdom")),
    CHARISMA(Ability(6, "Charisma"))
}
