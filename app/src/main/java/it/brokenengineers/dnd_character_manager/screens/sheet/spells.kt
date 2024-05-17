package it.brokenengineers.dnd_character_manager.screens.sheet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme

@Composable
fun SpellsScreen(navController: NavController) {
    Text(text = "Spells Screen")
}

@Preview(showBackground = true)
@Composable
fun SpellsScreenPreview() {
    val navController = rememberNavController()
    DndCharacterManagerTheme {
        SpellsScreen(navController)
    }
}