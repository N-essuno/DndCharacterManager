package it.brokenengineers.dnd_character_manager.screens.levelup

import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.enums.MockSpells
import it.brokenengineers.dnd_character_manager.ui.composables.ExpandableCard
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags

@Composable
fun ChooseNewSpells(
    spellsChosen: MutableState<List<Spell>>,
    viewModel: DndCharacterManagerViewModel,
    done: MutableState<Boolean>
) {
    val context = LocalContext.current


    // get all spells
    val allSpells = MockSpells.getAllSpells()
    // get spells already known by character
    val character = viewModel.selectedCharacter.collectAsState().value
    val knownSpells = character?.spellsKnown
    // filter out spells already known by character
    var spellsToDisplay = allSpells.filter { spell ->
        knownSpells?.none { knownSpell -> knownSpell.name == spell.name } ?: true
    }
    // get max spell level learnable by character
    val maxSpellLevel = character?.getMaxSpellLevel() ?: 0
    // filter out spells with level higher than max spell level
    spellsToDisplay = spellsToDisplay.filter { it.level <= maxSpellLevel }

    if(!done.value) {
        Text(stringResource(R.string.choose_spells), style = MaterialTheme.typography.titleMedium)

        spellsToDisplay.forEach { spell ->
            ExpandableCard(
                title = spell.name,
                description = spell.description,
                selected = spellsChosen.value.contains(spell),
                onSelected = {
                    if (spellsChosen.value.contains(spell)) {
                        // clicked on a selected magic, remove from selected
                        spellsChosen.value -= spell
                    } else if (spellsChosen.value.size < 2 && !spellsChosen.value.contains(spell)) {
                        // clicked on an unselected magic, add to selected
                        spellsChosen.value += spell
                    } else {
                        // toast to warn user that they can only select 2 spells
                        Toast.makeText(
                            context,
                            context.getString(R.string.warning_exceeded_2_spells),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                testTag = "${TestTags.CHOOSE_SPELL}_${spell.name}"
            )
        }
    } else {
        Text(
            stringResource(R.string.spells_chosen),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag(TestTags.SPELLS_CHOSEN)
        )
        spellsChosen.value.forEach { spell ->
            Text(text = spell.name, style = MaterialTheme.typography.bodyMedium)
        }
    }

    // confirm button, after clicking the spell list disappears and the spells chosen are shown
    if(!done.value){
        Button(
            onClick = {
                done.value = true
            },
            enabled = spellsChosen.value.size == 2,
            modifier = Modifier.testTag(TestTags.CONFIRM_SPELLS)
        ) {
            Text(stringResource(id = R.string.confirm))
        }
    }
}