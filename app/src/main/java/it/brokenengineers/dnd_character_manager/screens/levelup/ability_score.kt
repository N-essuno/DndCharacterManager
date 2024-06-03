package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.ui.composables.StatIncrease
import it.brokenengineers.dnd_character_manager.ui.composables.StatelessIncrDecrRow
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

/**
 * AbilityScoreImprovement is a composable that allows the user to increase the ability scores of the character.
 * The user can choose to increase one ability score by two points or two ability scores by one point.
 *
 * @param character the character to level up
 * @param levelUpViewModel the view model that handles the level up process
 * @param done a mutable state that indicates if the user has finished selecting the ability scores to improve
 */
@Composable
fun AbilityScoreImprovement(
    character: DndCharacter,
    levelUpViewModel: LevelUpViewModel,
    done: MutableState<Boolean>
) {
    val abilityValueImprovements = levelUpViewModel.abilityValuesImprovements

    fun onIncreaseScore(score: AbilityEnum) {
        if(abilityValueImprovements.values.sumOf { it.intValue } >= 2) {
            // If the user has already selected two ability scores, do nothing
            return
        }
        abilityValueImprovements[score]?.intValue = abilityValueImprovements[score]?.intValue?.plus(1) ?: 0
    }

    fun onDecreaseScore(score: AbilityEnum) {
        if((abilityValueImprovements[score]?.intValue ?: 0) > 0) {
            abilityValueImprovements[score]?.intValue = abilityValueImprovements[score]?.intValue?.minus(1) ?: 0
        }
    }

    if(!done.value){
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
                    timesSelected = abilityValueImprovements[ability]?.intValue ?: 0,
                    onAdd = { onIncreaseScore(ability) },
                    enabledAddCondition = { abilityValueImprovements.values.sumOf { it.intValue } < 2 },
                    onRemove = { onDecreaseScore(ability) },
                    enabledRemoveCondition = { (abilityValueImprovements[ability]?.intValue ?: 0) > 0 }
                )
            }

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    done.value = true
                },
                enabled = abilityValueImprovements.values.sumOf { it.intValue } > 0) {
                Text(stringResource(R.string.confirm))
            }
        }
    } else {
        val improvedScores = abilityValueImprovements.filterValues { it.intValue > 0 }.keys

        Text(stringResource(R.string.ability_scores_increased))
        improvedScores.forEach {
            StatIncrease(
                statName = it.ability.name,
                oldValue = character.getAbilityValue(it),
                newValue = character.getAbilityValue(it) + (if(improvedScores.size == 1) 2 else 1)
            )
        }
    }
}