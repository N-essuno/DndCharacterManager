package it.brokenengineers.dnd_character_manager.screens.levelup

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.composables.StatIncrease
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
    val levelUpCommitted = remember { mutableStateOf(false) }

    if(!levelUpCommitted.value){
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
                item { Text(text = stringResource(R.string.level_up), style = MaterialTheme.typography.titleLarge) }
                item {
                    StatIncrease(
                    "HP",
                    oldValue = getMaxHpStatic(
                        dndClass = character.dndClass!!,
                        level = character.level,
                        abilityValues = character.abilityValues
                    ),
                    newValue = getMaxHpStatic(
                        dndClass = character.dndClass!!,
                        level = character.level + 1,
                        abilityValues = character.abilityValues
                    ),
                ) }
                item { DynamicLevelUp(
                    character = character,
                    viewModel = viewModel,
                    navController = navController,
                    levelUpCommitted = levelUpCommitted
                )}
                // hp upgrade from previous to current level
    //            viewModel.increaseLevel(character)  // TODO fix, recomposition of character card does not work when level is increased
                // TODO viewModel should update the character after the levelup choices are made
    //            viewModel.increaseHpLevelUp(character)
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
            Log.d("LevelUp", "Loading")
        }
    }
}

