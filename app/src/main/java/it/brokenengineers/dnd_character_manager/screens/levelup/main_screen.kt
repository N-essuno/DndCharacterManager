package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.composables.StatIncrease
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XXLPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel

@Composable
fun LevelUp(
    characterId: Int,
    navController: NavHostController,
    viewModel: DndCharacterManagerViewModel
){
    LaunchedEffect(characterId) {
        viewModel.fetchCharacterById(characterId)
    }
    val char by viewModel.selectedCharacter.collectAsState(initial = null)
    char?.let { character ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = XXLPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                CharacterCard(
                    dndCharacter = character,
                    navController = navController,
                    onHomePage = false
                )
            }
            item { Text(text = "Level Up!", style = MaterialTheme.typography.titleLarge) }
            // hp upgrade from previous to current level
            item {
                StatIncrease(
                "HP",
                currentValue = getMaxHpStatic(
                    dndClass = character.dndClass,
                    level = character.level,
                    abilityValues = character.abilityValues
                ),
                newValue = getMaxHpStatic(
                    dndClass = character.dndClass,
                    level = character.level + 1,
                    abilityValues = character.abilityValues
                ),
            ) }
            item {DynamicLevelUp2(character = character, viewModel = viewModel)}
        }
    }
}

@Composable
fun DynamicLevelUp2(
    character: DndCharacter,
    viewModel: DndCharacterManagerViewModel
) {
    AbilityScoreImprovement(character = character, viewModel = viewModel)
}

