package it.brokenengineers.dnd_character_manager.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun CharacterCard(hp: Int? = null){
    Card(
        modifier = Modifier
            // to fill the width of the screen
//            .fillMaxWidth()
            .padding(start = MediumPadding, end = MediumPadding),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Character Image"
            )
            Text(
                text = "Character Name",
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding)
            )
            Text(
                text = "Character Class",
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding)
            )
            Text(
                text = "Character Level",
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding)
            )
            if (hp != null) {
                Text(
                    text = "Max Hp: 45 * Hp left: $hp",
                    modifier = Modifier.padding(start = SmallPadding, end = SmallPadding)
                )
            }
            Spacer(modifier = Modifier.padding(SmallPadding))
        }
    }
}