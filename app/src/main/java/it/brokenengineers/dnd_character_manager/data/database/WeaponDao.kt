package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it.brokenengineers.dnd_character_manager.data.classes.Weapon

@Dao
interface WeaponDao {
    @Query("SELECT * FROM Weapon WHERE id = :weaponId LIMIT 1")
    fun getWeapon(weaponId: Int): Weapon

    @Query("SELECT * FROM Weapon WHERE name = :name LIMIT 1")
    fun getWeaponByName(name: String): Weapon

    @Insert
    fun insertAll(weapons: List<Weapon>)

    @Query("SELECT * FROM Weapon")
    fun getAllWeapons(): List<Weapon>
}