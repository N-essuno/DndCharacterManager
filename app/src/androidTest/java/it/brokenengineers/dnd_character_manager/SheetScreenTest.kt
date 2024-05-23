package it.brokenengineers.dnd_character_manager

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
    var viewModel: DndCharacterManagerViewModel = DndCharacterManagerViewModel()

    @Before
    fun setUp() {
        viewModel.init()

        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            CustomNavigationHost(navController = navController, viewModel = viewModel)
        }
    }

    @Test
    fun testStartScreen() {
        val welcomeMessageString = "Welcome back adventurer!"
        composeTestRule.onNodeWithText(welcomeMessageString).assertIsDisplayed()
    }

    @Test
    fun testSelectCharacter() {
        // Perform click action on the card
        composeTestRule.onNodeWithTag("test_card").performClick()

        navController.assertCurrentRouteWithIdEqual("sheet/1")
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