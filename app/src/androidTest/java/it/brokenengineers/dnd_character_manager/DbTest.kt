package it.brokenengineers.dnd_character_manager

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
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
                db.raceDao()
            )
        }
    }

    @After
    @Throws(IOException::class)
    fun testCloseDb() {
        db.close()
    }

    @Test
    fun testGetAllRaces() {
        val testRaces = RaceEnum.entries.map {it.race}

        val races = runBlocking {
            repository.fetchAllRacesBlocking()
        }

        Log.d("DbTest", "testGetAllRaces: $races")

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
        }

        runBlocking {
            repository.deleteAllCharacters()
        }
    }
}