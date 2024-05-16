package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun AbilityScoreImprovement() {
    Column(
        modifier = Modifier
            .padding(SmallPadding)
    ) {
        Text(
            "Choose an ability score to improve by two, or two ability scores to improve by one.",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.padding(SmallPadding))
        AbilityScore("Strength", 10)
        AbilityScore("Dexterity", 14)
        AbilityScore("Constitution", 12)
        AbilityScore("Intelligence", 16)
        AbilityScore("Wisdom", 10)
        AbilityScore("Charisma", 8)
    }
}

@Composable
fun AbilityScore(name: String, score: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$name: $score", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = { /*TODO: Implement increase score logic*/ }) {
            Text("Increase")
        }
    }
}