package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterKnownSpellCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterPreparedSpellCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacterSkillCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.Skill
import it.brokenengineers.dnd_character_manager.data.classes.Spell

// TODO add Update query

@Dao
interface DndCharacterDao {
    /** --------------------- SELECT QUERIES --------------------- **/

    @Query("SELECT * FROM DndCharacter")
    fun getAllCharacters(): List<DndCharacter>

    @Query("SELECT * FROM DndCharacter WHERE id = :id LIMIT 1")
    fun getCharacterById(id: Int): DndCharacter

    @Query("SELECT * FROM DndCharacter WHERE name = :name LIMIT 1")
    fun getCharacterByName(name: String): DndCharacter

    /** --------------------- INSERT QUERIES --------------------- **/

    @Transaction
    fun insert(dndCharacter: DndCharacter): Int {
        // insert the character without the lists of cross referenced entities (spells, skills)
        // we need the character to be inserted to create the cross references
        val dndCharacterId = insertDndCharacter(dndCharacter)
        dndCharacter.id = dndCharacterId.toInt()

        // insert the cross referenced entities
        insertDndCharacterSkillProficiencies(dndCharacter)
        insertDndCharacterKnownSpells(dndCharacter)
        insertDndCharacterPreparedSpells(dndCharacter)
        return dndCharacterId.toInt()
    }

    @Transaction
    fun insertAll(dndCharacters: List<DndCharacter>) {
        dndCharacters.forEach { dndCharacter ->
            insert(dndCharacter)
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
        if (dndCharacter.skillProficiencies == null || dndCharacter.skillProficiencies!!.isEmpty()) {
            return dndCharacterId
        }
        val skillIds = dndCharacter.skillProficiencies!!.map { it.id }

        // Create and insert the cross reference entities
        val crossRefs = skillIds.map { skillId ->
            DndCharacterSkillCrossRef(dndCharacterId = dndCharacterId, skillId = skillId)
        }
        insertDndCharacterSkillCrossRefs(crossRefs)
        return dndCharacterId
    }

    // insert queries needed to make relationships between DndCharacter and Spell lists

    @Insert
    fun insertDndCharacterKnownSpellCrossRefs(crossRefs: List<DndCharacterKnownSpellCrossRef>)

    @Transaction
    fun insertDndCharacterKnownSpells(dndCharacter: DndCharacter): Int {
        val dndCharacterId = dndCharacter.id
        if (dndCharacter.spellsKnown == null || dndCharacter.spellsKnown!!.isEmpty()) {
            return dndCharacterId
        }
        val spellIds = dndCharacter.spellsKnown!!.map { it.id }

        // Create and insert the cross reference entities
        val crossRefs = spellIds.map { spellId ->
            DndCharacterKnownSpellCrossRef(dndCharacterId = dndCharacterId, spellId = spellId)
        }

        insertDndCharacterKnownSpellCrossRefs(crossRefs)
        return dndCharacterId
    }

    @Insert
    fun insertDndCharacterPreparedSpellCrossRefs(crossRefs: List<DndCharacterPreparedSpellCrossRef>)

    @Transaction
    fun insertDndCharacterPreparedSpells(dndCharacter: DndCharacter): Int{
        val dndCharacterId = dndCharacter.id
        if (dndCharacter.preparedSpells == null || dndCharacter.spellsKnown!!.isEmpty()) {
            return dndCharacterId
        }
        val spellIds = dndCharacter.preparedSpells!!.map { it.id }

        // Update the cross reference entities
        val crossRefs = spellIds.map { spellId ->
            DndCharacterPreparedSpellCrossRef(dndCharacterId = dndCharacterId, spellId = spellId)
        }

        insertDndCharacterPreparedSpellCrossRefs(crossRefs)
        return dndCharacterId
    }

    /** --------------------- UPDATE QUERIES --------------------- **/

    @Query("SELECT * FROM Skill " +
            "INNER JOIN DndCharacterSkillCrossRef ON Skill.id = DndCharacterSkillCrossRef.skillId " +
            "WHERE DndCharacterSkillCrossRef.dndCharacterId = :characterId")
    fun getSkillsForCharacter(characterId: Int): List<Skill>

    @Query("SELECT * FROM Spell " +
            "INNER JOIN DndCharacterKnownSpellCrossRef ON Spell.id = DndCharacterKnownSpellCrossRef.spellId " +
            "WHERE DndCharacterKnownSpellCrossRef.dndCharacterId = :characterId")
    fun getKnownSpellsForCharacter(characterId: Int): List<Spell>

    @Query("SELECT * FROM Spell " +
            "INNER JOIN DndCharacterPreparedSpellCrossRef ON Spell.id = DndCharacterPreparedSpellCrossRef.spellId " +
            "WHERE DndCharacterPreparedSpellCrossRef.dndCharacterId = :characterId")
    fun getPreparedSpellsForCharacter(characterId: Int): List<Spell>

    @Transaction
    fun update(dndCharacter: DndCharacter) {
        // update the character
        updateDndCharacter(dndCharacter)
        // update the cross referenced entities
        val currentSkills = getSkillsForCharacter(dndCharacter.id)
        val currentSpellsKnown = getKnownSpellsForCharacter(dndCharacter.id)
        val currentPreparedSpells = getPreparedSpellsForCharacter(dndCharacter.id)

        if (currentSkills.isEmpty()) {
            insertDndCharacterSkillProficiencies(dndCharacter)
        } else {
            updateDndCharacterSkillProficiencies(dndCharacter)
        }

        if (currentSpellsKnown.isEmpty()) {
            insertDndCharacterKnownSpells(dndCharacter)
        } else {
            updateDndCharacterKnownSpells(dndCharacter)
        }

        if (currentPreparedSpells.isEmpty()) {
            insertDndCharacterPreparedSpells(dndCharacter)
        } else {
            updateDndCharacterPreparedSpells(dndCharacter)
        }
    }

    @Update
    fun updateDndCharacter(dndCharacter: DndCharacter)

    @Transaction
    fun updateDndCharacterSkillProficiencies(dndCharacter: DndCharacter) {
        val dndCharacterId = dndCharacter.id
        if (dndCharacter.skillProficiencies == null || dndCharacter.skillProficiencies!!.isEmpty()) {
            return
        }
        val skillIds = dndCharacter.skillProficiencies!!.map { it.id }

        // Update the cross reference entities
        val crossRefs = skillIds.map { skillId ->
            DndCharacterSkillCrossRef(dndCharacterId = dndCharacterId, skillId = skillId)
        }

        updateDndCharacterSkillProficienciesCrossRefs(crossRefs)
    }

    @Update
    fun updateDndCharacterSkillProficienciesCrossRefs(crossRefs: List<DndCharacterSkillCrossRef>)

    @Transaction
    fun updateDndCharacterKnownSpells(dndCharacter: DndCharacter) {
        val dndCharacterId = dndCharacter.id
        if (dndCharacter.spellsKnown == null || dndCharacter.spellsKnown!!.isEmpty()) {
            return
        }
        val spellIds = dndCharacter.spellsKnown!!.map { it.id }

        // Update the cross reference entities
        val crossRefs = spellIds.map { spellId ->
            DndCharacterKnownSpellCrossRef(dndCharacterId = dndCharacterId, spellId = spellId)
        }

        updateDndCharacterKnownSpellCrossRefs(crossRefs)
    }

    @Update
    fun updateDndCharacterKnownSpellCrossRefs(crossRefs: List<DndCharacterKnownSpellCrossRef>)

    @Transaction
    fun updateDndCharacterPreparedSpells(dndCharacter: DndCharacter) {
        val dndCharacterId = dndCharacter.id
        if (dndCharacter.preparedSpells == null || dndCharacter.spellsKnown!!.isEmpty()) {
            return
        }
        val spellIds = dndCharacter.preparedSpells!!.map { it.id }

        // Create and insert the cross reference entities
        val crossRefs = spellIds.map { spellId ->
            DndCharacterPreparedSpellCrossRef(dndCharacterId = dndCharacterId, spellId = spellId)
        }

        updateDndCharacterPreparedSpellCrossRefs(crossRefs)
    }

    @Insert
    fun updateDndCharacterPreparedSpellCrossRefs(crossRefs: List<DndCharacterPreparedSpellCrossRef>)



    /** --------------------- DELETE QUERIES --------------------- **/

    // TODO check if delete annotation is more appropriate
    @Delete
    fun deleteCharacter(dndCharacter: DndCharacter): Int

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