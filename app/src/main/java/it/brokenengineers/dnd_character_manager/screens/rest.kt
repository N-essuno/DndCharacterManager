package it.brokenengineers.dnd_character_manager.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    // selected magics contains map magic -> times selected
    val selectedMagics = remember { mutableMapOf<Magic, Int>() }
    val totalSlotsUsed = selectedMagics.values.sum()
    val slotsAvailable = 4

    val magicsKnown = listOf(Magic("Magic1", 1), Magic("Magic2", 2), Magic("Magic3", 3), Magic("Magic4", 4))

    Column {
        Text("Choose magics to recover", style = MaterialTheme.typography.titleMedium)
        Text(text = "You have $slotsAvailable slots available", style = MaterialTheme.typography.bodyLarge)
        magicsKnown.forEach { magic ->
            MagicRow(
                selectedMagics = selectedMagics,
                magic = magic,
                onAdd = { addToChosen(magic, selectedMagics) },
                onRemove = { removeFromChosen(magic) })
        }
    }
}

fun removeFromChosen(magic: Magic) {

}

fun addToChosen(magic: Magic, selectedMagic: MutableMap<Magic, Int>) {
    if(selectedMagic.containsKey(magic)){
        // non null operator safe because we checked if it contains the key
        selectedMagic[magic] = selectedMagic[magic]!! + 1
    } else{
        selectedMagic[magic] = 1
    }
}

@Composable
fun MagicRow(
    selectedMagics: Map<Magic, Int>,
    magic: Magic,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row {
        Text(text = "${magic.name} (Level: ${magic.level})")
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onAdd) {
            Text("Add")
        }
        Spacer(modifier = Modifier.width(8.dp))
        if(selectedMagics.containsKey(magic)){
            Text(text = selectedMagics[magic].toString())
        } else{
            Button(onClick = onRemove) {
                Text("Remove")
            }
            Text(text = "0")
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

data class Magic(val name: String, val level: Int)
