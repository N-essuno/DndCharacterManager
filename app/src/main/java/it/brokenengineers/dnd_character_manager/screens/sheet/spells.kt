package it.brokenengineers.dnd_character_manager.screens.sheet

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.ui.theme.MediumVerticalSpacing
import it.brokenengineers.dnd_character_manager.ui.theme.OverBottomNavBar
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SpellsScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = { CharacterSheetNavBar(navController) }
    ) { innerPadding ->
        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = OverBottomNavBar)
        ) {
            val cantrips = listOf("Cantrip 1", "Cantrip 2", "Cantrip 3", "Cantrip 4", "Cantrip 5")
            val lev1Spells = listOf("Spell 1", "Spell 2", "Spell 3", "Spell 4", "Spell 5")
            val lev2Spells = listOf("Spell 1", "Spell 2", "Spell 3", "Spell 4", "Spell 5")
            val lev3Spells = listOf("Spell 1", "Spell 2", "Spell 3", "Spell 4", "Spell 5")
            val lev4Spells = listOf("Spell 1", "Spell 2", "Spell 3", "Spell 4", "Spell 5")
            val lev5Spells = listOf("Spell 1", "Spell 2", "Spell 3", "Spell 4", "Spell 5")
            val lev6Spells = listOf("Spell 1", "Spell 2", "Spell 3", "Spell 4", "Spell 5")
            val lev7Spells = listOf("Spell 1", "Spell 2", "Spell 3", "Spell 4", "Spell 5")
            val lev8Spells = listOf("Spell 1", "Spell 2", "Spell 3", "Spell 4", "Spell 5")

            SpellsTitleRow(title = "Spells")

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ){
                SpellsLevelColumn(level = 0, spells = cantrips)
                SpellsLevelColumn(level = 1, spells = lev1Spells)
                SpellsLevelColumn(level = 2, spells = lev2Spells)
            }


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ){
                SpellsLevelColumn(level = 3, spells = lev3Spells)
                SpellsLevelColumn(level = 4, spells = lev4Spells)
                SpellsLevelColumn(level = 5, spells = lev5Spells)
            }

            Spacer(modifier = Modifier.height(MediumVerticalSpacing))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ){
                SpellsLevelColumn(level = 6, spells = lev6Spells)
                SpellsLevelColumn(level = 7, spells = lev7Spells)
                SpellsLevelColumn(level = 8, spells = lev8Spells)
            }

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ){
                // TODO set depending on character stats
                Text(
                    modifier = Modifier.padding(SmallPadding),
                    text = "DC Saving Throws: 0",
                )
                Text(
                    modifier = Modifier.padding(SmallPadding),
                    text = "Spell Attack Bonus: 0",
                )
            }

        }
    }
}

@Composable
fun SpellsLevelColumn(level: Int, spells: List<String>?){
    val scrollState = rememberScrollState()
    Column {
        Row {
            Text(
                modifier = Modifier.padding(SmallPadding),
                text = "Level $level",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                // TODO implement slot use and use better icon
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    Icons.Default.Build,
                    modifier = Modifier
                        .size(20.dp),
                    contentDescription = "Delete")
            }
        }
        Text(
            // TODO set depending on available slots
            modifier = Modifier.padding(start = SmallPadding, bottom = SmallPadding),
            text = "Slots: 0/0",
            style = MaterialTheme.typography.bodyMedium
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(140.dp)
                .verticalScroll(scrollState)
        ) {
            spells?.let {
                it.forEach { spell ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            // TODO set depending on prepared spells
                            modifier = Modifier.size(32.dp),
                            selected = true,
                            onClick = { }
                        )
                        Text(
                            text = spell,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun SpellsTitleRow(title: String){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(SmallPadding),
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpellsScreenPreview() {
    val navController = rememberNavController()
    DndCharacterManagerTheme {
        SpellsScreen(navController)
    }
}