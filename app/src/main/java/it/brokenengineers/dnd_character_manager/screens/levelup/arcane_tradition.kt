package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.ui.composables.ExpandableCard
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun ChooseArcaneTradition(
    arcaneTraditionChosen: MutableState<ArcaneTraditionItem?>,
    done: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .padding(SmallPadding)
    ) {
        Text(stringResource(R.string.choose_arcane_tradition), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.padding(SmallPadding))

        // List of arcane traditions
        val traditions = listOf(
            ArcaneTraditionItem(
                "Illusion",
                "You focus your studies on magic that dazzles the senses, befuddles the mind, and tricks even the wisest folk. Your magic is subtle, but the illusions crafted by your keen mind make the impossible seem real. Some illusionists – including many gnome wizards – are benign tricksters who use their spells to entertain. Others are more sinister masters of deception, using their illusions to frighten and fool others for their personal gain."
            ),
            ArcaneTraditionItem(
                "Necromancy",
                "The School of Necromancy explores the cosmic forces of life, death, and undeath. As you focus your studies in this tradition, you learn to manipulate the energy that animates all living things. As you progress, you learn to sap the life force from a creature as your magic destroys its body, transforming that vital energy into magical power you can manipulate.\n" +
                        "\n" +
                        "Most people see necromancers as menacing, or even villainous, due to the close association with death. Not all necromancers are evil, but the forces they manipulate are considered taboo by many societies."
            ),
            // Add more traditions here...
        )

        var selectedTradition by remember { mutableStateOf<ArcaneTraditionItem?>(null) }


        if(arcaneTraditionChosen.value == null){
            // Create an expandable card for each tradition
            traditions.forEach { tradition ->
                ExpandableCard(
                    title = tradition.name,
                    description = tradition.description,
                    selected = selectedTradition == tradition,
                    onSelected = { selectedTradition = tradition }
                )
                Spacer(modifier = Modifier.padding(SmallPadding))
            }
        } else {
            Text(
                text = arcaneTraditionChosen.value!!.name,
                style = MaterialTheme.typography.titleMedium
            )
        }

        // Save the selected tradition
        if (selectedTradition != null && !done.value) {
            Button(
                onClick = {
                    arcaneTraditionChosen.value = selectedTradition
                    done.value = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    }
}

data class ArcaneTraditionItem(val name: String, val description: String)