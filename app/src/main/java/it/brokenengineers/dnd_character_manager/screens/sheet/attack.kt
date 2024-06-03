package it.brokenengineers.dnd_character_manager.screens.sheet

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.Weapon
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.getRagesPerDay
import it.brokenengineers.dnd_character_manager.ui.theme.IconButtonMedium
import it.brokenengineers.dnd_character_manager.ui.theme.MediumVerticalSpacing
import it.brokenengineers.dnd_character_manager.ui.theme.OverBottomNavBar
import it.brokenengineers.dnd_character_manager.ui.theme.RadioButtonMedium
import it.brokenengineers.dnd_character_manager.ui.theme.ScrollColumnHeightMedium
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XXLVerticalSpacing
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AttackScreen(
    navController: NavHostController,
    characterId: Int,
    viewModel: DndCharacterManagerViewModel
) {
    LaunchedEffect(characterId) {
        viewModel.fetchCharacterById(characterId)
    }
    val char by viewModel.selectedCharacter.collectAsState(initial = null)
    char?.let {character ->
        val characterClass = character.dndClass

        if (characterClass == DndClassEnum.WIZARD.dndClass) {
            SpellsScreen(navController, character, viewModel)
        } else if (characterClass == DndClassEnum.BARBARIAN.dndClass) {
            MeleeScreen(navController, character)
        }

        Spacer(modifier = Modifier.height(XXLVerticalSpacing))

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MeleeScreen(
    navController: NavHostController,
    dndCharacter: DndCharacter
) {
    val meleeTitle = stringResource(id = R.string.melee_title)
    val attackBonusString = stringResource(id = R.string.attack_bonus)
    val totalRagesString = stringResource(id = R.string.total_rages)
    val weapon = dndCharacter.weapon
    val weaponList = listOf(weapon)
    val attackBonus = dndCharacter.getAttackBonus()
    val totalRages = getRagesPerDay(dndCharacter.level)

    Scaffold(
        bottomBar = { CharacterSheetNavBar(navController, dndCharacter.id) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(MediumVerticalSpacing))

            Text(
                modifier = Modifier.padding(SmallPadding),
                text = meleeTitle,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(MediumVerticalSpacing))

            WeaponTableHeader()
            weaponList.forEach { weapon ->
                weapon?.let {
                    WeaponRow(it)
                }
            }

            Spacer(modifier = Modifier.height(MediumVerticalSpacing))

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(SmallPadding),
                    text = "$attackBonusString $attackBonus",
                )
                Text(
                    modifier = Modifier.padding(SmallPadding),
                    text = "$totalRagesString $totalRages",
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SpellsScreen(
    navController: NavHostController,
    dndCharacter: DndCharacter,
    viewModel: DndCharacterManagerViewModel
) {
    val spellDcSavingThrowsString = stringResource(id = R.string.spell_dc_saving_throws)
    val attackBonusString = stringResource(id = R.string.attack_bonus)

    val spellDcSavingThrow = dndCharacter.getSpellDcSavingThrow()
    val attackBonus = dndCharacter.getAttackBonus()

    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = { CharacterSheetNavBar(navController, dndCharacter.id) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = OverBottomNavBar)
        ) {
            SpellsTitleRow()

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                SpellsLevelColumn(level = 0, dndCharacter = dndCharacter, viewModel = viewModel)
                SpellsLevelColumn(level = 1, dndCharacter = dndCharacter, viewModel = viewModel)
                SpellsLevelColumn(level = 2, dndCharacter = dndCharacter, viewModel = viewModel)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                SpellsLevelColumn(level = 3, dndCharacter = dndCharacter, viewModel = viewModel)
                SpellsLevelColumn(level = 4, dndCharacter = dndCharacter, viewModel = viewModel)
                SpellsLevelColumn(level = 5, dndCharacter = dndCharacter, viewModel = viewModel)
            }

            Spacer(modifier = Modifier.height(MediumVerticalSpacing))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                SpellsLevelColumn(level = 6, dndCharacter = dndCharacter, viewModel = viewModel)
                SpellsLevelColumn(level = 7, dndCharacter = dndCharacter, viewModel = viewModel)
                SpellsLevelColumn(level = 8, dndCharacter = dndCharacter, viewModel = viewModel)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.padding(SmallPadding),
                    text = "$spellDcSavingThrowsString $spellDcSavingThrow",
                )
                Text(
                    modifier = Modifier.padding(SmallPadding),
                    text = "$attackBonusString $attackBonus",
                )
            }

        }
    }
}

@Composable
fun SpellsLevelColumn(level: Int, dndCharacter: DndCharacter, viewModel: DndCharacterManagerViewModel){
    val slotsString = stringResource(id = R.string.slots)
    val levelString = stringResource(id = R.string.level)
    val spells = dndCharacter.spellsKnown?.filter { it.level == level }
    val slots = dndCharacter.availableSpellSlots?.get(level)

    var useSlotTag = ""
    var nSlotTag = ""
    if (level == 1){
        useSlotTag = TestTags.USE_SLOT_BUTTON
        nSlotTag = TestTags.N_SLOT_TEXT
    }


    val scrollState = rememberScrollState()
    Column {
        Row {
            Text(
                modifier = Modifier.padding(SmallPadding),
                text = "$levelString $level",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                modifier = Modifier.testTag(useSlotTag),
                onClick = { viewModel.useSpellSlot(level) }
            ) {
                Icon(
                    Icons.Default.Build,
                    modifier = Modifier
                        .size(IconButtonMedium),
                    contentDescription = "Delete")
            }
        }
        if (slots != null) {
            Text(
                modifier = Modifier
                    .padding(start = SmallPadding, bottom = SmallPadding)
                    .testTag(nSlotTag),
                text = "$slotsString $slots",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column(
            modifier = Modifier
                .height(ScrollColumnHeightMedium)
                .verticalScroll(scrollState)
        ) {
            spells?.let {
                it.forEach { spell ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            modifier = Modifier.size(RadioButtonMedium),
                            selected = dndCharacter.isSpellPrepared(spell),
                            onClick = { }
                        )
                        Text(
                            text = spell.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpellsTitleRow(){
    val spellsTitleString = stringResource(id = R.string.spells_title)
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(SmallPadding).testTag(TestTags.SPELLS_TITLE_TEXT),
                text = spellsTitleString,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun WeaponTableHeader(){
    val weaponString = stringResource(id = R.string.weapon)
    val damageString = stringResource(id = R.string.damage)

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(SmallPadding),
                text = weaponString,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Text("|", style = MaterialTheme.typography.bodyLarge)
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(SmallPadding),
                text = damageString,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun WeaponRow(weapon: Weapon){
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(SmallPadding),
                text = weapon.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text("|", style = MaterialTheme.typography.bodyLarge)
        Column (
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(SmallPadding),
                text = weapon.damage,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SpellsScreenPreview() {
//    val navController = rememberNavController()
//    DndCharacterManagerTheme {
//        AttackScreen(navController, it, viewModel)
//    }
//}