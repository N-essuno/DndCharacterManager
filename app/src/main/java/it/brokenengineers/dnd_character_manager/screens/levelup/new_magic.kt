package it.brokenengineers.dnd_character_manager.screens.levelup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import it.brokenengineers.dnd_character_manager.ui.composables.ExpandableCard
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun NewMagic() {
    val context = LocalContext.current
    val magicList = listOf(
        MagicItem(
            "Magic Missile",
            "A missile of magical energy darts forth from your fingertip and strikes its target, dealing 1d4+1 force damage."
        ),
        MagicItem(
            "Fireball",
            "A bright streak flashes from your pointing finger to a point you choose within range and then blossoms with a low roar into an explosion of flame."
        ),
        MagicItem(
            "Lightning Bolt",
            "A stroke of lightning forming a line 100 feet long and 5 feet wide blasts out from you in a direction you choose."
        ),
        MagicItem(
            "Cure Wounds",
            "A creature you touch regains a number of hit points equal to 1d8 + your spellcasting ability modifier."
        ),
    )
    var selectedMagics by remember { mutableStateOf<List<MagicItem>>(emptyList()) }

    Text("Choose 2 new magics", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.padding(SmallPadding))
    magicList.forEach { magic ->
        ExpandableCard(
            title = magic.name,
            description = magic.description,
            selected = selectedMagics.contains(magic),
            onSelected = {
                selectedMagics = if (selectedMagics.contains(magic)) {
                    // clicked on a selected magic, remove from selected
                    selectedMagics - magic
                } else if (selectedMagics.size < 2 && !selectedMagics.contains(magic)) {
                    // clicked on an unselected magic, add to selected
                    selectedMagics + magic
                } else {
                    // toast to warn user that they can only select 2 magics
                    Toast.makeText(context, "You can only select 2 magics", Toast.LENGTH_SHORT)
                        .show()
                    selectedMagics
                }
                Log.d("NewMagic", selectedMagics.toString())
            }
        )
        Spacer(modifier = Modifier.padding(SmallPadding))
    }
}

data class MagicItem(val name: String, val description: String)