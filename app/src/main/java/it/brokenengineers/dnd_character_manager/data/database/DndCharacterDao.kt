package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterKnownSpellCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterPreparedSpellCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterSkillCrossRef

@Dao
interface DndCharacterDao {
    @Query("SELECT * FROM DndCharacter")
    fun getAllCharacters(): List<DndCharacter>

    @Query("SELECT * FROM DndCharacter WHERE id = :id LIMIT 1")
    fun getCharacterById(id: Int): DndCharacter

    @Query("SELECT * FROM DndCharacter WHERE name = :name LIMIT 1")
    fun getCharacterByName(name: String): DndCharacter

    /** --------------------- INSERT QUERIES --------------------- **/

    @Transaction
    fun insert(dndCharacter: DndCharacter): Int {
        val dndCharacterId = insertDndCharacter(dndCharacter)
        dndCharacter.id = dndCharacterId.toInt()
        insertDndCharacterSkillProficiencies(dndCharacter)
        insertDndCharacterKnownSpells(dndCharacter)
        insertDndCharacterPreparedSpells(dndCharacter)
        return dndCharacterId.toInt()
    }

    @Transaction
    fun insertAll(dndCharacters: List<DndCharacter>) {
        dndCharacters.forEach { dndCharacter ->
            val dndCharacterId = insertDndCharacter(dndCharacter)
            dndCharacter.id = dndCharacterId.toInt()
            insertDndCharacterSkillProficiencies(dndCharacter)
            insertDndCharacterKnownSpells(dndCharacter)
            insertDndCharacterPreparedSpells(dndCharacter)
        }
    }

    // insert queries needed to make relationships between DndClass and Ability list

    @Insert
    fun insertDndCharacter(dndCharacter: DndCharacter): Long

    @Insert
    fun insertAllDndCharacters(dndCharacter: List<DndCharacter>)

    @Insert
    fun insertDndCharacterSkillCrossRefs(crossRefs: List<DndCharacterSkillCrossRef>)

    @Transaction
    fun insertDndCharacterSkillProficiencies(dndCharacter: DndCharacter): Int {
        val dndCharacterId = dndCharacter.id
        val skillIds = dndCharacter.skillProficiencies!!.map { it.id }

        // Create and insert the cross reference entities
        val crossRefs = skillIds.map { skillId ->
            DndCharacterSkillCrossRef(dndCharacterId = dndCharacterId.toInt(), skillId = skillId)
        }

        insertDndCharacterSkillCrossRefs(crossRefs)
        return dndCharacterId.toInt()
    }

    // insert queries needed to make relationships between DndClass and Spell lists

    @Insert
    fun insertDndCharacterKnownSpellCrossRefs(crossRefs: List<DndCharacterKnownSpellCrossRef>)

    @Transaction
    fun insertDndCharacterKnownSpells(dndCharacter: DndCharacter): Int {
        val dndCharacterId = dndCharacter.id
        if (dndCharacter.spellsKnown == null) {
            return dndCharacterId.toInt()
        }
        val spellIds = dndCharacter.spellsKnown!!.map { it.id }

        // Create and insert the cross reference entities
        val crossRefs = spellIds.map { spellId ->
            DndCharacterKnownSpellCrossRef(dndCharacterId = dndCharacterId.toInt(), spellId = spellId)
        }

        insertDndCharacterKnownSpellCrossRefs(crossRefs)
        return dndCharacterId.toInt()
    }

    @Insert
    fun insertDndCharacterPreparedSpellCrossRefs(crossRefs: List<DndCharacterPreparedSpellCrossRef>)

    @Transaction
    fun insertDndCharacterPreparedSpells(dndCharacter: DndCharacter): Int{
        val dndCharacterId = dndCharacter.id
        if (dndCharacter.preparedSpells == null) {
            return dndCharacterId
        }
        val spellIds = dndCharacter.preparedSpells!!.map { it.id }

        // Create and insert the cross reference entities
        val crossRefs = spellIds.map { spellId ->
            DndCharacterPreparedSpellCrossRef(dndCharacterId = dndCharacterId.toInt(), spellId = spellId)
        }

        insertDndCharacterPreparedSpellCrossRefs(crossRefs)
        return dndCharacterId.toInt()
    }


    /** --------------------- END INSERT QUERIES --------------------- **/

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