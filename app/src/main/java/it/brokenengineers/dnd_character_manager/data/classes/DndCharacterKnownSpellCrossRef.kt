package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["dndCharacterId", "spellId"],
    foreignKeys = [
        ForeignKey(entity = DndCharacter::class,
            parentColumns = ["id"],
            childColumns = ["dndCharacterId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Spell::class,
            parentColumns = ["id"],
            childColumns = ["spellId"],
            onDelete = ForeignKey.CASCADE)
    ])
/**
 * Represents the many-to-many relationship between a DndCharacter and a Spell
 * */
data class DndCharacterKnownSpellCrossRef(
    val dndCharacterId: Int,
    val spellId: Int
)