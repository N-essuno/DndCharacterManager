package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["dndCharacterId", "skillId"],
    foreignKeys = [
        ForeignKey(entity = DndCharacter::class,
            parentColumns = ["id"],
            childColumns = ["dndCharacterId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Skill::class,
            parentColumns = ["id"],
            childColumns = ["skillId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class DndCharacterSkillCrossRef(
    val dndCharacterId: Int,
    val skillId: Int
)