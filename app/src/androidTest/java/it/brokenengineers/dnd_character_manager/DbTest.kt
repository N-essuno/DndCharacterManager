package it.brokenengineers.dnd_character_manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
import it.brokenengineers.dnd_character_manager.repository.DndCharacterManagerRepository
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DbTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var db: DndCharacterManagerDB
    private lateinit var repository: DndCharacterManagerRepository

    @Before
    fun init() {
        // initialize db and repository
        db = DndCharacterManagerDB.getDatabase(context, false)!!
        repository = runBlocking {
            DndCharacterManagerRepository(
                DndCharacterManagerViewModel(db),
                db.characterDao(),
                db.raceDao(),
                db.abilityDao(),
                db.dndClassDao(),
                db.skillDao(),
                db.weaponDao(),
                db.spellDao()
            )
        }
    }

    @After
    @Throws(IOException::class)
    fun testCloseDb() {
        db.close()
    }

    @Test
    fun testGetAllSpells() {
        val spells = runBlocking {
            repository.fetchAllSpellsBlocking()
        }

        assert(spells.isNotEmpty())
    }

    @Test
    fun testGetAllWeapons() {
        val weapons = runBlocking {
            repository.fetchAllWeaponsBlocking()
        }

        assert(weapons.isNotEmpty())
    }

    @Test
    fun testGetAllSkills() {
        val testSkills = SkillEnum.entries.map { it.skill }

        val skills = runBlocking {
            repository.fetchAllSkillsBlocking()
        }

        assert(skills.isNotEmpty())
        assert(skills.size == testSkills.size)
        for (i in testSkills.indices) {
            val skill = skills[i]
            val testSkill = testSkills[i]
            assert(skill.name == testSkill.name)
            assert(skill.ability != null)
            assert(skill.ability!!.name == testSkill.ability!!.name)
        }
    }

    @Test
    fun testGetAllClasses() {
        val testClasses = DndClassEnum.entries.map { it.dndClass }

        val classes = runBlocking {
            repository.fetchAllDndClassesBlocking()
        }

        assert(classes.isNotEmpty())
        assert(classes.size == testClasses.size)

        // check if all classes are the same
        for (i in testClasses.indices) {
            val dndClass = classes[i]
            val testClass = testClasses[i]
            assert(dndClass.name == testClass.name)
            assert(dndClass.primaryAbility != null)
            assert(dndClass.primaryAbility!!.name == testClass.primaryAbility!!.name)
            for (j in testClass.savingThrowProficiencies.indices) {
                assert(dndClass.savingThrowProficiencies[j].name == testClass.savingThrowProficiencies[j].name)
            }
            assert(dndClass.canUseSpells == testClass.canUseSpells)
        }
    }

    @Test
    fun testGetAllAbilities() {
        val testAbilities = AbilityEnum.entries.map { it.ability }

        val abilities = runBlocking {
            repository.fetchAllAbilitiesBlocking()
        }

        assert(abilities.isNotEmpty())
        assert(abilities.size == testAbilities.size)
    }

    @Test
    fun testGetAllRaces() {
        val testRaces = RaceEnum.entries.map { it.race }

        val races = runBlocking {
            repository.fetchAllRacesBlocking()
        }

        assert(races.isNotEmpty())
        assert(races.size == testRaces.size)
    }

    @Test
    fun testGetAllCharacters() {
        val testCharacters = runBlocking { repository.createMockCharacters() }

        // Insert test characters
        runBlocking {
            repository.insertAllCharactersBlocking(testCharacters)
        }
        val characters = runBlocking {
            repository.fetchAllCharactersBlocking()
        }

        assert(characters.isNotEmpty())
        assert(characters.size == testCharacters.size)

        // check if all characters are the same
        for (i in testCharacters.indices) {
            val character = characters[i]
            val testCharacter = testCharacters[i]

            // DndCharacter simple fields assertions
            assert(character.name == testCharacter.name)
            assert(character.level == testCharacter.level)
            assert(character.race != null)
            assert(character.raceId == character.race!!.id)
            assert(character.race!!.name == testCharacter.race!!.name)

            // DndClass assertions
            assert(character.dndClass != null)
            assert(character.dndClassId == character.dndClass!!.id)
            assert(character.dndClass!!.name == testCharacter.dndClass!!.name)
            assert(character.dndClass!!.primaryAbility != null)
            assert(character.dndClass!!.primaryAbility!!.name == testCharacter.dndClass!!.primaryAbility!!.name)
            val characterSavingThrows = character.dndClass!!.savingThrowProficiencies
            val testCharacterSavingThrows = testCharacter.dndClass!!.savingThrowProficiencies
            for (j in testCharacterSavingThrows.indices) {
                assert(characterSavingThrows[j].name == testCharacterSavingThrows[j].name)
            }

            // Skills proficiencies assertions
            val characterSkills = character.skillProficiencies
            val testCharacterSkills = testCharacter.skillProficiencies
            assert(characterSkills != null)
            assert(characterSkills!!.isNotEmpty())
            assert(characterSkills.size == testCharacterSkills!!.size)
            val characterSkillsList = characterSkills.sortedBy { it.name }
            val testCharacterSkillsList = testCharacterSkills.sortedBy { it.name }
            for (j in characterSkillsList.indices) {
                assert(characterSkillsList[j].name == testCharacterSkillsList[j].name)
                assert(characterSkillsList[j].ability != null)
                assert(characterSkillsList[j].ability!!.name == testCharacterSkillsList[j].ability!!.name)
            }

            // Weapon assertions
            if (character.dndClass!!.name == DndClassEnum.BARBARIAN.dndClass.name) {
                assert(character.weapon != null)
                assert(character.weapon!!.name == testCharacter.weapon!!.name)
                assert(character.weapon!!.damage == testCharacter.weapon!!.damage)
                // Can use spells assertion
                assert(!character.dndClass!!.canUseSpells)
            }
            if (character.dndClass!!.name == DndClassEnum.WIZARD.name) {
                assert(character.weapon == null)
            }

            // Spells assertions
            if (character.dndClass!!.name == DndClassEnum.WIZARD.dndClass.name) {
                // Known spells assertions
                assert(character.spellsKnown != null)
                assert(character.spellsKnown!!.isNotEmpty())
                assert(character.spellsKnown!!.size == testCharacter.spellsKnown!!.size)
                val characterSpellsList = character.spellsKnown!!.sortedBy { it.name }
                val testCharacterSpellsList = testCharacter.spellsKnown!!.sortedBy { it.name }
                for (j in characterSpellsList.indices) {
                    assert(characterSpellsList[j].name == testCharacterSpellsList[j].name)
                    assert(characterSpellsList[j].level == testCharacterSpellsList[j].level)
                    assert(characterSpellsList[j].school == testCharacterSpellsList[j].school)
                }

                // Prepared spells assertions
                assert(character.preparedSpells != null)
                assert(character.preparedSpells!!.isNotEmpty())
                assert(character.preparedSpells!!.size == testCharacter.preparedSpells!!.size)
                val characterPreparedSpellsList = character.preparedSpells!!.sortedBy { it.name }
                val testCharacterPreparedSpellsList = testCharacter.preparedSpells!!.sortedBy { it.name }
                for (j in characterPreparedSpellsList.indices) {
                    assert(characterPreparedSpellsList[j].name == testCharacterPreparedSpellsList[j].name)
                    assert(characterPreparedSpellsList[j].level == testCharacterPreparedSpellsList[j].level)
                    assert(characterPreparedSpellsList[j].school == testCharacterPreparedSpellsList[j].school)
                }

                // Can use spells assertion
                assert(character.dndClass!!.canUseSpells)
            }
        }

        runBlocking {
            repository.deleteAllCharacters()
        }
    }
}