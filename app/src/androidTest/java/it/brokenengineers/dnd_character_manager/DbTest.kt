package it.brokenengineers.dnd_character_manager

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.repository.DndCharacterManagerRepository
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class DbTest {
    private lateinit var db: DndCharacterManagerDB

    @Before
    fun testOpenDb() {
        // get application context
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Get the database
        val db = DndCharacterManagerDB.getDatabase(context)
        assert(db != null)
    }

    @After
    @Throws(IOException::class)
    fun testCloseDb() {
        db.close()
    }

    @Test
    fun testGetAllCharacters() {
        // get application context
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Get the database
        val db = DndCharacterManagerDB.getDatabase(context)

        // get repository for retrieving mock characters
        val repo = db?.let { DndCharacterManagerViewModel(it) }?.let {
            DndCharacterManagerRepository(
                it,
                db.characterDao()
            )
        }

        val testCharacters = runBlocking { repo?.createMockCharacters() }

        if (db != null) {
            // Initialize your DAO here
            val characterDao = db.characterDao()
            // Insert test character
            runBlocking { characterDao.insertAll(testCharacters!!) }
            val characters = runBlocking { characterDao.getAllCharacters() }
            assert(characters.isNotEmpty())
            assert(characters.size == testCharacters?.size)

            // Clean up
            runBlocking { characterDao.deleteCharacters(testCharacters) }
        }
    }
}