package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Spell (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    // TODO need to add spell description for the choose Spell view
    val description: String,
    val level: Int,
    val school: String
)