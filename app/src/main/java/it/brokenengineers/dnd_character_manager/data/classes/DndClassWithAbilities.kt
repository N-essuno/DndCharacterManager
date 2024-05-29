package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Junction
import androidx.room.Relation

data class DndClassWithAbilities(
    var classId: Int,
    @Relation(
        entity = Ability::class,
        parentColumn = "classId",
        entityColumn = "id",
        associateBy = Junction(DndClassAbilityCrossRef::class)
    )
    val abilities: List<Ability>
)