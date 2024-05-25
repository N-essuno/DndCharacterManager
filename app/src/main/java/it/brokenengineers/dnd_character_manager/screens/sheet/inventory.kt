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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem
import it.brokenengineers.dnd_character_manager.ui.theme.LargeVerticalSpacing
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.MediumVerticalSpacing
import it.brokenengineers.dnd_character_manager.ui.theme.OverBottomNavBar
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallVerticalSpacing
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel

// TODO add gold visualization

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InventoryScreen(
    navController: NavHostController,
    characterId: Int,
    viewModel: DndCharacterManagerViewModel
) {
    LaunchedEffect(characterId) {
        viewModel.fetchCharacterById(characterId)
    }
    val char by viewModel.selectedCharacter.collectAsState(initial = null)
    val scrollState = rememberScrollState()

    char?.let { character ->
        Scaffold(
            bottomBar = { CharacterSheetNavBar(navController, characterId) }
        ) { innerPadding ->
            val inventoryTitleString = stringResource(id = R.string.inventory_title)
            val items = character.inventoryItems
            val maxWeight = character.getMaxCarryWeight().toString()
            val currentWeight = character.getCurrentCarryWeight().toString()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = OverBottomNavBar)
            ) {
                Spacer(modifier = Modifier.height(LargeVerticalSpacing))
                InventoryTitleRow(inventoryTitleString, viewModel)
                Spacer(modifier = Modifier.height(MediumVerticalSpacing))
                InventoryLegend()

                var testUse = true

                items?.forEach {inventoryItem ->
                    InventoryItemRow(inventoryItem, viewModel, testUse)
                    testUse = false
                }

                Spacer(modifier = Modifier.height(MediumVerticalSpacing))
                WeightRow(weight = currentWeight, maxWeight = maxWeight)
            }
        }
    }
}


@Composable
fun InventoryTitleRow(title: String, viewModel: DndCharacterManagerViewModel){
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
            AddItemButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun InventoryLegend() {
    val itemString = stringResource(id = R.string.item)
    val quantityString = stringResource(id = R.string.quantity)
    val weightString = stringResource(id = R.string.weight)
    val actionString = stringResource(id = R.string.action)
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = itemString,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = MediumPadding, end = SmallPadding,
                    top = SmallPadding, bottom = SmallPadding)
            )
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = quantityString,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
            )
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = weightString,
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
                text = actionString,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding)
            )
        }
    }
}

@Composable
fun InventoryItemRow(
    item: InventoryItem,
    viewModel: DndCharacterManagerViewModel,
    testUse: Boolean = false
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    )
    {
        var deleteTag = ""
        var incrementTag = ""
        var decrementTag = ""
        var itemTag = ""
        var quantityTag = ""
        var weightTag = ""
        if (testUse) {
            deleteTag = "delete_item"
            incrementTag = "increment_item"
            decrementTag = "decrement_item"
            itemTag = "item_name"
            quantityTag = "item_quantity"
            weightTag = "item_weight"
        }

        Column (
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
        ){
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = item.name,
                modifier = Modifier.padding(MediumPadding).testTag(itemTag)
                )
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ){
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = item.quantity.toString(),
                modifier = Modifier.padding(MediumPadding).testTag(quantityTag)
            )
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .weight(1f)
        ){
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = item.weight.toString(),
                modifier = Modifier.padding(MediumPadding).testTag(weightTag)
            )
        }
        Column (
            modifier = Modifier
                .weight(1.8f)
        ){
            Row {
                IconButton(
                    onClick = { viewModel.deleteInventoryItem(item) }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.testTag(deleteTag)
                        )
                }
                IconButton(
                    onClick = { viewModel.incrementInventoryItem(item) }
                ) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "Add",
                        modifier = Modifier.testTag(incrementTag)
                    )
                }
                IconButton(
                    onClick = { viewModel.decrementInventoryItem(item) }
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Remove",
                        modifier = Modifier.testTag(decrementTag)
                    )
                }
            }

        }
    }
}

@Composable
fun AddItemButton(modifier: Modifier, viewModel: DndCharacterManagerViewModel) {
    val itemNameString = stringResource(id = R.string.item_name)
    val quantityString = stringResource(id = R.string.quantity)
    val weightString = stringResource(id = R.string.weight)
    val addItemString = stringResource(id = R.string.add_item)
    val confirmString = stringResource(id = R.string.confirm)
    val cancelString = stringResource(id = R.string.cancel)
    val addItemToInventoryString = stringResource(id = R.string.add_item_to_inventory)

    val openDialog = remember { mutableStateOf(false) }
    val itemName = remember { mutableStateOf("") }
    val quantity = remember { mutableStateOf("1") }
    val weight = remember { mutableStateOf("") }

    Button(
        modifier = modifier.padding(end = SmallPadding).testTag("add_item"),
        onClick = { openDialog.value = true }
    ) {
        Text(addItemString)
    }

    if (openDialog.value) {
        itemName.value = ""
        quantity.value = "1"
        weight.value = ""
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = addItemToInventoryString) },
            text = {
                Column {

                    TextField(
                        modifier = Modifier.testTag("add_item_name"),
                        value = itemName.value,
                        onValueChange = { itemName.value = it },
                        label = { Text(itemNameString) }
                    )
                    TextField(
                        modifier = Modifier.testTag("add_item_quantity"),
                        value = quantity.value,
                        onValueChange = { quantity.value = it },
                        label = { Text(quantityString) }
                    )
                    TextField(
                        modifier = Modifier.testTag("add_item_weight"),
                        value = weight.value,
                        onValueChange = { weight.value = it },
                        label = { Text(weightString) }
                    )
                }
            },
            confirmButton = {
                Button(
                    modifier = Modifier.testTag("add_item_confirm"),
                    onClick = {
                        viewModel.addItemToInventory(
                            name = itemName.value,
                            quantity = quantity.value,
                            weight = weight.value
                        )
                    openDialog.value = false
                }) {
                    Text(confirmString)
                }
            },
            dismissButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text(cancelString)
                }
            }
        )
    }
}

@Composable
fun WeightRow(weight: String, maxWeight: String){
    val totalWeightString = stringResource(id = R.string.total_weight)
    val maxWeightString = stringResource(id = R.string.max_weight)
    val remainingWeightString = stringResource(id = R.string.remaining_weight)

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(start = SmallPadding).testTag("total_weight"),
                text = "$totalWeightString $weight",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(end = SmallPadding).testTag("max_weight"),
                text = "$maxWeightString $maxWeight",
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
                text = "$remainingWeightString ${maxWeight.toDouble() - weight.toDouble()}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

}


//@Preview(showBackground = true)
//@Composable
//fun InventoryScreenPreview() {
//    val navController = rememberNavController()
//    DndCharacterManagerTheme {
//        InventoryScreen(navController, it, viewModel)
//    }
//}