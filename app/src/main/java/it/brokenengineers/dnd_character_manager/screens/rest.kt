package it.brokenengineers.dnd_character_manager.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.composables.StatelessIncrDecrRow
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel

@Composable
fun Rest(
    characterId: Int,
    navController: NavHostController,
    viewModel: DndCharacterManagerViewModel
) {
    val context = LocalContext.current
    val shortRestClicked = remember { mutableStateOf(false) }
    val longRestClicked = remember { mutableStateOf(false) }

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
                        HpRecovery(
                            character = character,
                            shortRestClicked = shortRestClicked.value,
                            longRestClicked = longRestClicked.value
                        )
                        Spacer(modifier = Modifier.height(MediumPadding))
                        SpellSlotsLeft(character = character)
                        Spacer(modifier = Modifier.height(MediumPadding))
                        SpellsPrepared(character = character)
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
                    ShortRest(viewModel, navController = navController)
                }
                if (longRestClicked.value) {
                    LongRest(viewModel, character = character, navController = navController)
                }
            }
        }
    }
}

@Composable
fun HpRecovery(
    character: DndCharacter,
    shortRestClicked: Boolean,
    longRestClicked: Boolean
) {
    val remainingHp = character.remainingHp
    val maxHp = character.getMaxHp()

    Column {
        // heart image
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Heart",
            modifier = Modifier.height(50.dp)
        )
        Text("HP before rest: $remainingHp / $maxHp",
            style = MaterialTheme.typography.bodyLarge)
        if (shortRestClicked){
            Text(text = "HP after short rest: ${character.getHpAfterShortRest()} / $maxHp",
                style = MaterialTheme.typography.bodyLarge)
        } else if (longRestClicked) {
            Text(
                text = "HP after long rest: $maxHp / $maxHp",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ShortRest(
    viewModel: DndCharacterManagerViewModel,
    navController: NavHostController
) {
    val character by viewModel.selectedCharacter.collectAsState(initial = null)
    var selectedSlots: MutableState<List<MutableIntState>>? = null

    character?.let { char ->
        // show hp recovery
        if(char.canRecoverSpells()){
            // list of currently selected slots for each spell level
            selectedSlots = remember {  // list itself is a state so that slots left is updated
                mutableStateOf(List(char.getMaxSpellLevelInPreparedSpells()) {
                    mutableIntStateOf(0)
                })
            }

            SpellRecovery(
                character = char,
                selectedSlots = selectedSlots!!.value
            )
        }
    }

    Spacer(modifier = Modifier.height(SmallPadding))
    Button(onClick = {
        // convert selectedSlots to list of ints
        val selectedSlotsInts = selectedSlots?.value?.map { it.intValue } ?: listOf()

        viewModel.shortRest(selectedSlotsInts)

//        navController.navigate("sheet/${character?.id}") {
//            popUpTo(navController.graph.findStartDestination().id)
//
//            launchSingleTop = true
//            restoreState = true
//        }
    }) {
        Text("Confirm")
    }
}

@Composable
fun SpellRecovery(
    character: DndCharacter,
    selectedSlots: List<MutableIntState>
) {
    // maps spell level to number of recoverable slots
    val slotsRecoverable: Map<Int, Int> = character.getRecoverableSpellSlots()
    // number of slots recoverable for short rest for character
    val maxSlotsRecoverable = character.getNumRecoverableSlotsForShortRest()
    // sum of selected slots, derived from selectedSlots
    val selectedSlotsSum by remember {
        derivedStateOf { selectedSlots.sumOf { it.intValue } }
    }
    // number of slots left to select
    val slotsLeft by remember {
        derivedStateOf { maxSlotsRecoverable - selectedSlotsSum }
    }

    Text("Choose spells to recover",
        style = MaterialTheme.typography.titleMedium)
    Text(text = "You have additional $slotsLeft slots to select",
        style = MaterialTheme.typography.bodyLarge)

    LazyColumn(modifier = Modifier
        .height(300.dp)
        .padding(SmallPadding)) {
        items(slotsRecoverable.size) { level ->
            val levelIndex = level-1
            var timesSelected by remember { mutableIntStateOf(0) }
            if (slotsRecoverable.containsKey(level)
                && slotsRecoverable[level] != 0
                ) { // only show slot levels that you can recover
                val slotsRecoverablePerLevel = slotsRecoverable[level]?:0
                SpellRecoveryRow(
                    level = level,
                    timesSelected = timesSelected,
                    slotsRecoverablePerLevel = slotsRecoverablePerLevel,
                    slotsLeft = slotsLeft,
                    onAdd = {
                        if (selectedSlotsSum < maxSlotsRecoverable) {
                            Log.d("selectedSlotsSum",
                                "slots left: $slotsLeft, selectedSlotsSum: $selectedSlotsSum, maxSlotsRecoverable: $maxSlotsRecoverable"
                            )
                            selectedSlots[levelIndex].intValue += 1
                            timesSelected += 1
                        }
                    },
                    onRemove = {
                        if (timesSelected > 0) {
                            Log.d("selectedSlotsSum",
                                "slots left: $slotsLeft, selectedSlotsSum: $selectedSlotsSum, maxSlotsRecoverable: $maxSlotsRecoverable"
                            )
                            selectedSlots[levelIndex].intValue -= 1
                            timesSelected -= 1
                        }
                    }
                )
            }
        }
    }

    Text("This will consume $selectedSlotsSum out of $maxSlotsRecoverable slots",
        style = MaterialTheme.typography.bodyLarge)
}


@Composable
fun SpellRecoveryRow(
    level: Int,
    timesSelected: Int,
    slotsRecoverablePerLevel: Int,
    slotsLeft: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    StatelessIncrDecrRow(
        label = "Level: $level Spells",
        timesSelected = timesSelected,
        onAdd = onAdd,
        enabledAddCondition = { slotsRecoverablePerLevel > 0 && slotsLeft > 0 },
        onRemove = onRemove,
        enabledRemoveCondition = { timesSelected > 0 }
    )
}


@Composable
fun LongRest(
    viewModel: DndCharacterManagerViewModel,
    navController: NavHostController,
    character: DndCharacter
) {
    // TODO check if magic user, if it is show spells to prepare, otherwise do not
    val spellsKnown = character.spellsKnown?.toList()
    val numPrepareableSpells = character.getNumPrepareableSpells()
    val spellsToPrepare = remember {mutableListOf<Spell>()}
    val numSpellsToPrepare = remember { mutableIntStateOf(0) }

    if(spellsKnown != null){
        // Create a list of states for each spell
        val selectedStates = remember { spellsKnown.map { mutableStateOf(false) } }

        Column(modifier = Modifier.padding(SmallPadding)) {
            Text("Choose spells to prepare",
                style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier
                .height(300.dp)
                .padding(SmallPadding)) {
                items(spellsKnown.size) { index ->
                    val spell = spellsKnown[index]
                    PrepareSpellRow(
                        spell = spell,
                        selected = selectedStates[index],
                        onAdd = {
                            if (numSpellsToPrepare.intValue < numPrepareableSpells) {
                                spellsToPrepare.add(spell)
                                numSpellsToPrepare.intValue += 1
                                Log.d("PrepareSpell", "Added spell ${spell.name}. Total: ${numSpellsToPrepare.intValue}")
                            } else {
                                Log.e("LongRest", "Cannot prepare more spells")
                            }
                        },
                        enabledAddCondition = {
                            numSpellsToPrepare.intValue < numPrepareableSpells
                        },
                        onRemove = {
                            spellsToPrepare.remove(spell)
                            numSpellsToPrepare.intValue -= 1
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(SmallPadding))
        }

        Column{
            Text("You selected ${numSpellsToPrepare.intValue} out of $numPrepareableSpells spells",
                style = MaterialTheme.typography.bodyLarge)
            Button(
                onClick = {
                    // TODO save changes to character via viewModel
                    viewModel.longRest(spellsToPrepare)
//                    navController.navigate("sheet/${character.id}") {
//                        popUpTo(navController.graph.findStartDestination().id)
//
//                        launchSingleTop = true
//                        restoreState = true
//                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Confirm")
            }
        }

    }
}

@Composable
fun PrepareSpellRow(
    spell: Spell,
    selected: MutableState<Boolean>,
    onAdd: () -> Unit,
    enabledAddCondition: () -> Boolean,
    onRemove: () -> Unit
) {
    Row {
        Text(
            text = spell.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = if(selected.value) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.weight(0.1f))
        if(selected.value) {
            // REMOVE BUTTON
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.1f),
                onClick = {
                    onRemove()
                    selected.value = false
                }
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_delete),
                    contentDescription = "Remove"
                )
            }
        } else {
            // ADD BUTTON
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.1f),
                onClick = {
                    onAdd()
                    selected.value = true
                },
                enabled = enabledAddCondition()
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
fun SpellSlotsLeft(character: DndCharacter) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Spell Slots Left",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(SmallPadding))

        val spellSlotsLeft = character.availableSpellSlots

        spellSlotsLeft?.let {
            for(spellSlot in spellSlotsLeft){
                val spellLevel = spellSlot.key
                val slotsLeft = spellSlot.value
                Text(
                    text = "Level $spellLevel: $slotsLeft",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun SpellsPrepared(character: DndCharacter) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Spells Prepared",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(SmallPadding))

        val spellsPrepared = character.preparedSpells

        spellsPrepared?.let {
            for(spell in spellsPrepared){
                Text(
                    text = "- ${spell.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}