package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["dndCharacterId", "spellId"],
    foreignKeys = [
        ForeignKey(entity = DndClass::class,
            parentColumns = ["id"],
            childColumns = ["dndCharacterId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Spell::class,
            parentColumns = ["id"],
            childColumns = ["spellId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class DndCharacterPreparedSpellCrossRef(
    val dndCharacterId: Int,
    val spellId: Int
)