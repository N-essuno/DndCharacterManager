package it.brokenengineers.dnd_character_manager

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.test_utils.assertCurrentRouteEqual
import it.brokenengineers.dnd_character_manager.test_utils.assertCurrentRouteWithIdEqual
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SheetScreenTest {
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
                val db = DndCharacterManagerDB.getDatabase(appContext, usingUI = false)
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
    fun test11StartScreen() {
        navController.assertCurrentRouteEqual("home")
        composeTestRule.onNodeWithTag(TestTags.WELCOME_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun test12NavigateSheet() {
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
    }

    @Test
    fun test13CharacterSheetDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_NAME_TEXT).assertTextEquals("Silvano")
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_RACE_TEXT).assertTextEquals("Eladrin")
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_CLASS_TEXT).assertTextEquals("Wizard")
    }

    @Test
    fun test14AddTempHp() {
        val tempHpString = appContext.getString(R.string.temp_hp)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag(TestTags.EDIT_TEMP_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_TEMP_HP_FIELD).performTextInput("5")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ADD_TEMP_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.TEMP_HP_TEXT).assertTextEquals("$tempHpString: 5")
    }

    @Test
    fun test15LoseTempHp() {
        val tempHpString = appContext.getString(R.string.temp_hp)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithTag(TestTags.EDIT_TEMP_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_TEMP_HP_FIELD).performTextInput("5")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_LOSE_TEMP_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.TEMP_HP_TEXT).assertTextEquals("$tempHpString: 0")
    }

    @Test
    fun test16LoseHp(){
        val hpString = appContext.getString(R.string.hp)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithTag(TestTags.EDIT_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_HP_FIELD).performTextInput("7")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_LOSE_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 0/7")
    }

    @Test
    fun test17AddHp(){
        val hpString = appContext.getString(R.string.hp)

        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithTag(TestTags.EDIT_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_HP_FIELD).performTextInput("7")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ADD_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 7/7")
    }

    @Test
    fun test18Hit(){
        val hpString = appContext.getString(R.string.hp)

        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithTag(TestTags.HIT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_HIT_FIELD).performTextInput("7")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ADD_HIT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 0/7")
    }

    @Test
    fun test19NavigateToInventory() {
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")
    }

    @Test
    fun test21IncreaseItem() {
        val totalWeightString = appContext.getString(R.string.total_weight)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")

        composeTestRule.onNodeWithTag(TestTags.ITEM_NAME_TEXT).assertTextEquals("Health potion")
        composeTestRule.onNodeWithTag(TestTags.ITEM_WEIGHT_TEXT).assertTextEquals("1.5")
        composeTestRule.onNodeWithTag(TestTags.ITEM_QUANTITY_TEXT).assertTextEquals("5")

        composeTestRule.onNodeWithTag(TestTags.ITEM_INCREMENT_BUTTON, useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag(TestTags.ITEM_NAME_TEXT).assertTextEquals("Health potion")
        composeTestRule.onNodeWithTag(TestTags.ITEM_WEIGHT_TEXT).assertTextEquals("1.5")
        composeTestRule.onNodeWithTag(TestTags.ITEM_QUANTITY_TEXT).assertTextEquals("6")
        composeTestRule.onNodeWithTag(TestTags.TOTAL_WEIGHT_TEXT)
            .assertTextEquals("$totalWeightString 9.2")
    }

    @Test
    fun test22DecreaseItem() {
        val totalWeightString = appContext.getString(R.string.total_weight)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")

        composeTestRule.onNodeWithTag(TestTags.ITEM_DECREMENT_BUTTON, useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag(TestTags.ITEM_NAME_TEXT).assertTextEquals("Health potion")
        composeTestRule.onNodeWithTag(TestTags.ITEM_WEIGHT_TEXT).assertTextEquals("1.5")
        composeTestRule.onNodeWithTag(TestTags.ITEM_QUANTITY_TEXT).assertTextEquals("5")
        composeTestRule.onNodeWithTag(TestTags.TOTAL_WEIGHT_TEXT)
            .assertTextEquals("$totalWeightString 7.7")
    }

    @Test
    fun test23DeleteItem() {
        val totalWeightString = appContext.getString(R.string.total_weight)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")

        composeTestRule.onNodeWithTag(TestTags.ITEM_NAME_TEXT).assertTextEquals("Health potion")
        composeTestRule.onNodeWithTag(TestTags.ITEM_WEIGHT_TEXT).assertTextEquals("1.5")
        composeTestRule.onNodeWithTag(TestTags.ITEM_QUANTITY_TEXT).assertTextEquals("5")

        composeTestRule.onNodeWithTag(TestTags.ITEM_DELETE_BUTTON, useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("Health potion").assertDoesNotExist()
        composeTestRule.onNodeWithText("1.5").assertDoesNotExist()
        composeTestRule.onNodeWithText("5").assertDoesNotExist()
        composeTestRule.onNodeWithTag(TestTags.TOTAL_WEIGHT_TEXT)
            .assertTextEquals("$totalWeightString 0.2")
    }

    @Test
    fun test24AddNewItem() {
        val totalWeightString = appContext.getString(R.string.total_weight)
        val addItemToInventoryString = appContext.getString(R.string.add_item_to_inventory)

        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")

        composeTestRule.onNodeWithTag(TestTags.ADD_ITEM_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ADD_ITEM_TITLE)
            .assertTextEquals(addItemToInventoryString)
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ITEM_NAME_FIELD).performTextInput("Test item")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ITEM_QUANTITY_FIELD).performTextClearance()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ITEM_QUANTITY_FIELD).performTextInput("4")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ITEM_WEIGHT_FIELD).performTextInput("3.0")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ADD_ITEM_BUTTON).performClick()

        composeTestRule.onNodeWithText("Test item").assertIsDisplayed()
        composeTestRule.onNodeWithText("3.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("4").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.TOTAL_WEIGHT_TEXT)
            .assertTextEquals("$totalWeightString 12.2")
    }

    @Test
    fun test25NavigateToAttack() {
        val spellsTitleString = appContext.getString(R.string.spells_title)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Attack & Spells").performClick()
        navController.assertCurrentRouteWithIdEqual("attack/1")

        composeTestRule.onNodeWithTag(TestTags.SPELLS_TITLE_TEXT).assertTextEquals(spellsTitleString)
        composeTestRule.onNodeWithText("Magic Missile").assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.N_SLOT_TEXT).assertTextEquals("Slots: 1")
    }

    @Test
    fun test26UseSlot(){
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Attack & Spells").performClick()
        navController.assertCurrentRouteWithIdEqual("attack/1")

        composeTestRule.onNodeWithTag(TestTags.N_SLOT_TEXT).assertTextEquals("Slots: 1")
        composeTestRule.onNodeWithTag(TestTags.USE_SLOT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.N_SLOT_TEXT).assertTextEquals("Slots: 0")
    }

    @Test
    fun test27NavigateToLevelUp(){
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag(TestTags.LEVELUP_BUTTON).performClick()
        navController.assertCurrentRouteWithIdEqual("levelup/1")
    }

    @Test
    fun test28NavigateToRest(){
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag(TestTags.REST_BUTTON).performClick()
        navController.assertCurrentRouteWithIdEqual("rest/1")
    }

    @Test
    fun test29NavigateToBuildCharacter(){
        composeTestRule.onNodeWithTag(TestTags.CREATE_CHARACTER_BUTTON).performClick()
        navController.assertCurrentRouteEqual("create_character")
    }
}