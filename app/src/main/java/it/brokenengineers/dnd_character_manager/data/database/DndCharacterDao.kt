package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterSkillCrossRef

@Dao
interface DndCharacterDao {
    @Query("SELECT * FROM DndCharacter")
    fun getAllCharacters(): List<DndCharacter>

    @Query("SELECT * FROM DndCharacter WHERE id = :id LIMIT 1")
    fun getCharacterById(id: Int): DndCharacter

    @Query("SELECT * FROM DndCharacter WHERE name = :name LIMIT 1")
    fun getCharacterByName(name: String): DndCharacter

    // insert queries needed to make relationships between DndClass and Ability list

    @Insert
    fun insertDndCharacter(dndCharacter: DndCharacter): Long

    @Insert
    fun insertAllDndCharacters(dndCharacter: List<DndCharacter>)

    @Insert
    fun insertDndCharacterSkillCrossRefs(crossRefs: List<DndCharacterSkillCrossRef>)

    @Transaction
    fun insertDndCharacterSkillProficiencies(dndCharacter: DndCharacter): Int {
        val dndCharacterId = insertDndCharacter(dndCharacter)
        val skillIds = dndCharacter.skillProficiencies!!.map { it.id }

        // Create and insert the cross reference entities
        val crossRefs = skillIds.map { skillId ->
            DndCharacterSkillCrossRef(dndCharacterId = dndCharacterId.toInt(), skillId = skillId)
        }

        insertDndCharacterSkillCrossRefs(crossRefs)
        return dndCharacterId.toInt()
    }

    @Transaction
    fun insertAll(dndCharacters: List<DndCharacter>) {
        dndCharacters.forEach { dndCharacter ->
            insertDndCharacterSkillProficiencies(dndCharacter)
        }
    }


    // TODO check if delete annotation is more appropriate
    @Delete
    fun deleteCharacter(dndCharacter: DndCharacter)
    @Delete
    fun deleteCharactersByIds(dndCharacters: List<DndCharacter>)
    @Transaction
    fun deleteCharacters(testCharacters: List<DndCharacter>?){
        val ids = testCharacters?.map { it.id }
        if (ids != null) {
            deleteCharactersByIds(testCharacters)
        }
    }
    @Query("DELETE FROM DndCharacter")
    suspend fun deleteAll()
}