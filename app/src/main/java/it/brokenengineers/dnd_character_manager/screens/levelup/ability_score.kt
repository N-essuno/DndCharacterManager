package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.ui.composables.StatIncrease
import it.brokenengineers.dnd_character_manager.ui.composables.StatelessIncrDecrRow
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel

/**
 * AbilityScoreImprovement is a composable that allows the user to increase the ability scores of the character.
 * The user can choose to increase one ability score by two points or two ability scores by one point.
 *
 * @param character the character to level up
 * @param viewModel the view model to handle the character data and store the changes in the database
 */
@Composable
fun AbilityScoreImprovement(character: DndCharacter, viewModel: DndCharacterManagerViewModel) {
    var confirmed by remember {mutableStateOf(false)}

    // Map each ability score to a mutable state, holding the number of times the user has selected it
    val selectedScores = AbilityEnum.entries.associateWith { remember { mutableIntStateOf(0) } }

    fun onIncreaseScore(score: AbilityEnum) {
        if(selectedScores.values.sumOf { it.intValue } >= 2) {
            // If the user has already selected two ability scores, do nothing
            return
        }
        selectedScores[score]?.intValue = selectedScores[score]?.intValue?.plus(1) ?: 0
    }

    fun onDecreaseScore(score: AbilityEnum) {
        if((selectedScores[score]?.intValue ?: 0) > 0) {
            selectedScores[score]?.intValue = selectedScores[score]?.intValue?.minus(1) ?: 0
        }
    }

    if(!confirmed){
        Column(
            modifier = Modifier
                .padding(SmallPadding)
        ) {
            Text(
                text = stringResource(R.string.ability_improvement_hint),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(SmallPadding))
            for (ability in AbilityEnum.entries) {
                StatelessIncrDecrRow(
                    label = "${ability.ability.name}: ${character.getAbilityValue(ability)}",
                    timesSelected = selectedScores[ability]?.intValue ?: 0,
                    onAdd = { onIncreaseScore(ability) },
                    enabledAddCondition = { selectedScores.values.sumOf { it.intValue } < 2 },
                    onRemove = { onDecreaseScore(ability) },
                    enabledRemoveCondition = { (selectedScores[ability]?.intValue ?: 0) > 0 }
                )
            }

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    val abilityScores = selectedScores.filterValues { it.intValue > 0 }.keys
                    if(abilityScores.size == 1) {
                        // If the user has selected one ability score, increase it by two
                        viewModel.increaseAbilityScore(character, abilityScores.first(), 2)
                    } else if(abilityScores.size == 2) {
                        // If the user has selected two ability scores, increase them by one
                        viewModel.increaseAbilityScore(character, abilityScores.first(), 1)
                        viewModel.increaseAbilityScore(character, abilityScores.last(), 1)
                    }
                    confirmed = true
                },
                enabled = selectedScores.values.sumOf { it.intValue } > 0) {
                Text(stringResource(R.string.confirm))
            }
        }
    } else {
        val improvedScores = selectedScores.filterValues { it.intValue > 0 }.keys

        Text(stringResource(R.string.ability_scores_increased))
        improvedScores.forEach {
            StatIncrease(
                statName = it.ability.name,
                currentValue = character.getAbilityValue(it),
                newValue = character.getAbilityValue(it) + (if(improvedScores.size == 1) 2 else 1)
            )
        }
    }
}