package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Ability::class,
            parentColumns = ["id"],
            childColumns = ["abilityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Skill(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @Ignore var ability: Ability?,
    val abilityId: Int
) {
    // Secondary constructor without the ignored field
    constructor(
        id: Int = 0,
        name: String,
        abilityId: Int
    ) : this(id, name, null, abilityId)
}