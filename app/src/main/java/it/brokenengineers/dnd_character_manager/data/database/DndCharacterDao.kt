package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DndCharacterDao {
    @Query("SELECT * FROM DndCharacter")
    suspend fun getAllCharacters(): List<DndCharacter>
    @Query("SELECT * FROM DndCharacter WHERE id = :id LIMIT 1")
    suspend fun getCharacterById(id: Int): DndCharacter
    @Query("SELECT * FROM DndCharacter WHERE name = :name LIMIT 1")
    suspend fun getCharacterByName(name: String): DndCharacter

    @Insert
    suspend fun insert(dndCharacter: DndCharacter)
    @Insert
    suspend fun insertAll(dndCharacters: List<DndCharacter>)

    // TODO check if delete annotation is more appropriate
    @Query("DELETE FROM DndCharacter WHERE id = :id")
    suspend fun deleteCharacterById(id: Int)
    @Query("DELETE FROM DndCharacter WHERE id IN (:ids)")
    suspend fun deleteCharactersByIds(ids: List<Int>)
    suspend fun deleteCharacters(testCharacters: List<DndCharacter>?){
        val ids = testCharacters?.map { it.id }
        if (ids != null) {
            deleteCharactersByIds(ids)
        }
    }
    @Query("DELETE FROM DndCharacter")
    suspend fun deleteAll()
}