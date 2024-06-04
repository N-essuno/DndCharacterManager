package it.brokenengineers.dnd_character_manager

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.test_utils.assertCurrentRouteWithIdEqual
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LevelUpTest {
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
    fun testLevelUpSilvano() {
        // Select mock character to level up
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        // Click on level up button
        composeTestRule.onNodeWithTag(TestTags.LEVELUP_BUTTON).performClick()
        navController.assertCurrentRouteWithIdEqual("levelup/1")

        // get character params
        val charLevel1 = viewModel.selectedCharacter.value
        val charClass = charLevel1?.dndClass
        // assert character level 1
        assert(charLevel1?.level == 1)
        // Character level is still the old one at this point. After selecting the new level choices
        // and committing, the view model will update the character level along with the
        // other changes.
        val hpLevel1 = getMaxHpStatic(charClass!!, charLevel1.level, charLevel1.abilityValues)
        val hpLevel2 = getMaxHpStatic(charClass, charLevel1.level+1, charLevel1.abilityValues)

        // Assert hp upgrade
        composeTestRule.onNodeWithTag(TestTags.STAT_INCREASE_NAME + "_HP").assertTextEquals("HP Upgrade")
        composeTestRule.onNodeWithTag("HP${TestTags.STAT_INCREASE_OLD_VAL}").assertTextEquals(hpLevel1.toString())
        composeTestRule.onNodeWithTag("HP${TestTags.STAT_INCREASE_NEW_VAL}").assertTextEquals(hpLevel2.toString())

        // Assert Choose Spells
        composeTestRule.onNodeWithTag(TestTags.CHOOSE_SPELL+"_Cure Wounds").performClick()
        composeTestRule.onNodeWithTag(TestTags.CHOOSE_SPELL+"_Cause Fear").performClick()
        composeTestRule.onNodeWithTag(TestTags.CONFIRM_SPELLS).performClick()
        composeTestRule.onNodeWithTag(TestTags.SPELLS_CHOSEN).assertExists()

        // Assert Choose Arcane tradition
        composeTestRule.onNodeWithTag(TestTags.CHOOSE_ARCANE_TRADITION+"_Illusion").performClick()
        composeTestRule.onNodeWithTag(TestTags.CONFIRM_ARCANE_TRADITION).performClick()
        composeTestRule.onNodeWithTag(TestTags.ARCANE_TRADITION_CHOSEN+"_Illusion").assertExists()

        // Assert level up committed
        composeTestRule.onNodeWithTag(TestTags.CONFIRM_LEVELUP).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        // Assert character level 2
        val charLevel2 = viewModel.selectedCharacter.value
        assert(charLevel2?.level == 2)
    }
}