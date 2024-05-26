package it.brokenengineers.dnd_character_manager

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SheetScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController
    private lateinit var appContext: Context
    private lateinit var viewModel: DndCharacterManagerViewModel

    @Before
    fun setUp() {
        composeTestRule.setContent {
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

    @Test
    fun testStartScreen() {
        navController.assertCurrentRouteEqual("home")
        composeTestRule.onNodeWithTag(TestTags.WELCOME_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun testNavigateSheet() {
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
    }

    @Test
    fun testCharacterSheetDisplayed() {
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_NAME_TEXT).assertTextEquals("Silvano")
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_RACE_TEXT).assertTextEquals("Eladrin")
        composeTestRule.onNodeWithTag(TestTags.CHARACTER_CLASS_TEXT).assertTextEquals("Wizard")
    }

    @Test
    fun testAddTempHp() {
        val tempHpString = appContext.getString(R.string.temp_hp)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag(TestTags.EDIT_TEMP_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_TEMP_HP_FIELD).performTextInput("5")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ADD_TEMP_HP).performClick()
        composeTestRule.onNodeWithTag(TestTags.TEMP_HP_TEXT).assertTextEquals("$tempHpString: 5")
    }

    @Test
    fun testLoseTempHp() {
        val tempHpString = appContext.getString(R.string.temp_hp)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag(TestTags.EDIT_TEMP_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_TEMP_HP_FIELD).performTextInput("5")
        composeTestRule.onNodeWithTag(TestTags.DIALOG_ADD_TEMP_HP).performClick()
        composeTestRule.onNodeWithTag(TestTags.TEMP_HP_TEXT).assertTextEquals("$tempHpString: 5")

        composeTestRule.onNodeWithTag(TestTags.EDIT_TEMP_HP_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_TEMP_HP_FIELD).performTextInput("5")
        composeTestRule.onNodeWithText(TestTags.DIALOG_LOSE_TEMP_HP).performClick()
        composeTestRule.onNodeWithTag(TestTags.TEMP_HP_TEXT).assertTextEquals("$tempHpString: 0")
    }

    @Test
    fun testLoseHp(){
        val hpString = appContext.getString(R.string.hp)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithText(TestTags.EDIT_HP_BUTTON).performClick()
        composeTestRule.onNodeWithText(TestTags.DIALOG_HP_FIELD).performTextInput("7")
        composeTestRule.onNodeWithText(TestTags.DIALOG_LOSE_HP).performClick()
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 0/7")
    }

    @Test
    fun testAddHp(){
        val hpString = appContext.getString(R.string.hp)
        val addHpString = appContext.getString(R.string.add_hp)
        val editHpString = appContext.getString(R.string.edit_hp_button)

        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithText(TestTags.EDIT_HP_BUTTON).performClick()
        composeTestRule.onNodeWithText(TestTags.DIALOG_HP_FIELD).performTextInput("7")
        composeTestRule.onNodeWithText(TestTags.DIALOG_LOSE_HP).performClick()
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 0/7")

        composeTestRule.onNodeWithText(editHpString).performClick()
        composeTestRule.onNodeWithText(TestTags.DIALOG_HP_FIELD).performTextInput("7")
        composeTestRule.onNodeWithText(addHpString).performClick()
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 7/7")
    }

    @Test
    fun testHit(){
        val hpString = appContext.getString(R.string.hp)

        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithTag(TestTags.HIT_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTags.DIALOG_HIT_FIELD).performTextInput("7")
        composeTestRule.onNodeWithText(TestTags.DIALOG_ADD_HIT).performClick()
        composeTestRule.onNodeWithTag(TestTags.HP_TEXT).assertTextEquals("$hpString: 0/7")
    }

    @Test
    fun testNavigateToInventory() {
        val totalWeightString = appContext.getString(R.string.total_weight)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")
        composeTestRule.onNodeWithTag(TestTags.TOTAL_WEIGHT_TEXT)
            .assertTextEquals("$totalWeightString 7.7")
    }

    @Test
    fun testIncreaseItem() {
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
    fun testDecreaseItem() {
        val totalWeightString = appContext.getString(R.string.total_weight)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")

        composeTestRule.onNodeWithTag(TestTags.ITEM_NAME_TEXT).assertTextEquals("Health potion")
        composeTestRule.onNodeWithTag(TestTags.ITEM_WEIGHT_TEXT).assertTextEquals("1.5")
        composeTestRule.onNodeWithTag(TestTags.ITEM_QUANTITY_TEXT).assertTextEquals("5")

        composeTestRule.onNodeWithTag(TestTags.ITEM_DECREMENT_BUTTON, useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag(TestTags.ITEM_NAME_TEXT).assertTextEquals("Health potion")
        composeTestRule.onNodeWithTag(TestTags.ITEM_WEIGHT_TEXT).assertTextEquals("1.5")
        composeTestRule.onNodeWithTag(TestTags.ITEM_QUANTITY_TEXT).assertTextEquals("4")
        composeTestRule.onNodeWithTag(TestTags.TOTAL_WEIGHT_TEXT)
            .assertTextEquals("$totalWeightString 6.2")
    }

    @Test
    fun testDeleteItem() {
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
    fun testAddNewItem() {
        val totalWeightString = appContext.getString(R.string.total_weight)
        val addItemToInventoryString = appContext.getString(R.string.add_item_to_inventory)

        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")

        composeTestRule.onNodeWithTag("add_item").performClick()
        composeTestRule.onNodeWithText(addItemToInventoryString).assertIsDisplayed()
        composeTestRule.onNodeWithTag("add_item_name").performTextInput("Test item")
        composeTestRule.onNodeWithTag("add_item_quantity").performTextClearance()
        composeTestRule.onNodeWithTag("add_item_quantity").performTextInput("4")
        composeTestRule.onNodeWithTag("add_item_weight").performTextInput("3.0")
        composeTestRule.onNodeWithTag("add_item_confirm").performClick()

        composeTestRule.onNodeWithText("Test item").assertIsDisplayed()
        composeTestRule.onNodeWithText("3.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("4").assertIsDisplayed()
        composeTestRule.onNodeWithTag("total_weight")
            .assertTextEquals("$totalWeightString 19.7")
    }

    @Test
    fun testNavigateToAttack() {
        val spellsTitleString = appContext.getString(R.string.spells_title)
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Attack & Spells").performClick()
        navController.assertCurrentRouteWithIdEqual("attack/1")

        composeTestRule.onNodeWithTag("spells_title").assertTextEquals(spellsTitleString)
        composeTestRule.onNodeWithText("Magic Missile").assertIsDisplayed()
        composeTestRule.onNodeWithText("Fireball").assertIsDisplayed()
        composeTestRule.onNodeWithTag("n_slot").assertTextEquals("Slots: 6")
    }

    @Test
    fun testUseSlot(){
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Attack & Spells").performClick()
        navController.assertCurrentRouteWithIdEqual("attack/1")

        composeTestRule.onNodeWithTag("n_slot").assertTextEquals("Slots: 6")
        composeTestRule.onNodeWithTag("use_slot").performClick()
        composeTestRule.onNodeWithTag("n_slot").assertTextEquals("Slots: 5")
    }

    @Test
    fun testNavigateToLevelUp(){
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag("levelup_button").performClick()
        navController.assertCurrentRouteWithIdEqual("levelup/1")
    }

    @Test
    fun testNavigateToRest(){
        composeTestRule.onNodeWithTag(TestTags.TEST_CARD).performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithTag("rest_button").performClick()
        navController.assertCurrentRouteWithIdEqual("rest/1")
    }

    @Test
    fun testNavigateToBuildCharacter(){
        composeTestRule.onNodeWithTag("create_character_button").performClick()
        navController.assertCurrentRouteEqual("build_character")
    }

    private fun NavController.assertCurrentRouteEqual(expectedRouteName: String) {
        val currentRoute = currentBackStackEntry?.destination?.route.toString()
        Assert.assertEquals(expectedRouteName, currentRoute)
    }

    private fun NavController.assertCurrentRouteWithIdEqual(expectedRouteName: String) {
        val id = currentBackStackEntry?.arguments?.getInt("characterId")
        val currentRoute = currentBackStackEntry?.destination?.route
            .toString().replace("{characterId}", id.toString())
        Assert.assertEquals(expectedRouteName, currentRoute)
    }

    fun NavController.assertCurrentRouteContains(partialRoute: String) {
        val currentRoute = currentBackStackEntry?.destination?.route.toString()
        Assert.assertTrue(currentRoute.contains(partialRoute))
    }
}