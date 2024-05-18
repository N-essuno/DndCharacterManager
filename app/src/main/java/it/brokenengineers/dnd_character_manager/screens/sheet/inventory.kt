package it.brokenengineers.dnd_character_manager.screens.sheet

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.ui.theme.LargeVerticalSpacing
import it.brokenengineers.dnd_character_manager.ui.theme.MediumVerticalSpacing
import it.brokenengineers.dnd_character_manager.ui.theme.OverBottomNavBar
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallVerticalSpacing

// TODO add gold visualization

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InventoryScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = { CharacterSheetNavBar(navController) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = OverBottomNavBar)
        ) {
            Spacer(modifier = Modifier.height(LargeVerticalSpacing))
            TitleRow("Inventory")

            Spacer(modifier = Modifier.height(MediumVerticalSpacing))
            val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
            val quantities = listOf("1", "2", "3", "4", "5")
            val weights = listOf("1 kg", "2 kg", "3 kg", "4 kg", "5 kg")

            InventoryLegend()
            for (i in items.indices) {
                InventoryItemRow(items[i], quantities[i], weights[i])
            }
            Spacer(modifier = Modifier.height(MediumVerticalSpacing))
            WeightRow(weight = "100", maxWeight = "200")

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
    Spacer(modifier = Modifier.height(SmallVerticalSpacing))
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
fun TitleRow(title: String){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(SmallPadding),
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            AddItemButton(Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
fun InventoryLegend() {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "Item",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column (
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column (
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "Weight",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
            .weight(1.9f)
        ) {
            Text(
                text = "Action",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
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
        Column {
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

@Composable
fun AddItemButton(modifier: Modifier) {
    val openDialog = remember { mutableStateOf(false) }
    val itemName = remember { mutableStateOf("") }
    val quantity = remember { mutableStateOf("1") }
    val weight = remember { mutableStateOf("") }

    Button(
        modifier = modifier.padding(end = SmallPadding),
        onClick = { openDialog.value = true }
    ) {
        Text("Add item")
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Add item to inventory") },
            text = {
                Column {
                    TextField(
                        value = itemName.value,
                        onValueChange = { itemName.value = it },
                        label = { Text("Item name") }
                    )
                    TextField(
                        value = quantity.value,
                        onValueChange = { quantity.value = it },
                        label = { Text("Quantity") }
                    )
                    TextField(
                        value = weight.value,
                        onValueChange = { weight.value = it },
                        label = { Text("Weight") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text("Dismiss")
                }
            }
        )
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