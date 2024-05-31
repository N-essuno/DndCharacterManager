package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.Feature
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.getRagesPerDay
import it.brokenengineers.dnd_character_manager.ui.composables.StatIncrease
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel

@Composable
fun DynamicLevelUp(character: DndCharacter, viewModel: DndCharacterManagerViewModel, navController: NavHostController) {
    val barbarian = DndClassEnum.BARBARIAN.dndClass
    val wizard = DndClassEnum.WIZARD.dndClass

    val levelUpEvents: List<LevelUpEvent> = listOf(
        // we handle level up until level 5

        // BARBARIAN
        LevelUpEvent(dndClass = barbarian, newLevel = 2, newFeatures = listOf(Feature.RECKLESS_ATTACK, Feature.DANGER_SENSE)),
        LevelUpEvent(dndClass = barbarian, newLevel = 3, choosePrimalPath = true, increaseNumRages = true),
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

    if (currentLevelUpEvent != null) {
        // Handle the level up event
        currentLevelUpEvent.newFeatures?.let {
            NewFeatures(it)
        }
        currentLevelUpEvent.increaseProficiencyBonus?.let {
            val currentValue = character.getProficiencyBonus()
            val newValue = currentValue + 1
            StatIncrease(statName = "Proficiency Bonus", currentValue = currentValue, newValue = newValue)
        }
        currentLevelUpEvent.increaseAbilityScore?.let {
            AbilityScoreImprovement(
                character = character,
                viewModel = viewModel
            )
        }
        currentLevelUpEvent.increaseNumRages?.let {
            StatIncrease(statName = "Rages per day", currentValue = getRagesPerDay(currentLevel), newValue = getRagesPerDay(currentLevel+1))
        }
        currentLevelUpEvent.chooseNewSpells?.let {
            NewSpells(
                characterId = character.id,
                viewModel = viewModel,
                navController = navController
            )
        }
        currentLevelUpEvent.choosePrimalPath?.let {
//            ChoosePrimalPath(
//                character = character,
//                viewModel = viewModel
        }
        currentLevelUpEvent.chooseArcaneTradition?.let {
            ArcaneTradition(
                character = character,
                viewModel = viewModel,
                navController = navController
            )
        }

    } else {
        Text(stringResource(R.string.level_up_event_not_found))
    }
}

data class LevelUpEvent(
    val dndClass: DndClass,
    val newLevel: Int,
    val newFeatures: List<Feature>? = null,
    val increaseProficiencyBonus: Boolean? = false,
    val increaseAbilityScore: Boolean? = null,
    val increaseNumRages: Boolean? = null,
    val chooseNewSpells: Boolean? = false,
//    val newPrimalPathFeature: Boolean? = false,
    val choosePrimalPath: Boolean? = false,
    val chooseArcaneTradition: Boolean? = false,
)