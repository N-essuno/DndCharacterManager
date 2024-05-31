package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Junction
import androidx.room.Relation

data class DndCharacterWithPreparedSpells(
    var dndCharacterId: Int,
    @Relation(
        entity = Spell::class,
        parentColumn = "dndCharacterId",
        entityColumn = "id",
        associateBy = Junction(DndCharacterPreparedSpellCrossRef::class)
    )
    val spells: Set<Spell>
)