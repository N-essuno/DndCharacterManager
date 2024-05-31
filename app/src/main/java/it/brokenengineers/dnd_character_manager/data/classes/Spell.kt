package it.brokenengineers.dnd_character_manager.data.classes

data class Spell (
    val name: String,
    // TODO need to add spell description for the choose Spell view
    val description: String,
    val level: Int,
    val school: String
)