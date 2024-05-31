package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Spell (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    val level: Int,
    val school: String
)