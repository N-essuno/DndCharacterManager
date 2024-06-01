package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.brokenengineers.dnd_character_manager.data.database.Converters

@TypeConverters(Converters::class)
@Entity
data class Race (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val walkSpeed: Int,
    val size: String,
    val abilityScoreIncrease: List<AbilityScoreIncrease>,
)