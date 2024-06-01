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
            childColumns = ["primaryAbilityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DndClass(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @Ignore var savingThrowProficiencies: List<Ability>,
    @Ignore var primaryAbility: Ability?,
    val primaryAbilityId: Int,
    val canUseSpells: Boolean
) {
    // Secondary constructor without the ignored fields
    constructor(
        id: Int = 0,
        name: String,
        primaryAbilityId: Int,
        canUseSpells: Boolean
    ) : this(id, name, emptyList(), null, primaryAbilityId, canUseSpells)
}