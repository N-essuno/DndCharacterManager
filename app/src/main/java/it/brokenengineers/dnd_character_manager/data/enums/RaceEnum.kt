package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.classes.AbilityScoreIncrease
import it.brokenengineers.dnd_character_manager.data.classes.Race

enum class RaceEnum(val race: Race){
    DWARF(
        Race(
        "Dwarf",
        25,
        "Medium",
        listOf(
            AbilityScoreIncrease(AbilityEnum.CONSTITUTION.ability, 1),
            AbilityScoreIncrease(AbilityEnum.STRENGTH.ability, 1)
        )
    )
    ),
    ELADRIN(
        Race(
        "Eladrin",
        30,
        "Medium",
        listOf(
            AbilityScoreIncrease(AbilityEnum.INTELLIGENCE.ability, 2),
            AbilityScoreIncrease(AbilityEnum.WISDOM.ability, 2)
        )
    )
    ),
}