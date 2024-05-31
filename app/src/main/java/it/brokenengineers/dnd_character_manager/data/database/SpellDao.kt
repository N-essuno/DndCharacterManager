package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it.brokenengineers.dnd_character_manager.data.classes.Spell

@Dao
interface SpellDao {
    @Query("SELECT * FROM Spell WHERE id = :spellId LIMIT 1")
    fun getSpell(spellId: Int): Spell

    @Query("SELECT * FROM Spell WHERE name = :name LIMIT 1")
    fun getSpellByName(name: String): Spell

    @Insert
    fun insertAll(spells: List<Spell>)

    @Query("SELECT * FROM Spell")
    fun getAllSpells(): List<Spell>

    @Query("SELECT * FROM Spell " +
            "INNER JOIN DndCharacterKnownSpellCrossRef ON Spell.id = DndCharacterKnownSpellCrossRef.spellId " +
            "WHERE DndCharacterKnownSpellCrossRef.dndCharacterId = :characterId")
    fun getSpellsForCharacter(characterId: Int): List<Spell>


}