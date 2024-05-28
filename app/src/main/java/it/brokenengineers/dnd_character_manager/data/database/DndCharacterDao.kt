package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter

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
    @Delete
    suspend fun deleteCharacter(dndCharacter: DndCharacter)
    @Delete
    suspend fun deleteCharactersByIds(dndCharacters: List<DndCharacter>)
    suspend fun deleteCharacters(testCharacters: List<DndCharacter>?){
        val ids = testCharacters?.map { it.id }
        if (ids != null) {
            deleteCharactersByIds(testCharacters)
        }
    }
    @Query("DELETE FROM DndCharacter")
    suspend fun deleteAll()
}