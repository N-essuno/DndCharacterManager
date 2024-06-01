package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.Relation

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

/**
 * Represents a DndCharacter with a set of known spells
 * */
data class DndCharacterWithKnownSpells(
    var dndCharacterId: Int,
    @Relation(
        entity = Spell::class,
        parentColumn = "dndCharacterId",
        entityColumn = "id",
        associateBy = Junction(DndCharacterKnownSpellCrossRef::class)
    )
    val spells: Set<Spell>
)