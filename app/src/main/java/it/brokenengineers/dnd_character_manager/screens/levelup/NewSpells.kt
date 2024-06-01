package it.brokenengineers.dnd_character_manager.screens.levelup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.enums.MockSpells
import it.brokenengineers.dnd_character_manager.ui.composables.ExpandableCard
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel

@Composable
fun NewSpells(
    characterId: Int,
    viewModel: DndCharacterManagerViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    var selectedSpells by remember { mutableStateOf<List<Spell>>(emptyList()) }

    Text(stringResource(R.string.choose_spells), style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.padding(SmallPadding))
    MockSpells.getAllSpells().forEach { magic ->
        ExpandableCard(
            title = magic.name,
            description = magic.description,
            selected = selectedSpells.contains(magic),
            onSelected = {
                selectedSpells = if (selectedSpells.contains(magic)) {
                    // clicked on a selected magic, remove from selected
                    selectedSpells - magic
                } else if (selectedSpells.size < 2 && !selectedSpells.contains(magic)) {
                    // clicked on an unselected magic, add to selected
                    selectedSpells + magic
                } else {
                    // toast to warn user that they can only select 2 spells
                    Toast.makeText(context,
                        context.getString(R.string.warning_exceeded_2_spells), Toast.LENGTH_SHORT)
                        .show()
                    selectedSpells
                }
                Log.d("New Spells", selectedSpells.toString())
            }
        )
        Spacer(modifier = Modifier.padding(SmallPadding))

    }
    // CONFIRM BUTTON
    Button(
        onClick = {
            // save to view model
            viewModel.saveNewSpells(selectedSpells)
            // redirect to character sheet
            navController.navigate("sheet/${characterId}") {
                popUpTo(navController.graph.findStartDestination().id)

                launchSingleTop = true
                restoreState = true
            }
        },
        enabled = selectedSpells.size == 2
    ) {
        Text(stringResource(id = R.string.confirm))
    }
}