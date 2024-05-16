package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun NewFeatures() {
    Spacer(modifier = Modifier.padding(SmallPadding))
    Text(
        text = "You have unlocked the following abilities:",
        style = MaterialTheme.typography.titleMedium
    )
    // show a card with the ability
    FeatureCard()
    FeatureCard()
}

@Composable
fun FeatureCard(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SmallPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            Text(
                text = "Arcane Recovery",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "You have learned to regain some of your magical energy by studying your spell book. Once per day when you finish a short rest, you can choose expended spell slots to recover. The spell slots can have a combined level that is equal to or less than half your wizard level (rounded up), and none of the slots can be 6th level or higher.\n" +
                        "\n" +
                        "For example, if you're a 4th-level wizard, you can recover up to two levels worth of spell slots. You can recover either a 2nd-level spell slot or two 1st-level spell slots.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}