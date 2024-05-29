package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["classId", "abilityId"],
    foreignKeys = [
        ForeignKey(entity = DndClass::class,
            parentColumns = ["id"],
            childColumns = ["classId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Ability::class,
            parentColumns = ["id"],
            childColumns = ["abilityId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class DndClassAbilityCrossRef(
    val classId: Int,
    val abilityId: Int
)