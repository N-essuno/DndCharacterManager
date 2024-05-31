package it.brokenengineers.dnd_character_manager.screens.build_character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.screens.levelup.NewSpells
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel

@Composable
fun ChooseSpellsScreen(characterId: Int, viewModel: DndCharacterManagerViewModel, navController: NavHostController) {
    Scaffold {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NewSpells(characterId = characterId, viewModel = viewModel, navController = navController)
        }
    }
}