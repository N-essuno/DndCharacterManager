package it.brokenengineers.dnd_character_manager.screens

import android.widget.ScrollView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XXLPadding

@Composable
fun LevelUp(){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = XXLPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        item {CharacterCard()}
        item {Text(text = "Level Up!", style = MaterialTheme.typography.titleLarge) }
        item {AbilityScoreImprovement()}
        item {NewAbilities() }
    }
}

@Composable
private fun AbilityScoreImprovement() {
    Column(
        modifier = Modifier
            .padding(SmallPadding)
    ){
        Text("Choose an ability score to improve by two, or two ability scores to improve by one.",
            style = MaterialTheme.typography.titleMedium)
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


@Composable
private fun NewAbilities() {
    Spacer(modifier = Modifier.padding(SmallPadding))
    Text(
        text = "You have unlocked the following abilities:",
        style = MaterialTheme.typography.titleMedium
    )
    // show a card with the ability
    AbilityCard()
    AbilityCard()
}

@Composable
fun AbilityCard(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SmallPadding)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ){
            Text(text = "Arcane Recovery",
                style = MaterialTheme.typography.titleMedium)
            Text(text = "You have learned to regain some of your magical energy by studying your spellbook. Once per day when you finish a short rest, you can choose expended spell slots to recover. The spell slots can have a combined level that is equal to or less than half your wizard level (rounded up), and none of the slots can be 6th level or higher.\n" +
                    "\n" +
                    "For example, if you're a 4th-level wizard, you can recover up to two levels worth of spell slots. You can recover either a 2nd-level spell slot or two 1st-level spell slots.",
                style = MaterialTheme.typography.bodyMedium)
        }
    }

}