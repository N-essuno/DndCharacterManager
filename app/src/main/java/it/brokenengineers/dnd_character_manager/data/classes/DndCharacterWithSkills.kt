package it.brokenengineers.dnd_character_manager.data.classes

import androidx.room.Junction
import androidx.room.Relation

data class DndCharacterWithSkills(
    var dndCharacterId: Int,
    @Relation(
        entity = Ability::class,
        parentColumn = "dndCharacterId",
        entityColumn = "id",
        associateBy = Junction(DndCharacterSkillCrossRef::class)
    )
    val skills: Set<Skill>
)