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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
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

    Text("Choose 2 new spells", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.padding(SmallPadding))
    MockSpells().getAllSpells().forEach { magic ->
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
                    Toast.makeText(context, "You can only select 2 spells", Toast.LENGTH_SHORT)
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
        Text("Confirm")
    }
}

class MockSpells{
    private companion object {
        val spellList = listOf(
            Spell(
                name = "Magic Missile",
                description = "A missile of magical energy darts forth from your fingertip and strikes its target, dealing 1d4+1 force damage.",
                level = 1,
                school = "Evocation"
            ),
            Spell(
                name = "Fireball",
                description = "A bright streak flashes from your pointing finger to a point you choose within range and then blossoms with a low roar into an explosion of flame.",
                level = 3,
                school = "Evocation"
            ),
            Spell(
                name = "Lightning Bolt",
                description = "A stroke of lightning forming a line 100 feet long and 5 feet wide blasts out from you in a direction you choose.",
                level = 3,
                school = "Evocation"
            ),
            Spell(
                name = "Cure Wounds",
                description = "A creature you touch regains a number of hit points equal to 1d8 + your spellcasting ability modifier.",
                level = 1,
                school = "Evocation"
            ),
            Spell(
                name = "Cause Fear",
                description = "You awaken the sense of mortality in one creature you can see within range. A construct or an undead is immune to this effect.",
                level = 1,
                school = "Necromancy"
            ),
            Spell(
                name = "Wither and Bloom",
                description = "You invoke both death and life upon a 10-foot-radius sphere centered on a point within range. Each creature of your choice in that area must make a Constitution saving throw, taking 2d6 necrotic damage on a failed save, or half as much damage on a successful one.",
                level = 2,
                school = "Necromancy"
            )
        ) // TODO should be a list retrieved from viewModel, containing spells that the character can learn

    }

    fun getSpellByName(name: String): Spell? {
        return spellList.find { it.name == name }
    }

    fun getAllSpells(): List<Spell> {
        return spellList
    }
}