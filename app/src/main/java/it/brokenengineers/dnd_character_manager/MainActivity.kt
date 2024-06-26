package it.brokenengineers.dnd_character_manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.screens.Rest
import it.brokenengineers.dnd_character_manager.screens.build_character.BuildCharacterStart
import it.brokenengineers.dnd_character_manager.screens.build_character.ChooseSpellsScreen
import it.brokenengineers.dnd_character_manager.screens.levelup.LevelUp
import it.brokenengineers.dnd_character_manager.screens.sheet.AttackScreen
import it.brokenengineers.dnd_character_manager.screens.sheet.CharacterSheetScreen
import it.brokenengineers.dnd_character_manager.screens.sheet.InventoryScreen
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XLPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XXLPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // init database here
            val db = DndCharacterManagerDB.getDatabase(this)
            DndCharacterManagerTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                val viewModel: DndCharacterManagerViewModel by viewModels {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            if (modelClass.isAssignableFrom(DndCharacterManagerViewModel::class.java)) {
                                @Suppress("UNCHECKED_CAST")
                                return db?.let { DndCharacterManagerViewModel(it) } as T
                            }
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }
                    }
                }
                viewModel.init()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    CustomNavigationHost(navController, viewModel)
                }
            }
        }
    }
}



@Composable
fun CustomNavigationHost(navController: NavHostController, viewModel: DndCharacterManagerViewModel) {
    NavHost(navController = navController, startDestination = "home_route") {
        navigation(startDestination = "home", route = "home_route") {
            composable("home") {
                // TODO go back to home page when finished testing
                HomePage(navController, viewModel)
//                Rest(1, navController, viewModel)
//                LevelUp(1, navController, viewModel)
//                ChooseSpellsScreen(characterId = 1, viewModel = viewModel, navController = navController)

            }
        }
        navigation(startDestination = "create_character", route = "character_creation") {
            composable("create_character") {
                BuildCharacterStart(navController, viewModel)
            }
            composable("choose_spells/{characterId}", arguments = listOf(navArgument("characterId"){
                type = NavType.IntType
            })) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId")
                characterId?.let {
                    ChooseSpellsScreen(characterId = it, viewModel = viewModel, navController = navController)
                }
            }
        }
        navigation(startDestination = "sheet", route = "character_sheet") {
            composable("sheet/{characterId}", arguments = listOf(navArgument("characterId"){
                type = NavType.IntType
            })) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId")
                characterId?.let {
                    CharacterSheetScreen(
                        characterId = it,
                        navController = navController,
                        viewModel = viewModel
                    )
                }

            }
            composable("inventory/{characterId}", arguments = listOf(navArgument("characterId"){
                type = NavType.IntType
            })) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId")
                characterId?.let {
                    InventoryScreen(
                        characterId = it,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
            composable("attack/{characterId}", arguments = listOf(navArgument("characterId"){
                type = NavType.IntType
            })) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId")
                characterId?.let {
                    AttackScreen(
                        characterId = it,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
        navigation(startDestination = "rest", route = "rest_route") {
            composable("rest/{characterId}", arguments = listOf(navArgument("characterId"){
                type = NavType.IntType
            })) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId")
                characterId?.let {
                    Rest(
                        characterId = it,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
        navigation(startDestination = "levelup", route = "levelup_route") {
            composable("levelup/{characterId}", arguments = listOf(navArgument("characterId"){
                type = NavType.IntType
            })) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId")
                characterId?.let {
                    LevelUp(
                        characterId = it,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: DndCharacterManagerViewModel
){
    val welcomeMessage = stringResource(id = R.string.welcome_message)
    val createCharacterString = stringResource(id = R.string.create_character_button)
    val characters by viewModel.characters.collectAsState(null)
    viewModel.clearSelectedCharacter()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = XXLPadding)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = welcomeMessage,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(top = XLPadding, bottom = MediumPadding)
                    .testTag(TestTags.WELCOME_MESSAGE)
            )
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var testCard = true
                characters?.let {
                    items(characters!!.size) {index ->
                        val character = characters!![index]
                        CharacterCard(
                            dndCharacter = character,
                            navController = navController,
                            onHomePage = true,
                            testCard = testCard
                        )
                        Spacer(modifier = Modifier.padding(SmallPadding))
                        testCard = false
                    }
                }
            }
        }
        Button(
            modifier = Modifier
                .testTag(TestTags.CREATE_CHARACTER_BUTTON)
                .align(Alignment.BottomCenter)
                .padding(MediumPadding),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            onClick = {
                navController.navigate("create_character"){
                    popUpTo(navController.graph.findStartDestination().id)

                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) {
            Text(createCharacterString)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    DndCharacterManagerTheme {
//        HomePage(Modifier, rememberNavController(), viewModel)
//    }
//}