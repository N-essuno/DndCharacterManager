package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.Feature
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.getRagesPerDay
import it.brokenengineers.dnd_character_manager.ui.composables.StatIncrease
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags

@Composable
fun DynamicLevelUp(
    character: DndCharacter,
    viewModel: DndCharacterManagerViewModel,
    navController: NavHostController,
    levelUpCommitted: MutableState<Boolean>
) {
    val levelUpViewModel = remember { LevelUpViewModel() }

    val barbarian = DndClassEnum.BARBARIAN.dndClass
    val wizard = DndClassEnum.WIZARD.dndClass

    val levelUpEvents: List<LevelUpEvent> = listOf(
        // we handle level up until level 5

        // BARBARIAN
        LevelUpEvent(dndClass = barbarian, newLevel = 2, newFeatures = listOf(Feature.RECKLESS_ATTACK, Feature.DANGER_SENSE)),
        LevelUpEvent(dndClass = barbarian, newLevel = 3, increaseNumRages = true),
        LevelUpEvent(dndClass = barbarian, newLevel = 4, increaseAbilityScore = true),
        LevelUpEvent(dndClass = barbarian, newLevel = 5, increaseProficiencyBonus = true, newFeatures = listOf(Feature.EXTRA_ATTACK, Feature.FAST_MOVEMENT)),

        // WIZARD
        LevelUpEvent(dndClass = wizard, newLevel = 2, chooseArcaneTradition = true, chooseNewSpells = true),
        LevelUpEvent(dndClass = wizard, newLevel = 3, newFeatures = listOf(Feature.CANTRIP_FORMULAS), chooseNewSpells = true),
        LevelUpEvent(dndClass = wizard, newLevel = 4, increaseAbilityScore = true, chooseNewSpells = true),
        LevelUpEvent(dndClass = wizard, newLevel = 5, increaseProficiencyBonus = true, chooseNewSpells = true),
    )

    val characterClass = character.dndClass
    val currentLevel = character.level
    val currentLevelUpEvent = levelUpEvents.find { it.dndClass == characterClass && it.newLevel == currentLevel+1 }

    // create empty mutable list
    // todo rafactor inside levelupview model
    val selectedSpells = remember { mutableStateOf<List<Spell>>(emptyList()) }


    val optionalSelectionsDone by remember {
        derivedStateOf {
            levelUpViewModel.arcaneSelectionDone.value &&
                    levelUpViewModel.spellSelectionDone.value &&
                    levelUpViewModel.abilityValuesImprovementsDone.value
        }
    }

    if (currentLevelUpEvent != null) {
        // Handle the level up event
        currentLevelUpEvent.newFeatures?.let {
            NewFeatures(it)
        }
        if (currentLevelUpEvent.increaseProficiencyBonus) {
            val currentValue = character.getProficiencyBonus()
            val newValue = currentValue + 1
            StatIncrease(statName = "Proficiency Bonus", oldValue = currentValue, newValue = newValue)
        }
        if (currentLevelUpEvent.increaseAbilityScore) {
            AbilityScoreImprovement(
                character = character,
                levelUpViewModel = levelUpViewModel,
                done = levelUpViewModel.abilityValuesImprovementsDone
            )
        } else {
            // If the user does not have to increase ability scores, set the done state to true
            levelUpViewModel.abilityValuesImprovementsDone.value = true
        }
        if (currentLevelUpEvent.increaseNumRages) {
            val currentValue = getRagesPerDay(currentLevel)
            val newValue = getRagesPerDay(currentLevel+1)
            StatIncrease(statName = "Rages per day", oldValue = currentValue, newValue = newValue)
        }
        if(currentLevelUpEvent.chooseNewSpells) {
            ChooseNewSpells(
                spellsChosen = selectedSpells,
                viewModel = viewModel,
                done = levelUpViewModel.spellSelectionDone
            )
        } else {
            levelUpViewModel.spellSelectionDone.value = true
        }
        if(currentLevelUpEvent.chooseArcaneTradition) {
            val arcaneTraditionChosen = remember { mutableStateOf<ArcaneTraditionItem?>(null) }
            ChooseArcaneTradition(
                arcaneTraditionChosen = arcaneTraditionChosen,
                done = levelUpViewModel.arcaneSelectionDone
            )
        } else {
            levelUpViewModel.arcaneSelectionDone.value = true
        }

        Button(
            onClick = {
                levelUpCommitted.value = true
                // SAVE SPELLS
                viewModel.saveNewSpells(selectedSpells.value)

                // INCREASE ABILITY SCORES
                // Get the pair ability int (scores) for abilities (STRENGTH, WISDOM, ..) selected by user
                val abilityScores = levelUpViewModel.abilityValuesImprovements.filterValues { it.intValue > 0 }
                // convert to map of Ability to Int
                val abilityIntMap: Map<Ability, Int> = abilityScores.mapKeys { entry ->
                    entry.key.ability
                }.mapValues { entry ->
                    entry.value.intValue
                }

                // LEVEL UP
                viewModel.levelUp(abilityIntMap)

                // redirect to character sheet
                navController.navigate("sheet/${character.id}") {
                    popUpTo(navController.graph.findStartDestination().id)

                    launchSingleTop = true
                    restoreState = true
                }
            },
            enabled = optionalSelectionsDone,
            modifier = Modifier.testTag(TestTags.CONFIRM_LEVELUP)
        ) {
            Text(stringResource(id = R.string.confirm))
        }

    } else {
        Text(stringResource(R.string.error_level_up_not_found))
    }
}

class LevelUpViewModel : ViewModel() {
    val arcaneSelectionDone = mutableStateOf(false)
    val spellSelectionDone = mutableStateOf(false)
    val abilityValuesImprovementsDone = mutableStateOf(false)

    val abilityValuesImprovements = AbilityEnum.entries.associateWith { mutableIntStateOf(0) }
}

data class LevelUpEvent(
    val dndClass: DndClass,
    val newLevel: Int,
    val newFeatures: List<Feature>? = null,
    val increaseProficiencyBonus: Boolean = false,
    val increaseAbilityScore: Boolean = false,
    val increaseNumRages: Boolean = false,
    val chooseNewSpells: Boolean = false,
    val chooseArcaneTradition: Boolean = false,
)