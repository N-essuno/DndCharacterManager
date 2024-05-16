package it.brokenengineers.dnd_character_manager.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
        item {ArcaneTradition()}
        item {AbilityScoreImprovement()}
        item {NewAbilities() }
    }
}

@Composable
fun ArcaneTradition() {
    Column(
        modifier = Modifier
            .padding(SmallPadding)
    ){
        Text("Choose an Arcane Tradition", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.padding(SmallPadding))

        // List of arcane traditions
        val traditions = listOf(
            ArcaneTraditionItem("Illusion", "You focus your studies on magic that dazzles the senses, befuddles the mind, and tricks even the wisest folk. Your magic is subtle, but the illusions crafted by your keen mind make the impossible seem real. Some illusionists – including many gnome wizards – are benign tricksters who use their spells to entertain. Others are more sinister masters of deception, using their illusions to frighten and fool others for their personal gain."),
            ArcaneTraditionItem("Necromancy", "The School of Necromancy explores the cosmic forces of life, death, and undeath. As you focus your studies in this tradition, you learn to manipulate the energy that animates all living things. As you progress, you learn to sap the life force from a creature as your magic destroys its body, transforming that vital energy into magical power you can manipulate.\n" +
                    "\n" +
                    "Most people see necromancers as menacing, or even villainous, due to the close association with death. Not all necromancers are evil, but the forces they manipulate are considered taboo by many societies."),
            // Add more traditions here...
        )

        var selectedTradition by remember { mutableStateOf<ArcaneTraditionItem?>(null) }

        // Create an expandable card for each tradition
        traditions.forEach { tradition ->
            ExpandableCard(
                title = tradition.name,
                description = tradition.description,
                selected = selectedTradition == tradition,
                // FIXME: When selected nothing changes, fix
                onSelected = { selectedTradition = tradition }
            )
            Spacer(modifier = Modifier.padding(SmallPadding))
        }
    }
}

data class ArcaneTraditionItem(val name: String, val description: String)

@Composable
fun ExpandableCard(title: String, description: String, selected: Boolean, onSelected: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation =
            if (expanded) CardDefaults.elevatedCardElevation() // Increase elevation when expanded
            else CardDefaults.cardElevation(), // Default elevation when collapsed
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor =
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Expand or collapse",
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }

            // Show the description if the card is expanded
            if (expanded) {
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
                Button(onClick = onSelected) {
                    Text("Choose this tradition")
                }
            }
        }
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
            Text(text = "You have learned to regain some of your magical energy by studying your spell book. Once per day when you finish a short rest, you can choose expended spell slots to recover. The spell slots can have a combined level that is equal to or less than half your wizard level (rounded up), and none of the slots can be 6th level or higher.\n" +
                    "\n" +
                    "For example, if you're a 4th-level wizard, you can recover up to two levels worth of spell slots. You can recover either a 2nd-level spell slot or two 1st-level spell slots.",
                style = MaterialTheme.typography.bodyMedium)
        }
    }

}