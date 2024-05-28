package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Skill (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val ability: Ability,
)