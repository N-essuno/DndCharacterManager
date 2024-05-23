package it.brokenengineers.dnd_character_manager.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DndCharacterDao {
    @Query("SELECT * FROM DndCharacter WHERE id = :id LIMIT 1")
    fun getCharacterById(id: Int): DndCharacter

    @Insert
    suspend fun insert(dndCharacter: DndCharacter)
}