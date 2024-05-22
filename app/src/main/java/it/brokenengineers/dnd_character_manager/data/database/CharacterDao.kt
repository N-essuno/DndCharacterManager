package it.brokenengineers.dnd_character_manager.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it.brokenengineers.dnd_character_manager.data.classes.Character

@Dao
interface CharacterDao {
    @Query("SELECT * FROM Character WHERE id = :id LIMIT 1")
    fun getCharacterById(id: Int): Character

    @Insert
    suspend fun insert(character: Character)
}