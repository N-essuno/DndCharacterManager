package it.brokenengineers.dnd_character_manager.data.classes

enum class Feature(val featureName: String, val description: String) {
    RAGE("Rage", "In battle, you fight with primal ferocity. On your turn, you can enter a rage as a bonus action."),
    UNARMORED_DEFENSE("Unarmored Defense", "While you are not wearing any armor, your Armor Class equals 10 + your Dexterity modifier + your Constitution modifier."),
    RECKLESS_ATTACK("Reckless Attack", "When you make your first attack on your turn, you can decide to attack recklessly. Doing so gives you advantage on melee weapon attack rolls using Strength during this turn, but attack rolls against you have advantage until your next turn."),
    DANGER_SENSE("Danger Sense", "At 2nd level, you gain an uncanny sense of when things nearby aren’t as they should be, giving you an edge when you dodge away from danger."),
    PRIMAL_PATH("Primal Path", "At 3rd level, you choose a path that shapes the nature of your rage. Choose the Path of the Berserker or the Path of the Totem Warrior, both detailed at the end of the class description."),
    EXTRA_ATTACK("Extra Attack", "Beginning at 5th level, you can attack twice, instead of once, whenever you take the Attack action on your turn."),
    FAST_MOVEMENT("Fast Movement", "Starting at 5th level, your speed increases by 10 feet while you aren’t wearing heavy armor."),
    CANTRIP_FORMULAS("Cantrip Formulas", "At 3rd level, you learn to create a formula for a cantrip of your choice. The cantrip must be on the wizard spell list, and it must be of a level for which you have spell slots."),
}