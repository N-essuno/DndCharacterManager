package it.brokenengineers.dnd_character_manager.screens.levelup

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.composables.StatIncrease
import it.brokenengineers.dnd_character_manager.ui.theme.XXLPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel


/**
 * Level up screen.
 * This screen displays the events that happen when a character levels up.
 * Some of the events are automatic, like the increase in HP, while others require the user to make
 * a choice.
 *
 * The following events are handled:
 * - HP increase (always, automatic)
 * - Learning new features (automatic)
 * - Ability score increase (automatic)
 * - Choosing a subclass [Arcane Tradition, Primal Path] (user choice)
 * - Choosing new spells (user choice)
 *
 * @param characterId the id of the character to level up
 * @param navController the navigation controller
 * @param viewModel the view model
 */
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
                    // CHARACTER CARD (summary of the character statistics at the old level)
                    CharacterCard(
                        dndCharacter = character,
                        navController = navController,
                        onHomePage = false
                    )
                }
                item { Text(text = stringResource(R.string.level_up), style = MaterialTheme.typography.titleLarge) }
                item {
                    // HP INCREASE
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
                item {
                    // OTHER LEVEL UP EVENTS
                    DynamicLevelUp(
                    character = character,
                    viewModel = viewModel,
                    navController = navController,
                    levelUpCommitted = levelUpCommitted
                )}
            }
        }
    } else {
        // Loading screen, shown while the level up is being committed
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
            Log.d("LevelUp", "Loading")
        }
    }
}

