package it.brokenengineers.dnd_character_manager

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SheetScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var navController: TestNavHostController
    lateinit var appContext: Context
    var viewModel: DndCharacterManagerViewModel = DndCharacterManagerViewModel()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            viewModel.init()
            navController = TestNavHostController(LocalContext.current)
            appContext = LocalContext.current
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            CustomNavigationHost(navController = navController, viewModel = viewModel)
        }
    }

    @Test
    fun testStartScreen() {
        val welcomeMessageString = appContext.getString(R.string.welcome_message)
        navController.assertCurrentRouteEqual("home")
        composeTestRule.onNodeWithText(welcomeMessageString).assertIsDisplayed()
    }

    @Test
    fun testSelectCharacter() {
        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
    }

    @Test
    fun testCharacterSheetDisplayed() {
        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Silvano").assertIsDisplayed()
        composeTestRule.onNodeWithText("Eladrin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wizard").assertIsDisplayed()
    }

    @Test
    fun testAddTempHp() {
        val tempHpString = appContext.getString(R.string.temp_hp)
        val addTempHpString = appContext.getString(R.string.add_temp_hp)
        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Edit temp HP").performClick()
        composeTestRule.onNodeWithText(tempHpString).performTextInput("5")
        composeTestRule.onNodeWithText(addTempHpString).performClick()
        composeTestRule.onNodeWithText("Temp HP: 5").assertIsDisplayed()
    }

    @Test
    fun testLoseTempHp() {
        val tempHpString = appContext.getString(R.string.temp_hp)
        val addTempHpString = appContext.getString(R.string.add_temp_hp)
        val loseTempHpString = appContext.getString(R.string.lose_temp_hp)
        val editTempHpString = appContext.getString(R.string.edit_temp_hp_button)
        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText(editTempHpString).performClick()
        composeTestRule.onNodeWithText(tempHpString).performTextInput("5")
        composeTestRule.onNodeWithText(addTempHpString).performClick()
        composeTestRule.onNodeWithText("Temp HP: 5").assertIsDisplayed()

        composeTestRule.onNodeWithText(editTempHpString).performClick()
        composeTestRule.onNodeWithText(tempHpString).performTextInput("5")
        composeTestRule.onNodeWithText(loseTempHpString).performClick()
        composeTestRule.onNodeWithText("Temp HP: 0").assertIsDisplayed()
    }

    @Test
    fun testLoseHp(){
        val hpString = appContext.getString(R.string.hp)
        val loseHpString = appContext.getString(R.string.lose_hp)
        val editHpString = appContext.getString(R.string.edit_hp_button)

        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithText(editHpString).performClick()
        composeTestRule.onNodeWithText(hpString).performTextInput("7")
        composeTestRule.onNodeWithText(loseHpString).performClick()
        composeTestRule.onNodeWithText("HP: 0/7").assertIsDisplayed()
    }

    @Test
    fun testAddHp(){
        val hpString = appContext.getString(R.string.hp)
        val addHpString = appContext.getString(R.string.add_hp)
        val loseHpString = appContext.getString(R.string.lose_hp)
        val editHpString = appContext.getString(R.string.edit_hp_button)

        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithText(editHpString).performClick()
        composeTestRule.onNodeWithText(hpString).performTextInput("7")
        composeTestRule.onNodeWithText(loseHpString).performClick()
        composeTestRule.onNodeWithText("HP: 0/7").assertIsDisplayed()

        composeTestRule.onNodeWithText(editHpString).performClick()
        composeTestRule.onNodeWithText(hpString).performTextInput("7")
        composeTestRule.onNodeWithText(addHpString).performClick()
        composeTestRule.onNodeWithText("HP: 7/7").assertIsDisplayed()
    }

    @Test
    fun testHit(){
        val hitButtonString = appContext.getString(R.string.hit_button)
        val addHitString = appContext.getString(R.string.add_hit)
        val hitManagementString = appContext.getString(R.string.hit_management)
        val hpString = appContext.getString(R.string.hp)

        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")

        composeTestRule.onNodeWithText(hitButtonString).performClick()
        composeTestRule.onNodeWithText(hitManagementString).assertIsDisplayed()

        composeTestRule.onNodeWithText(hpString).performTextInput("7")
        composeTestRule.onNodeWithText(addHitString).performClick()
        composeTestRule.onNodeWithText("HP: 0/7").assertIsDisplayed()
    }

    @Test
    fun testNavigateToInventory() {
        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")
    }

    @Test
    fun testIncreaseItem() {
        composeTestRule.onNodeWithTag("test_card").performClick()
        navController.assertCurrentRouteWithIdEqual("sheet/1")
        composeTestRule.onNodeWithText("Inventory").performClick()
        navController.assertCurrentRouteWithIdEqual("inventory/1")

        composeTestRule.onNodeWithTag("item_name").assertTextEquals("Health potion")
        composeTestRule.onNodeWithTag("weight").assertTextEquals("1.5")
        composeTestRule.onNodeWithTag("quantity").assertTextEquals("5")

        composeTestRule.onNodeWithTag("add_item", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithTag("item_name").assertTextEquals("Health potion")
        composeTestRule.onNodeWithTag("weight").assertTextEquals("1.5")
        composeTestRule.onNodeWithTag("quantity").assertTextEquals("6")
    }

    fun NavController.assertCurrentRouteEqual(expectedRouteName: String) {
        val currentRoute = currentBackStackEntry?.destination?.route.toString()
        Assert.assertEquals(expectedRouteName, currentRoute)
    }

    fun NavController.assertCurrentRouteWithIdEqual(expectedRouteName: String) {
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