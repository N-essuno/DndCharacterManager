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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.view_model.RestViewModel

@Composable
fun Rest() {
    val context = LocalContext.current
    val shortRestClicked = remember { mutableStateOf(false) }
    val longRestClicked = remember { mutableStateOf(false) }

    // init view model
    val restViewModel = RestViewModel()

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

            Row(modifier = Modifier.padding(SmallPadding)){
                CharacterCard(20)

                SpellSlotsLeft()
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

            if(shortRestClicked.value){
                ShortRest(restViewModel)
            }
            if(longRestClicked.value){
                LongRest()
            }
        }
    }

}

@Composable
fun LongRest() {
    // should be retrieved from data model based on character selected
    val magicsKnown = listOf(Magic("Magic1", 1), Magic("Magic2", 2), Magic("Magic3", 3), Magic("Magic4", 4))
    TODO("Not yet implemented")
}

@Composable
fun ShortRest(viewModel: RestViewModel) {
    val slotsAvailable by remember { derivedStateOf { viewModel.slotsAvailable } }
    Text("Choose magics to recover",
        style = MaterialTheme.typography.titleMedium)
    Text(text = "You have $slotsAvailable slots available",
        style = MaterialTheme.typography.bodyLarge)

    LazyColumn(modifier = Modifier.height(300.dp)) {
        items(9) { index ->
            val level = index + 1
            val timesSelected by remember { derivedStateOf { viewModel.selectedSpells[index].intValue } }
            SpellRow(
                viewModel = viewModel,
                level = level,
                timesSelected = timesSelected,
                slotsAvailable = slotsAvailable,
                onAdd = {
                    if(slotsAvailable > 0) {
                        viewModel.addSpell(level)
                    }
                },
                onRemove = {
                    if(timesSelected > 0) {
                        viewModel.removeSpell(level)
                    }
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(SmallPadding))
    Button(onClick = { /*TODO*/ }) {
        Text("Confirm")
    }
}

@Composable
fun SpellRow(
    viewModel: RestViewModel,
    level: Int,
    timesSelected: Int,
    slotsAvailable: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {

    Row {
        Text(text = "Level: $level Spells)")
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onAdd,
            enabled = slotsAvailable > 0
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                contentDescription = "Add"
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
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
fun MagicRow(
    selectedCount: Int,
    slotsAvailable: Int,
    magic: Magic,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val timesSelected by remember { derivedStateOf { selectedCount } }

    Row {
        Text(text = "${magic.name} (Level: ${magic.level})")
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onAdd,
            enabled = selectedCount < slotsAvailable
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                contentDescription = "Add"
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onRemove,
            // if the magic is not in the map, the button should be disabled
            enabled = selectedCount > 0
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


data class Magic(val name: String, val level: Int)