
import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.MainActivity
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.repository.DndCharacterManagerRepository
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags
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
    fun testCharacterCreationBarbarian() {

        // get application context
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Get the database
        val db = DndCharacterManagerDB.getDatabase(context)
        if (db != null) {
            val repository = runBlocking {
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
                character = runBlocking { repository.fetchCharacterByNameBlocking("Test Character") }
            }
            // Assert that the character was created successfully
            assert(character != null)
            // convert character result to DndCharacter object
            assertEquals("Test Character", character?.name)
            assertEquals("Dwarf", character?.race?.name)
            assert(character?.dndClass != null)
            assertEquals("Barbarian", character?.dndClass?.name)
            assert(character?.dndClass!!.savingThrowProficiencies.isNotEmpty())
            assert(character?.dndClass!!.primaryAbility!!.name == "Strength")
            assert(character?.skillProficiencies != null)
            assert(character?.skillProficiencies!!.isNotEmpty())

            assert(character!!.weapon != null)
            assert(character!!.weapon!!.name == "Hammer")
            assert(character!!.weapon!!.damage == "1d12")

            // clean up the database
            composeTestRule.runOnIdle {
                runBlocking { repository.deleteCharacter(character!!) }
            }
        }
    }

    @Test
    fun testCharacterCreationWizard() {

        // get application context
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Get the database
        val db = DndCharacterManagerDB.getDatabase(context)
        if (db != null) {
            val repository = runBlocking {
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

            // Navigate to the character creation screen
            composeTestRule.onNodeWithTag("create_character_button").performClick()

            // Fill in the necessary fields
            composeTestRule.onNodeWithTag("character_name_field").performTextInput("Test Character")
            composeTestRule.onNodeWithTag("option_ELADRIN").performClick()
            composeTestRule.onNodeWithTag("option_WIZARD").performClick()
            // ... add more fields as necessary ...

            // Click the button to create the character
            composeTestRule.onNodeWithTag("create_character_button").performClick()

            composeTestRule.onNodeWithTag("Fireball").performClick()
            composeTestRule.onNodeWithTag("Magic Missile").performClick()
            composeTestRule.onNodeWithTag(TestTags.ADD_SPELLS_CONFIRM_BUTTON).performClick()

            var character: DndCharacter? = null

            composeTestRule.runOnIdle {
                // Use Room's testing APIs to query the database and get the character
                character = runBlocking { repository.fetchCharacterByNameBlocking("Test Character") }
            }

            // Assert that the character was created successfully
            assert(character != null)
            // convert character result to DndCharacter object
            assertEquals("Test Character", character?.name)
            assertEquals("Eladrin", character?.race?.name)
            assert(character?.dndClass != null)
            assertEquals("Wizard", character?.dndClass?.name)
            assert(character?.dndClass!!.savingThrowProficiencies.isNotEmpty())
            assert(character?.dndClass!!.primaryAbility!!.name == "Intelligence")
            assert(character?.skillProficiencies != null)
            assert(character?.skillProficiencies!!.isNotEmpty())

            assert(character!!.weapon == null)

            assert(character!!.spellsKnown != null)
            assert(character!!.spellsKnown!!.isNotEmpty())
            assert(character!!.preparedSpells != null)
            assert(character!!.preparedSpells!!.isEmpty())

            // clean up the database
            composeTestRule.runOnIdle {
                runBlocking { repository.deleteCharacter(character!!) }
            }
        }
    }
}