package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.classes.DndClass

enum class DndClassEnum(val dndClass: DndClass) {
    BARBARIAN(
        DndClass(
            id = 1,
            name = "Barbarian",
            savingThrowProficiencies = listOf(
                AbilityEnum.STRENGTH.ability,
                AbilityEnum.CONSTITUTION.ability),
            primaryAbility = AbilityEnum.STRENGTH.ability,
            primaryAbilityId = AbilityEnum.STRENGTH.ability.id,
            canUseSpells = false
        )
    ),
    WIZARD(
        DndClass(
            id = 2,
            name = "Wizard",
            savingThrowProficiencies = listOf(
                AbilityEnum.INTELLIGENCE.ability,
                AbilityEnum.WISDOM.ability),
            primaryAbility = AbilityEnum.INTELLIGENCE.ability,
            primaryAbilityId = AbilityEnum.INTELLIGENCE.ability.id,
            canUseSpells = true
        )
    )
}