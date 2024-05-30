package it.brokenengineers.dnd_character_manager.data.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it.brokenengineers.dnd_character_manager.data.classes.Skill

@Dao
interface SkillDao {
    @Query("SELECT * FROM Skill WHERE id = :skillId LIMIT 1")
    fun getSkill(skillId: Int): Skill

    @Query("SELECT * FROM Skill")
    fun getAllASkills(): List<Skill>

    @Insert
    fun insertAll(skills: List<Skill>)

    @Query("SELECT * FROM Skill " +
            "INNER JOIN DndCharacterSkillCrossRef ON Skill.id = DndCharacterSkillCrossRef.skillId " +
            "WHERE DndCharacterSkillCrossRef.dndCharacterId = :characterId")
    fun getSkillsForCharacter(characterId: Int): List<Skill>

    @Query("SELECT * FROM Skill " +
            "INNER JOIN DndCharacterSkillCrossRef ON Skill.id = DndCharacterSkillCrossRef.skillId " +
            "WHERE DndCharacterSkillCrossRef.dndCharacterId = :classId")
    fun getSkillProficienciesForDndClass(classId: Int): List<Skill>
}