package it.brokenengineers.dnd_character_manager.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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

@Composable
fun Rest() {
    val context = LocalContext.current
    val shortRestClicked = remember { mutableStateOf(false) }
    val longRestClicked = remember { mutableStateOf(false) }

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

            CharacterCard(20)

            SpellSlotsLeft()

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
                ShortRest()
            }
            if(longRestClicked.value){
                LongRest()
            }
        }
    }

}

@Composable
fun LongRest() {
    TODO("Not yet implemented")
}

@Composable
fun ShortRest() {
    // TODO instead of choosing spell uses to recover, the user chooses the spell slot levels to recover
    // the spell user has a certain amount of spell slots to recover, he can choose to recover every
    // magic level spell so that the total amount of slots recovered is equal to the amount of slots available

    // should be retrieved from data model based on character selected
    val magicsKnown = listOf(Magic("Magic1", 1), Magic("Magic2", 2), Magic("Magic3", 3), Magic("Magic4", 4))

    // selected magics contains map magic -> times selected
    val selectedMagics = remember { mutableStateMapOf<Magic, MutableState<Int>>() }
    initSelectedMagics(selectedMagics, magicsKnown) // initialize the map with 0 for each magic
    val totalSlotsUsed = selectedMagics.values.sumOf { it.value }
    val slotsAvailable = 4


    Column {
        Text("Choose magics to recover", style = MaterialTheme.typography.titleMedium)
        Text(text = "You have $slotsAvailable slots available", style = MaterialTheme.typography.bodyLarge)
        magicsKnown.forEach { magic ->
            MagicRow(
                selectedCount = selectedMagics[magic]!!.value,
                slotsAvailable = slotsAvailable,
                magic = magic,
                onAdd = {
                    if(totalSlotsUsed < slotsAvailable) {
                        Log.d("ShortRest", "Adding ${magic.name}. Current count: ${selectedMagics[magic]}")
                        selectedMagics[magic]?.let {currentCount ->
                            selectedMagics[magic]?.value = currentCount.value + 1
                        }
                        Log.d("ShortRest", "New count: ${selectedMagics[magic]}")
                    }
                },
                onRemove = {
                    if(selectedMagics[magic]!!.value > 0) {
                        selectedMagics[magic]?.let {currentCount ->
                            selectedMagics[magic]?.value = currentCount.value - 1
                        }
                    }
                }
            )
        }
    }
}

fun initSelectedMagics(
    selectedMagics: MutableMap<Magic, MutableState<Int>>,
    magicsKnown: List<Magic>
){
    magicsKnown.forEach { magic ->
        selectedMagics[magic] = mutableIntStateOf(0)
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
