package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import it.brokenengineers.dnd_character_manager.data.classes.Race

@Dao
interface RaceDao {
    @Query("SELECT * FROM Race")
    suspend fun getAllRaces(): List<Race>

    @Query("SELECT * FROM Race WHERE id = :id LIMIT 1")
    suspend fun getRaceById(id: Int): Race

    @Query("SELECT * FROM Race WHERE name = :name LIMIT 1")
    suspend fun getRaceByName(name: String): Race

    @Insert
    suspend fun insert(race: Race)
    @Insert
    suspend fun insertAll(race: List<Race>)

    @Delete
    suspend fun delete(race: Race)


}