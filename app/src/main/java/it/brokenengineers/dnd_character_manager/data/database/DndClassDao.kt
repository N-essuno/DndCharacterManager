package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.DndClassAbilityCrossRef

@Dao
interface DndClassDao {
    // Note: the return of the following query won't contain the Ability list
    @Query("SELECT * FROM DndClass WHERE id = :classId LIMIT 1")
    fun getDndClassById(classId: Int): DndClass

    @Query("SELECT * FROM DndClass WHERE name = :name LIMIT 1")
    fun getDndClassByName(name: String): DndClass

    @Query("SELECT * FROM Ability " +
            "INNER JOIN DndClassAbilityCrossRef ON Ability.id = DndClassAbilityCrossRef.abilityId " +
            "WHERE DndClassAbilityCrossRef.classId = :classId")
    fun getSavingThrowsProficienciesForDndClass(classId: Int): List<Ability>

    @Query("SELECT * FROM DndClass")
    fun getAllDndClasses(): List<DndClass>


    // insert queries needed to make relationships between DndClass and Ability list

    @Insert
    fun insertDndClass(dndClass: DndClass): Long

    @Insert
    fun insertAllDndClasses(dndClasses: List<DndClass>)

    @Insert
    fun insertDndClassAbilityCrossRefs(crossRefs: List<DndClassAbilityCrossRef>)

    @Transaction
    fun insertDndClassWithAbilities(dndClass: DndClass) {
        // Insert the DndClass and get its generated ID
        val dndClassId = insertDndClass(dndClass)
        val abilities = dndClass.savingThrowProficiencies.map { it.id }

        // Create and insert the cross reference entities
        val crossRefs = abilities.map { abilityId ->
            DndClassAbilityCrossRef(classId = dndClassId.toInt(), abilityId = abilityId)
        }
        insertDndClassAbilityCrossRefs(crossRefs)
    }

    @Transaction
    fun insertAllDndClassesWithAbilities(dndClasses: List<DndClass>){
        dndClasses.forEach { dndClass ->
            insertDndClassWithAbilities(dndClass)
        }
    }


}