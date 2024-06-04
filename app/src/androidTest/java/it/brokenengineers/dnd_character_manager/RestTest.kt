package it.brokenengineers.dnd_character_manager

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.test_utils.assertCurrentRouteWithIdEqual
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RestTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController
    private lateinit var appContext: Context
    private lateinit var viewModel: DndCharacterManagerViewModel

    @Before
    fun setUp() {
        composeTestRule.setContent {
            DndCharacterManagerTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                appContext = LocalContext.current
                // DB not used in this test but needed for ViewModel
                val db = DndCharacterManagerDB.getDatabase(appContext)
                assert(db != null)
                viewModel = DndCharacterManagerViewModel(db!!)
                viewModel.init()
                navController = TestNavHostController(appContext)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                CustomNavigationHost(navController = navController, viewModel = viewModel)
            }
        }
    }

    @Test
    fun testShortRestSilvano() {
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag(TestTags.REST_BUTTON).performClick()
        navController.assertCurrentRouteWithIdEqual("rest/1")
        composeTestRule.onNodeWithTag(TestTags.SHORT_REST_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.ADD_ITEM_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.CONFIRM_REST_BUTTON).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Attack & Spells").performClick()
        navController.assertCurrentRouteWithIdEqual("attack/1")
        composeTestRule.onNodeWithTag(TestTags.N_SLOT_TEXT).assertTextEquals("Slots: 2")
    }

    @Test
    fun testLongRestSilvano() {
        val hpString = appContext.getString(R.string.hp)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        // remove hp
        composeTestRule.onNodeWithTag(TestTags.HIT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_HIT_FIELD).performTextInput("7")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ADD_HIT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 0/7")

        composeTestRule.onNodeWithTag(TestTags.REST_BUTTON).performClick()
        navController.assertCurrentRouteWithIdEqual("rest/1")
        composeTestRule.onNodeWithTag(TestTags.LONG_REST_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.ADD_ITEM_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.CONFIRM_REST_BUTTON).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        // check hp recovered
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 7/7")

        // check spell prepared
        composeTestRule.onNodeWithText("Attack & Spells").performClick()
        navController.assertCurrentRouteWithIdEqual("attack/1")
        composeTestRule.onNodeWithTag(TestTags.PREPARED_SPELL_RADIO_BUTTON).assertIsSelected()
    }

//    private fun waitForDbUpdate(){
//        Thread.sleep(2000)
//        composeTestRule.waitForIdle()
//    }
}