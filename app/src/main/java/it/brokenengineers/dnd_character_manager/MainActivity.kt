package it.brokenengineers.dnd_character_manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import it.brokenengineers.dnd_character_manager.screens.sheet.CharacterSheetScreen
import it.brokenengineers.dnd_character_manager.screens.sheet.InventoryScreen
import it.brokenengineers.dnd_character_manager.screens.sheet.SpellsScreen
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XLPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XXLPadding

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DndCharacterManagerTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    CustomNavigationHost(navController)
                }
            }
        }
    }
}

@Composable
fun CustomNavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home_route") {
        navigation(startDestination = "home", route = "home_route") {
            composable("home") { HomePage(Modifier, navController) }
        }
        navigation(startDestination = "sheet", route = "character_sheet") {
            composable("sheet") { CharacterSheetScreen(navController) }
            composable("inventory") { InventoryScreen(navController) }
            composable("spells") { SpellsScreen(navController) }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier, navController: NavHostController){
    val welcomeMessage = stringResource(id = R.string.welcome_message)
    val createCharacterString = stringResource(id = R.string.create_character_button)
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
                modifier = Modifier.padding(top = XLPadding, bottom = MediumPadding)
            )
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(5) {
                    CharacterCard(navController)
                    Spacer(modifier = Modifier.padding(SmallPadding))
                }
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(MediumPadding),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            onClick = { /*TODO*/ }
        ) {
            Text(createCharacterString)
        }
    }

}

@Composable
fun CharacterCard(navController: NavHostController){
    Card(
        modifier = Modifier
            // to fill the width of the screen
//            .fillMaxWidth()
            .clickable {
                // navigate to character sheet
                navController.navigate("character_sheet") {
                    // pop up to the home screen
                    popUpTo("home_route") { inclusive = false }
                }
            }
            .padding(start = MediumPadding, end = MediumPadding),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO retrieve character info
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Character Image"
            )
            Text(
                text = "Character Name",
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding))
            Text(
                text = "Character Class",
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding))
            Text(
                text = "Character Level",
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding))
            Spacer(modifier = Modifier.padding(SmallPadding))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DndCharacterManagerTheme {
        HomePage(Modifier, rememberNavController())
    }
}