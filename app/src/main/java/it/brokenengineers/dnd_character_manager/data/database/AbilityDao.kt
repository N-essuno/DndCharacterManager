package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it.brokenengineers.dnd_character_manager.data.classes.Ability

@Dao
interface AbilityDao {
    @Query("SELECT * FROM Ability WHERE id = :abilityId LIMIT 1")
    fun getAbility(abilityId: Int): Ability

    @Query("SELECT * FROM Ability")
    fun getAllAbilities(): List<Ability>

    @Insert
    fun insertAll(abilities: List<Ability>)
}