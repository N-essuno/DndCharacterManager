package it.brokenengineers.dnd_character_manager.screens.sheet

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.ui.theme.LargePadding
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XSPadding

// TODO: add temp hp visualization

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CharacterSheetScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    Scaffold (
        bottomBar = { CharacterSheetNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 60.dp)
        ) {
            Spacer(modifier = Modifier.height(LargePadding))
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (head, charImage, mainInfo, abilityRow,
                    skillsRow, savingThrowsTitle, savingThrowsRow) = createRefs()
                CharacterSheetHead(modifier = Modifier
                    .constrainAs(head) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                ImageAndDamageRow(modifier = Modifier
                    .constrainAs(charImage) {
                        top.linkTo(head.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                MainInfo(modifier = Modifier
                    .constrainAs(mainInfo) {
                        top.linkTo(charImage.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                AbilityRow(modifier = Modifier
                    .constrainAs(abilityRow) {
                        top.linkTo(mainInfo.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                SkillRow(modifier = Modifier
                    .constrainAs(skillsRow) {
                        top.linkTo(abilityRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Saving throws",
                    modifier = Modifier
                        .padding(SmallPadding)
                        .constrainAs(savingThrowsTitle) {
                            top.linkTo(skillsRow.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                SavingThrowsRow(modifier = Modifier
                    .constrainAs(savingThrowsRow) {
                        top.linkTo(savingThrowsTitle.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
        }
    }
}

@Composable
fun CharacterSheetHead(modifier: Modifier) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout (
            modifier = Modifier.fillMaxSize()
        ){
            val (cName, cRace, cClass, hpCard, restButton) = createRefs()
            Text(
                text = "Character Name",
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(cName) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            )
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Character Race",
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(cRace) {
                        start.linkTo(parent.start)
                        top.linkTo(cName.bottom)
                    }
            )
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Character Class",
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(cClass) {
                        start.linkTo(cRace.end)
                        top.linkTo(cName.bottom)
                    }
            )
            HitPointsCard(
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(hpCard) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
            )
            RestButton(
                modifier = Modifier
                    .constrainAs(restButton) {
                        end.linkTo(hpCard.start)
                        top.linkTo(parent.top)
                    }
            )
        }
    }
}

@Composable
fun HitPointsCard(modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO: get values based on character
            Text("140/140")
            Text(
                modifier = Modifier.padding(XSPadding),
                text = "Hit Points"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestButton(modifier: Modifier) {
    val showDialog = remember { mutableStateOf(false) }
    MaterialTheme(
        shapes = Shapes(small = CircleShape)
    ) {
        IconButton(
            modifier = modifier,
            onClick = { showDialog.value = true },
        ) {
            Icon(Icons.Default.Favorite, contentDescription = "Delete")
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = { Text("Rest Options") },
            text = { Text("Choose an option") },
            confirmButton = {
                Button(
                    onClick = {
                        // TODO: handle short rest
                        showDialog.value = false
                    }
                ) {
                    Text("Short Rest")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // TODO handle long rest
                        showDialog.value = false
                    }
                ) {
                    Text("Long Rest")
                }
            }
        )
    }
}

@Composable
fun ImageAndDamageRow(modifier: Modifier) {
    val showDialogHp = remember { mutableStateOf(false) }
    val showDialogTempHp = remember { mutableStateOf(false) }
    val showDialogHit = remember { mutableStateOf(false) }
    val hp = remember { mutableStateOf("") }
    val tempHp = remember { mutableStateOf("") }
    val hitValue = remember { mutableStateOf("") }

    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxSize()
    ) {
        ConstraintLayout (
            modifier = Modifier.fillMaxSize()
        ){
            val (charImage, editHpButton, editTempHpButton, hitButton) = createRefs()
            Image(
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(charImage) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Character Image"
            )
            MyButton(
                modifier = Modifier
                    .constrainAs(editHpButton) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(editTempHpButton.top)
                    },
                text = "Edit HP",
            ) {
                showDialogHp.value = true
            }
            MyButton(
                modifier = Modifier
                    .constrainAs(editTempHpButton) {
                        top.linkTo(editHpButton.bottom)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
                text = "Edit temp HP",
            ) {
                showDialogTempHp.value = true
            }
            MyButton(
                modifier = Modifier
                    .constrainAs(hitButton) {
                        top.linkTo(parent.top)
                        end.linkTo(editHpButton.start)
                        bottom.linkTo(editTempHpButton.top)
                    },
                text = "Hit",
            ) {
                showDialogHit.value = true
            }
        }
        }

        if (showDialogHit.value) {
        AlertDialog(
            onDismissRequest = {
                showDialogHit.value = false
            },
            title = { Text("Hit Management") },
            text = {
                OutlinedTextField(
                    value = hitValue.value,
                    onValueChange = { hitValue.value = it },
                    label = { Text("HP") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        // TODO: handle hit value
                        showDialogHit.value = false
                    }
                ) {
                    Text("Add Hit")
                }
            },
        )
    }

        if (showDialogHp.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialogHp.value = false
                },
                title = { Text("HP Management") },
                text = {
                    OutlinedTextField(
                        value = hp.value,
                        onValueChange = { hp.value = it },
                        label = { Text("HP") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // TODO: handle adding hp
                            showDialogHp.value = false
                        }
                    ) {
                        Text("Add HP")
                    }
                },

                dismissButton = {
                    Button(
                        onClick = {
                            // TODO handle losing HP
                            showDialogHp.value = false
                        }
                    ) {
                        Text("Lose HP")
                    }
                }
            )
        }

        if (showDialogTempHp.value) {
            AlertDialog(
                onDismissRequest = {
                    showDialogTempHp.value = false
                },
                title = { Text("Temp HP Management") },
                text = {
                    OutlinedTextField(
                        value = tempHp.value,
                        onValueChange = { tempHp.value = it },
                        label = { Text("Temp HP") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // TODO: handle adding temp hp
                            showDialogTempHp.value = false
                        }
                    ) {
                        Text("Add Temp HP")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            // TODO handle losing temp hp
                            showDialogTempHp.value = false
                        }
                    ) {
                        Text("Lose Temp HP")
                    }
                }
            )
        }
}

@Composable
fun MainInfo(modifier: Modifier) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (profBonus, speed, init, armorClass) = createRefs()
            // TODO: get values based on character
            MainInfoElem(
                name = "PROF. BONUS",
                value = "+3",
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(profBonus) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            )
            MainInfoElem(
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(speed) {
                        start.linkTo(profBonus.end)
                        top.linkTo(parent.top)
                        end.linkTo(init.start)
                    },
                name = "WLK. SPEED",
                value = "30 ft."
            )
            MainInfoElem(
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(init) {
                        start.linkTo(speed.end)
                        top.linkTo(parent.top)
                        end.linkTo(armorClass.start)
                    },
                name = "INITIAT.",
                value = "+3"
            )
            MainInfoElem(
                modifier = Modifier
                    .padding(top = SmallPadding, end = MediumPadding)
                    .constrainAs(armorClass) {
                        start.linkTo(init.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                name = "ARMOR CLASS",
                value = "18"
            )
        }
    }
}

@Composable
fun MainInfoElem(modifier: Modifier, name: String, value: String) {
    Card (
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        ConstraintLayout {
            val (nameText, valueText) = createRefs()
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = value,
                modifier = Modifier
                    .padding(XSPadding)
                    .constrainAs(valueText) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = name,
                modifier = Modifier
                    .padding(XSPadding)
                    .constrainAs(nameText) {
                        start.linkTo(parent.start)
                        top.linkTo(valueText.bottom)
                        end.linkTo(parent.end)
                    }
            )

        }
    }
}

@Composable
fun AbilityRow(modifier: Modifier) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (str, dex, con, int, wis, cha) = createRefs()
            // TODO: get points and bonus based on abilities passed
            AbilityCard(
                name = "STRENGTH",
                value = "16",
                bonus = "+3",
                modifier = Modifier
                    .constrainAs(str) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(dex.start)
                    }
            )
            AbilityCard(
                name = "DEXTERITY",
                value = "14",
                bonus = "+2",
                modifier = Modifier
                    .constrainAs(dex) {
                        start.linkTo(str.end)
                        top.linkTo(parent.top)
                        end.linkTo(con.start)
                    }
            )
            AbilityCard(
                name = "CONSTITUTION",
                value = "14",
                bonus = "+2",
                modifier = Modifier
                    .constrainAs(con) {
                        start.linkTo(dex.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
            AbilityCard(
                name = "INTELLIGENCE",
                value = "10",
                bonus = "+0",
                modifier = Modifier
                    .constrainAs(int) {
                        start.linkTo(parent.start)
                        top.linkTo(str.bottom)
                        end.linkTo(wis.start)
                    }
            )
            AbilityCard(
                name = "WISDOM",
                value = "12",
                bonus = "+1",
                modifier = Modifier
                    .constrainAs(wis) {
                        start.linkTo(int.end)
                        top.linkTo(dex.bottom)
                        end.linkTo(cha.start)
                    }
            )
            AbilityCard(
                name = "CHARISMA",
                value = "8",
                bonus = "-1",
                modifier = Modifier
                    .constrainAs(cha) {
                        start.linkTo(wis.end)
                        top.linkTo(con.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun AbilityCard(modifier: Modifier, name: String, value: String, bonus: String) {
    Card (
        modifier = modifier
            .fillMaxWidth(0.33f)
            .padding(SmallPadding),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        ConstraintLayout (
            modifier = Modifier.fillMaxSize()
        ){
            val (nameText, valueText, bonusText) = createRefs()
            Text(
                style = MaterialTheme.typography.bodyLarge,
                text = value,
                modifier = Modifier.constrainAs(valueText) {
                    start.linkTo(parent.start)
                    top.linkTo(nameText.bottom)
                    end.linkTo(parent.end)
                }
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = name,
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(nameText) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = bonus,
                modifier = Modifier
                    .padding(XSPadding)
                    .constrainAs(bonusText) {
                        start.linkTo(parent.start)
                        top.linkTo(valueText.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun SkillRow(modifier: Modifier) {
    val skills1 = listOf(
        "Acrobatics", "Animal Handling", "Arcana", "Athletics",
        "Deception", "History", "Insight", "Intimidation", "Investigation"
    )
    val skills2 = listOf(
        "Medicine", "Nature", "Perception", "Performance",
        "Persuasion", "Religion", "Sleight of Hand", "Stealth", "Survival"
    )
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            // TODO: get points based on character
            val (col1, col2) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(col1) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(col2.start)
                    }
            ){
                skills1.forEach {
                    SkillBox(
                        name = it,
                        value = "+3"
                    )
                }
            }
            Column(
                modifier = Modifier
                    .constrainAs(col2) {
                        start.linkTo(col1.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            ) {
                skills2.forEach {
                    SkillBox(
                        name = it,
                        value = "+3"
                    )
                }
            }
        }
    }
}

@Composable
fun SkillBox(name: String, value: String) {
    Box (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
    ) {
        ConstraintLayout {
            val (nameText, valueText) = createRefs()
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = value,
                modifier = Modifier
                    .padding(XSPadding)
                    .constrainAs(valueText) {
                        start.linkTo(nameText.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = name,
                modifier = Modifier
                    .padding(XSPadding)
                    .constrainAs(nameText) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun SavingThrowsRow(modifier: Modifier) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (str, dex, con, int, wis, cha) = createRefs()
            // TODO: get bonus based on character
            SavingThrowCard(
                name = "STRENGTH",
                bonus = "+3",
                modifier = Modifier
                    .constrainAs(str) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(dex.start)
                    }
            )
            SavingThrowCard(
                name = "DEXTERITY",
                bonus = "+2",
                modifier = Modifier
                    .constrainAs(dex) {
                        start.linkTo(str.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
            SavingThrowCard(
                name = "CONSTITUTION",
                bonus = "+2",
                modifier = Modifier
                    .constrainAs(con) {
                        start.linkTo(parent.start)
                        top.linkTo(str.bottom)
                        end.linkTo(int.start)
                    }
            )
            SavingThrowCard(
                name = "INTELLIGENCE",
                bonus = "+0",
                modifier = Modifier
                    .constrainAs(int) {
                        start.linkTo(con.end)
                        top.linkTo(dex.bottom)
                        end.linkTo(parent.end)
                    }
            )
            SavingThrowCard(
                name = "WISDOM",
                bonus = "+1",
                modifier = Modifier
                    .constrainAs(wis) {
                        start.linkTo(parent.start)
                        top.linkTo(con.bottom)
                        end.linkTo(cha.start)
                    }
            )
            SavingThrowCard(
                name = "CHARISMA",
                bonus = "-1",
                modifier = Modifier
                    .constrainAs(cha) {
                        start.linkTo(wis.end)
                        top.linkTo(con.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun SavingThrowCard(modifier: Modifier, name: String, bonus: String){
    Card (
        modifier = modifier
            .fillMaxWidth(0.50f)
            .padding(SmallPadding),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        ConstraintLayout (
            modifier = Modifier.fillMaxSize()
        ){
            val (nameText, bonusText, proficiencyCheckBok) = createRefs()
            RoundCheckbox(
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(proficiencyCheckBok) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = name,
                modifier = Modifier
                    .constrainAs(nameText) {
                        start.linkTo(proficiencyCheckBok.end)
                        top.linkTo(parent.top)
                        end.linkTo(bonusText.start)
                        bottom.linkTo(parent.bottom)
                    }
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = bonus,
                modifier = Modifier
                    .padding(XSPadding)
                    .constrainAs(bonusText) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Composable
fun RoundCheckbox(modifier: Modifier) {
    // TODO: get checked based on character
    val checkedState = remember { mutableStateOf(true) }
    MaterialTheme(
        shapes = Shapes(small = CircleShape)
    ) {
        Checkbox(
            modifier = modifier,
            checked = checkedState.value,
            onCheckedChange = { },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
fun MyButton(modifier: Modifier, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(end = SmallPadding)
    ) {
        Text(text)
    }
}

@Composable
fun CharacterSheetNavBar(navController: NavHostController) {
    BottomNavigation (
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        BottomNavItem.items.forEach { screen ->
            BottomNavigationItem(
                icon = { },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)

                        launchSingleTop = true
                        restoreState = true
                    }
                },

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    DndCharacterManagerTheme {
        CharacterSheetScreen(navController)
    }
}

sealed class BottomNavItem(val route: String, val label: String) {
    data object Home : BottomNavItem("home", "Home")
    data object Sheet : BottomNavItem("sheet", "Sheet")
    data object Inventory : BottomNavItem("inventory", "Inventory")
    data object Spells : BottomNavItem("spells", "Spells")
    companion object {
        val items = listOf(Home, Sheet, Inventory, Spells)
    }
}




