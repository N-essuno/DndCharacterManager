package it.brokenengineers.dnd_character_manager.screens.sheet

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.ui.theme.LargePadding
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XXLPadding

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InventoryScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Scaffold(
            bottomBar = { CharacterSheetNavBar(navController) }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 60.dp)
            ) {
                Spacer(modifier = Modifier.height(LargePadding))
                TitleRow()

                Spacer(modifier = Modifier.height(MediumPadding))
                val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
                val quantities = listOf("1", "2", "3", "4", "5")
                val weights = listOf("1 kg", "2 kg", "3 kg", "4 kg", "5 kg")

                InventoryLegend()
                for (i in items.indices) {
                    InventoryItemRow(items[i], quantities[i], weights[i])
                }
                Spacer(modifier = Modifier.height(MediumPadding))
                WeightRow(weight = "100", maxWeight = "200")

            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 62.dp),
            onClick = { /*TODO*/ }
        ) {
            Text("Add item")
        }
    }
}

@Composable
fun WeightRow(weight: String, maxWeight: String){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(start = SmallPadding),
                text = "Total Weight: $weight",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(end = SmallPadding),
                text = "Max Weight: $maxWeight",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
    Spacer(modifier = Modifier.height(SmallPadding))
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(end = SmallPadding),
                text = "Remaining Weight: ${maxWeight.toInt() - weight.toInt()}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}

@Composable
fun TitleRow(){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Inventory",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun InventoryLegend() {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(
                text = "Item",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column {
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column {
            Text(
                text = "Weight",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column {
            Text(
                text = "Action",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = LargePadding, top = SmallPadding, bottom = SmallPadding, end = 100.dp)
            )
        }
    }

}

@Composable
fun InventoryItemRow(name: String, quantity: String, weight: String) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    )
    {
        Column (){
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = name,
                modifier = Modifier.padding(SmallPadding)
                )
        }
        Column {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = quantity,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column (
        ){
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = weight,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column {
            Row {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Add")
                }
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Remove")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryScreenPreview() {
    val navController = rememberNavController()
    DndCharacterManagerTheme {
        InventoryScreen(navController)
    }
}