package it.brokenengineers.dnd_character_manager.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun Rest() {
    val context = LocalContext.current
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
                        Toast.makeText(context, "Short Rest taken", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(SmallPadding)
                ) {
                    Text("Short Rest")
                }
                // Long Rest Button
                Button(
                    onClick = {
                        Toast.makeText(context, "Long Rest taken", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(SmallPadding)
                ) {
                    Text("Long Rest")
                }
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
