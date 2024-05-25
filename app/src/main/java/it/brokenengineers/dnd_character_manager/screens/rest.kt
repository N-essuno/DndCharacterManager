package it.brokenengineers.dnd_character_manager.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.view_model.RestViewModel

@Composable
fun Rest(
    characterId: Int,
    navController: NavHostController,
    viewModel: DndCharacterManagerViewModel
) {
    val context = LocalContext.current
    val shortRestClicked = remember { mutableStateOf(false) }
    val longRestClicked = remember { mutableStateOf(false) }

    // init view model
    val restViewModel = RestViewModel() // TODO review this, maybe remove it

    LaunchedEffect(characterId) {
        viewModel.fetchCharacterById(characterId)
    }
    val char by viewModel.selectedCharacter.collectAsState(initial = null)
    char?.let { character ->
        Scaffold { innerPadding ->

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = "Even the mightiest warriors need to rest sometimes.",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(SmallPadding))
                // summary of character hp and spell slots
                // button to take either a short or long rest

                Row(modifier = Modifier.padding(SmallPadding)) {
                    Column {
                        CharacterCard(
                            dndCharacter = character,
                            navController = navController,
                            onHomePage = false
                        )
                    }
                    Column {
                        SpellSlotsLeft()
                        Spacer(modifier = Modifier.height(MediumPadding))
                        HpRecovery()
                    }
                }


                Row {
                    // Short Rest Button
                    Button(
                        onClick = {
                            shortRestClicked.value = true
                            longRestClicked.value = false
//                        Toast.makeText(context, "Short Rest taken", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.padding(SmallPadding)
                    ) {
                        Text("Short Rest")
                    }
                    // Long Rest Button
                    Button(
                        onClick = {
                            longRestClicked.value = true
                            shortRestClicked.value = false
//                        Toast.makeText(context, "Long Rest taken", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.padding(SmallPadding)
                    ) {
                        Text("Long Rest")
                    }
                }

                if (shortRestClicked.value) {
                    ShortRest(restViewModel, character = character, navController = navController)
                }
                if (longRestClicked.value) {
                    LongRest(restViewModel, character = character, navController = navController)
                }
            }
        }
    }
}

@Composable
fun HpRecovery() {
    // should be retrieved from data model based on character selected
    val hp = 20
    val maxHp = 50
    val currentHp = 30
    Column {
        Text("HP Recovery",
            style = MaterialTheme.typography.titleMedium)
        Text("Current HP: $currentHp / $maxHp",
            style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun LongRest(viewModel: RestViewModel, navController: NavHostController, character: DndCharacter) {
    val spellsKnown by remember { derivedStateOf { viewModel.spellsKnown } }

    Column {
        Text("Choose spells to prepare",
            style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.height(300.dp).padding(SmallPadding)) {
            items(spellsKnown.size) { index ->
                val spell = spellsKnown[index]
                PrepareSpellRow(
                    spell = spell,
                    onAdd = {
                        viewModel.prepareSpell(spell)
                    },
                    onRemove = {
                        viewModel.undoPrepareSpell(spell)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(SmallPadding))
        Button(
            onClick = {
                // TODO save changes to character via viewModel
                navController.navigate("sheet/${character.id}") {
                    popUpTo(navController.graph.findStartDestination().id)

                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Confirm")
        }
    }
}

@Composable
fun ShortRest(viewModel: RestViewModel, navController: NavHostController, character: DndCharacter) {
    val slotsAvailable by remember { derivedStateOf { viewModel.slotsAvailable } }
    Text("Choose magics to recover",
        style = MaterialTheme.typography.titleMedium)
    Text(text = "You have $slotsAvailable slots available",
        style = MaterialTheme.typography.bodyLarge)

    LazyColumn(modifier = Modifier.height(300.dp)) {
        items(9) { index ->
            val level = index + 1
            val timesSelected by remember { derivedStateOf { viewModel.spellsToRecover[index].intValue } }
            RecoverSpellRow(
                level = level,
                timesSelected = timesSelected,
                slotsAvailable = slotsAvailable,
                onAdd = {
                    if(slotsAvailable > 0) {
                        viewModel.recoverSpellSlot(level)
                    }
                },
                onRemove = {
                    if(timesSelected > 0) {
                        viewModel.undoRecoverSpellSlot(level)
                    }
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(SmallPadding))
    Button(onClick = {
        // TODO save changes to character via viewModel
        navController.navigate("sheet/${character.id}") {
            popUpTo(navController.graph.findStartDestination().id)

            launchSingleTop = true
            restoreState = true
        }
    }) {
        Text("Confirm")
    }
}

@Composable
fun RecoverSpellRow(
    level: Int,
    timesSelected: Int,
    slotsAvailable: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {

    Row {
        Text(text = "Level: $level Spells)")
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = onAdd,
            enabled = slotsAvailable > 0
        ){
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                contentDescription = "Add"
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        IconButton(
            onClick = onRemove,
            enabled = timesSelected > 0
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_delete),
                contentDescription = "Remove"
            )
        }
        Text(text = timesSelected.toString())
    }
}

@Composable
fun PrepareSpellRow(
    spell: Spell,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    var selected by remember { mutableStateOf(false) }
    Row {
        Text(text = spell.name, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        if(selected) {
            IconButton(
                onClick = {
                    onRemove()
                    selected = false
                }
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_delete),
                    contentDescription = "Remove"
                )
            }
        } else {
            IconButton(
                onClick = {
                    onAdd()
                    selected = true
                }
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = "Add",
                )
            }
        }

    }
}



@Composable
fun SpellSlotsLeft() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Spell Slots Left",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(SmallPadding))
        Text(
            text = "1st Level: 3",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "2nd Level: 2",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "3rd Level: 1",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


data class Spell(val name: String, val level: Int)