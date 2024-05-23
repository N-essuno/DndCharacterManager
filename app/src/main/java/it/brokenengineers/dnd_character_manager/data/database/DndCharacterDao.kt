package it.brokenengineers.dnd_character_manager.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DndCharacterDao {
    @Query("SELECT * FROM DndCharacter WHERE id = :id LIMIT 1")
    suspend fun getCharacterById(id: Int): DndCharacter
    @Query("SELECT * FROM DndCharacter WHERE name = :name LIMIT 1")
    suspend fun getCharacterByName(name: String): DndCharacter

    @Insert
    suspend fun insert(dndCharacter: DndCharacter)
}