package it.brokenengineers.dnd_character_manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
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
        db = DndCharacterManagerDB.getDatabase(context)!!
        repository = runBlocking {
            DndCharacterManagerRepository(
                DndCharacterManagerViewModel(db),
                db.characterDao(),
                db.raceDao(),
                db.abilityDao(),
                db.dndClassDao()
            )
        }
    }

    @After
    @Throws(IOException::class)
    fun testCloseDb() {
        db.close()
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
            assert(character.name == testCharacter.name)
            assert(character.level == testCharacter.level)
            assert(character.race != null)
            assert(character.raceId == character.race!!.id)
            assert(character.race!!.name == testCharacter.race!!.name)
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
        }

        runBlocking {
            repository.deleteAllCharacters()
        }
    }
}