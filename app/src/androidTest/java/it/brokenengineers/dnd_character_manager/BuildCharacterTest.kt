import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.MainActivity
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterDao
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import kotlinx.coroutines.runBlocking
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

                // Navigate to the character creation screen
            composeTestRule.onNodeWithTag("create_character_button").performClick()

            // Fill in the necessary fields
            composeTestRule.onNodeWithTag("character_name_field").performTextInput("Test Character")
            composeTestRule.onNodeWithTag("option_DWARF").performClick()
            composeTestRule.onNodeWithTag("option_BARBARIAN").performClick()
            // ... add more fields as necessary ...

            // Click the button to create the character
            composeTestRule.onNodeWithTag("create_character_button").performClick()

            var character: DndCharacter? = null

            composeTestRule.runOnIdle {
                // Use Room's testing APIs to query the database and get the character
                character = runBlocking { characterDao.getCharacterByName("Test Character") }
            }
            // Assert that the character was created successfully
            assert(character != null)
            // convert character result to DndCharacter object
            assertEquals("Test Character", character?.name)
            assertEquals("Dwarf", character?.race?.name)
            assertEquals("Barbarian", character?.dndClass?.name)

            // clean up the database
            composeTestRule.runOnIdle {
                runBlocking { characterDao.deleteCharacterById(character!!.id) }
            }
        }
    }
}