import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.MainActivity
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterDao
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BuildCharacterTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCharacterCreation() {

        // get application context
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Get the database
        val db = DndCharacterManagerDB.getDatabase(context)
        if (db != null) {
            // Initialize your DAO here
            val characterDao: DndCharacterDao = db.characterDao()

            // Use runOnIdle to wait for the UI to be idle before performing actions
            composeTestRule.runOnIdle {
                // Navigate to the character creation screen
                composeTestRule.onNodeWithTag("create_character_button").performClick()

                // Fill in the necessary fields
                composeTestRule.onNodeWithTag("character_name_field").performTextInput("Test Character")
                composeTestRule.onNodeWithTag("option_Dwarf").performClick()
                composeTestRule.onNodeWithTag("option_Barbarian").performClick()
                // ... add more fields as necessary ...

                // Click the button to create the character
                composeTestRule.onNodeWithTag("create_character_button").performClick()

                // Use Room's testing APIs to query the database and get the character
                val character: DndCharacter = runBlocking { characterDao.getCharacterByName("Test Character") }
                // convert character result to DndCharacter object
                assertEquals("Test Character", character.name)
                assertEquals("Elf", character.race)
                assertEquals("Warrior", character.dndClass)
                // ... add more assertions as necessary ...
            }
        }
    }
}