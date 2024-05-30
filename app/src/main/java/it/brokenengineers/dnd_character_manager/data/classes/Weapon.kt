package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weapon (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    val damage: String
)