package it.brokenengineers.dnd_character_manager.screens.sheet

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.ui.theme.CheckBoxMedium
import it.brokenengineers.dnd_character_manager.ui.theme.LargeVerticalSpacing
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.OverBottomNavBar
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XSPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel
import it.brokenengineers.dnd_character_manager.viewModel.TestTags

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
/**
 * Composable for the character sheet screen.
 * It shows the character's main information, abilities, skills, saving throws,
 * and allows the user to manage the character's hit points, level up, and rest.
 * @param navController: NavController
 * @param characterId: Int (id of the character to show)
 * @param viewModel: DndCharacterManagerViewModel
 * */
fun CharacterSheetScreen(
    navController: NavHostController,
    characterId: Int,
    viewModel: DndCharacterManagerViewModel
) {
    // when id passed changed fetch the character
    LaunchedEffect(characterId) {
        viewModel.fetchCharacterById(characterId)
    }
    val char by viewModel.selectedCharacter.collectAsState(initial = null)

    Scaffold(
        bottomBar = { CharacterSheetNavBar(navController, characterId) }
    ) { innerPadding ->
        char?.let { character ->
            val savingThrowsString = stringResource(id = R.string.saving_throws)
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = OverBottomNavBar)
            ) {
                Spacer(modifier = Modifier.height(LargeVerticalSpacing))
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val (head, charImage, mainInfo, abilityRow,
                        skillsRow, savingThrowsTitle, savingThrowsRow) = createRefs()
                    CharacterSheetHead(
                        dndCharacter = character,
                        navController = navController,
                        modifier = Modifier
                            .constrainAs(head) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                    )
                    ImageAndDamageRow(
                        viewModel = viewModel,
                        dndCharacter = character,
                        modifier = Modifier
                            .constrainAs(charImage) {
                                top.linkTo(head.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                    MainInfo(
                        dndCharacter = character,
                        modifier = Modifier
                        .constrainAs(mainInfo) {
                            top.linkTo(charImage.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                    AbilityRow(
                        dndCharacter = character,
                        modifier = Modifier
                            .constrainAs(abilityRow) {
                                top.linkTo(mainInfo.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                    SkillRow(
                        dndCharacter = character,
                        modifier = Modifier
                        .constrainAs(skillsRow) {
                            top.linkTo(abilityRow.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = savingThrowsString,
                        modifier = Modifier
                            .padding(SmallPadding)
                            .constrainAs(savingThrowsTitle) {
                                top.linkTo(skillsRow.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )
                    SavingThrowsRow(
                        dndCharacter = character,
                        modifier = Modifier
                        .constrainAs(savingThrowsRow) {
                            top.linkTo(savingThrowsTitle.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }
            }
        } ?: run {
            // fetch the character until correct recompose
            while (viewModel.selectedCharacter.value == null) {
                viewModel.fetchCharacterById(characterId)
                Thread.sleep(500)
            }

        }
    }
}

/**
 * Composable showing basic character information: race, class, hit points.
 * Contains buttons to start rest and level up
 * */
@Composable
fun CharacterSheetHead(modifier: Modifier, dndCharacter: DndCharacter, navController: NavHostController) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout (
            modifier = Modifier.fillMaxSize()
        ){
            val (cName, cLevel, cRace, cClass, hpCard, restButton, levelUpButton) = createRefs()
            Text(
                text = dndCharacter.name,
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(cName) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
                    .testTag(TestTags.CHARACTER_NAME_TEXT)
            )
            Text(
                text = "Level: ${dndCharacter.level}",
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(cLevel) {
                        start.linkTo(cName.end)
                        top.linkTo(parent.top)
                    }
                    .testTag(TestTags.CHARACTER_LEVEL_TEXT)
            )
            dndCharacter.race?.let {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = it.name,
                    modifier = Modifier
                        .padding(SmallPadding)
                        .constrainAs(cRace) {
                            start.linkTo(parent.start)
                            top.linkTo(cName.bottom)
                        }
                        .testTag(TestTags.CHARACTER_RACE_TEXT)
                )
            }
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = dndCharacter.dndClass!!.name,
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(cClass) {
                        start.linkTo(cRace.end)
                        top.linkTo(cName.bottom)
                    }
                    .testTag(TestTags.CHARACTER_CLASS_TEXT)
            )
            HitPointsCard(
                dndCharacter = dndCharacter,
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(hpCard) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
            )
            RestButton(
                dndCharacter = dndCharacter,
                navController = navController,
                modifier = Modifier
                    .constrainAs(restButton) {
                        end.linkTo(hpCard.start)
                        top.linkTo(parent.top)
                    }
                    .testTag(TestTags.REST_BUTTON)
            )
            LevelUpButton(
                dndCharacter = dndCharacter,
                navController = navController,
                modifier = Modifier
                    .constrainAs(levelUpButton) {
                        end.linkTo(restButton.start)
                        top.linkTo(parent.top)
                    }
                    .testTag(TestTags.LEVELUP_BUTTON)
            )
        }
    }
}

@Composable
fun HitPointsCard(modifier: Modifier, dndCharacter: DndCharacter) {
    val hpString = stringResource(id = R.string.hp)
    val tempHpString = stringResource(id = R.string.temp_hp)
    val maxHp = dndCharacter.getMaxHp().toString()
    val currentHp = dndCharacter.remainingHp
    val tempHp = dndCharacter.tempHp
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                modifier = Modifier
                    .testTag(TestTags.HP_TEXT)
                    .padding(SmallPadding),
                text = "$hpString: $currentHp/$maxHp"
            )
            Text(
                modifier = Modifier
                    .testTag(TestTags.TEMP_HP_TEXT)
                    .padding(start = SmallPadding, end = SmallPadding, bottom = SmallPadding),
                text = "$tempHpString: $tempHp"
            )
        }
    }
}

@Composable
fun RestButton(modifier: Modifier, navController: NavHostController, dndCharacter: DndCharacter) {
    MaterialTheme(
        shapes = Shapes(small = CircleShape)
    ) {
        IconButton(
            modifier = modifier,
            onClick = {
                navController.navigate("rest/${dndCharacter.id}") {
                    popUpTo(navController.graph.findStartDestination().id)

                    launchSingleTop = true
                    restoreState = true
                }
            },
        ) {
            Icon(Icons.Default.Favorite, contentDescription = "Delete")
        }
    }
}

@Composable
fun LevelUpButton(modifier: Modifier, navController: NavHostController, dndCharacter: DndCharacter) {
    MaterialTheme(
        shapes = Shapes(small = CircleShape)
    ) {
        IconButton(
            modifier = modifier,
            onClick = {
                navController.navigate("levelup/${dndCharacter.id}") {
                    popUpTo(navController.graph.findStartDestination().id)

                    launchSingleTop = true
                    restoreState = true
                }
            },
        ) {
            Icon(Icons.Default.Star, contentDescription = "Delete")
        }
    }
}

/**
 * Composable used to manage the character's (temp) hit points.
 * It shows the character's image and various buttons to manage the hit points.
 * */
@Composable
fun ImageAndDamageRow(
    modifier: Modifier,
    dndCharacter: DndCharacter,
    viewModel: DndCharacterManagerViewModel
) {
    val hitButtonString = stringResource(id = R.string.hit_button)
    val editHpButtonString = stringResource(id = R.string.edit_hp_button)
    val editTempHpButtonString = stringResource(id = R.string.edit_temp_hp_button)

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
                    .testTag(TestTags.EDIT_HP_BUTTON)
                    .constrainAs(editHpButton) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(editTempHpButton.top)
                    },
                text = editHpButtonString
            ) {
                showDialogHp.value = true
            }
            MyButton(
                modifier = Modifier
                    .constrainAs(editTempHpButton) {
                        top.linkTo(editHpButton.bottom)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .testTag(TestTags.EDIT_TEMP_HP_BUTTON),
                text = editTempHpButtonString,
            ) {
                showDialogTempHp.value = true
            }
            MyButton(
                modifier = Modifier
                    .testTag(TestTags.HIT_BUTTON)
                    .constrainAs(hitButton) {
                        top.linkTo(parent.top)
                        end.linkTo(editHpButton.start)
                        bottom.linkTo(editTempHpButton.top)
                    },
                text = hitButtonString,
            ) {
                showDialogHit.value = true
            }
        }
    }

    val hitManagementString = stringResource(id = R.string.hit_management)
    val hpString = stringResource(id = R.string.hp)
    val addHitString = stringResource(id = R.string.add_hit)

    val hpManagementString = stringResource(id = R.string.hp_management)
    val addHpString = stringResource(id = R.string.add_hp)
    val loseHpString = stringResource(id = R.string.lose_hp)

    val tempHpManagementString = stringResource(id = R.string.temp_hp_management)
    val tempHpString = stringResource(id = R.string.temp_hp)
    val addTempHpString = stringResource(id = R.string.add_temp_hp)
    val loseTempHpString = stringResource(id = R.string.lose_temp_hp)

    if (showDialogHit.value) {
        hitValue.value = ""
        AlertDialog(
            onDismissRequest = {
                showDialogHit.value = false
            },
            title = { Text(hitManagementString) },
            text = {
                OutlinedTextField(
                    modifier = Modifier.testTag(TestTags.DIALOG_HIT_FIELD),
                    value = hitValue.value,
                    onValueChange = { hitValue.value = it },
                    label = { Text(text=hpString, color = MaterialTheme.colorScheme.onSurface) }
                )
            },
            confirmButton = {
                Button(
                    modifier = Modifier.testTag(TestTags.DIALOG_ADD_HIT_BUTTON),
                    onClick = {
                        viewModel.addHit(hitValue.value.toInt())
                        showDialogHit.value = false
                    },
                ) {
                    Text(addHitString)
                }
            },
        )
    }


    if (showDialogHp.value) {
        hp.value = ""
        AlertDialog(
            onDismissRequest = {
                showDialogHp.value = false
            },
            title = { Text(hpManagementString) },
            text = {
                OutlinedTextField(
                    modifier = Modifier.testTag(TestTags.DIALOG_HP_FIELD),
                    value = hp.value,
                    onValueChange = { hp.value = it },
                    label = { Text(hpString, color = MaterialTheme.colorScheme.onSurface) }
                )
            },
            confirmButton = {
                Button(
                    modifier = Modifier.testTag(TestTags.DIALOG_ADD_HP_BUTTON),
                    onClick = {
                        viewModel.addHp(hp.value.toInt())
                        showDialogHp.value = false
                    }
                ) {
                    Text(addHpString)
                }
            },

            dismissButton = {
                Button(
                    modifier = Modifier.testTag(TestTags.DIALOG_LOSE_HP_BUTTON),
                    onClick = {
                        viewModel.loseHp(hp.value.toInt())
                        showDialogHp.value = false
                    }
                ) {
                    Text(loseHpString)
                }
            }
        )
        }

    if (showDialogTempHp.value) {
        tempHp.value = ""
        AlertDialog(
            onDismissRequest = {
                showDialogTempHp.value = false
            },
            title = { Text(tempHpManagementString) },
            text = {
                OutlinedTextField(
                    modifier = Modifier.testTag(TestTags.DIALOG_TEMP_HP_FIELD),
                    value = tempHp.value,
                    onValueChange = { tempHp.value = it },
                    label = { Text(tempHpString, color = MaterialTheme.colorScheme.onSurface) }
                )
            },
            confirmButton = {
                Button(
                    modifier = Modifier.testTag(TestTags.DIALOG_ADD_TEMP_HP_BUTTON),
                    onClick = {
                        viewModel.addTempHp(tempHp.value.toInt())
                        showDialogTempHp.value = false
                    }
                ) {
                    Text(addTempHpString)
                }
            },
            dismissButton = {
                Button(
                    modifier = Modifier.testTag(TestTags.DIALOG_LOSE_TEMP_HP_BUTTON),
                    onClick = {
                        viewModel.loseTempHp(tempHp.value.toInt())
                        showDialogTempHp.value = false
                    }
                ) {
                    Text(loseTempHpString)
                }
            }
        )
    }
}

/**
 * Composable showing character's info about: proficiency bonus, walk speed, initiative, armor class.
 * */
@Composable
fun MainInfo(modifier: Modifier, dndCharacter: DndCharacter) {
    val profBonusString = stringResource(id = R.string.prof_bonus)
    val walkSpeedString = stringResource(id = R.string.walk_speed)
    val initString = stringResource(id = R.string.initiative)
    val armorClassString = stringResource(id = R.string.armor_class)
    val proficiencyBonus = dndCharacter.getProficiencyBonus().toString()
    val walkSpeed = dndCharacter.getWalkSpeed().toString()
    val initiative = dndCharacter.getInitiative().toString()
    val armorClassValue = dndCharacter.getArmorClass().toString()

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (profBonus, speed, init, armorClass) = createRefs()
            MainInfoElem(
                name = profBonusString,
                value = proficiencyBonus,
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
                name = walkSpeedString,
                value = "$walkSpeed ft."
            )
            MainInfoElem(
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(init) {
                        start.linkTo(speed.end)
                        top.linkTo(parent.top)
                        end.linkTo(armorClass.start)
                    },
                name = initString,
                value = initiative
            )
            MainInfoElem(
                modifier = Modifier
                    .padding(top = SmallPadding, end = MediumPadding)
                    .constrainAs(armorClass) {
                        start.linkTo(init.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                name = armorClassString,
                value = armorClassValue
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

/**
 * Composable showing character's ability values and modifiers.
 * */
@Composable
fun AbilityRow(modifier: Modifier, dndCharacter: DndCharacter) {
    val strengthString = stringResource(id = R.string.strength)
    val dexterityString = stringResource(id = R.string.dexterity)
    val constitutionString = stringResource(id = R.string.constitution)
    val intelligenceString = stringResource(id = R.string.intelligence)
    val wisdomString = stringResource(id = R.string.wisdom)
    val charismaString = stringResource(id = R.string.charisma)

    val strValue = dndCharacter.getAbilityValue(AbilityEnum.STRENGTH).toString()
    val strModifier = dndCharacter.getAbilityModifier(AbilityEnum.STRENGTH).toString()
    val dexValue = dndCharacter.getAbilityValue(AbilityEnum.DEXTERITY).toString()
    val dexModifier = dndCharacter.getAbilityModifier(AbilityEnum.DEXTERITY).toString()
    val conValue = dndCharacter.getAbilityValue(AbilityEnum.CONSTITUTION).toString()
    val conModifier = dndCharacter.getAbilityModifier(AbilityEnum.CONSTITUTION).toString()
    val intValue = dndCharacter.getAbilityValue(AbilityEnum.INTELLIGENCE).toString()
    val intModifier = dndCharacter.getAbilityModifier(AbilityEnum.INTELLIGENCE).toString()
    val wisValue = dndCharacter.getAbilityValue(AbilityEnum.WISDOM).toString()
    val wisModifier = dndCharacter.getAbilityModifier(AbilityEnum.WISDOM).toString()
    val chaValue = dndCharacter.getAbilityValue(AbilityEnum.CHARISMA).toString()
    val chaModifier = dndCharacter.getAbilityModifier(AbilityEnum.CHARISMA).toString()

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (str, dex, con, int, wis, cha) = createRefs()
            AbilityCard(
                name = strengthString,
                value = strValue,
                bonus = strModifier,
                modifier = Modifier
                    .constrainAs(str) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(dex.start)
                    }
            )
            AbilityCard(
                name = dexterityString,
                value = dexValue,
                bonus = dexModifier,
                modifier = Modifier
                    .constrainAs(dex) {
                        start.linkTo(str.end)
                        top.linkTo(parent.top)
                        end.linkTo(con.start)
                    }
            )
            AbilityCard(
                name = constitutionString,
                value = conValue,
                bonus = conModifier,
                modifier = Modifier
                    .constrainAs(con) {
                        start.linkTo(dex.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
            AbilityCard(
                name = intelligenceString,
                value = intValue,
                bonus = intModifier,
                modifier = Modifier
                    .constrainAs(int) {
                        start.linkTo(parent.start)
                        top.linkTo(str.bottom)
                        end.linkTo(wis.start)
                    }
            )
            AbilityCard(
                name = wisdomString,
                value = wisValue,
                bonus = wisModifier,
                modifier = Modifier
                    .constrainAs(wis) {
                        start.linkTo(int.end)
                        top.linkTo(dex.bottom)
                        end.linkTo(cha.start)
                    }
            )
            AbilityCard(
                name = charismaString,
                value = chaValue,
                bonus = chaModifier,
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

/**
 * Composable showing character's skills and their values.
 * */
@Composable
fun SkillRow(modifier: Modifier, dndCharacter: DndCharacter) {
    val skills1 = dndCharacter.getSkills().slice(0..8)
    val skills2 = dndCharacter.getSkills().slice(9..17)

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
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
                        name = it.first.name,
                        value = it.second.toString()
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
                        name = it.first.name,
                        value = it.second.toString()
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

/**
 * Composable showing character's saving throws, their values, and eventual proficiencies.
 * */
@Composable
fun SavingThrowsRow(modifier: Modifier, dndCharacter: DndCharacter) {
    val strengthString = stringResource(id = R.string.strength)
    val dexterityString = stringResource(id = R.string.dexterity)
    val constitutionString = stringResource(id = R.string.constitution)
    val intelligenceString = stringResource(id = R.string.intelligence)
    val wisdomString = stringResource(id = R.string.wisdom)
    val charismaString = stringResource(id = R.string.charisma)

    val strSavingThrowBonus = dndCharacter.getSavingThrowBonus(AbilityEnum.STRENGTH).toString()
    val dexSavingThrowBonus = dndCharacter.getSavingThrowBonus(AbilityEnum.DEXTERITY).toString()
    val conSavingThrowBonus = dndCharacter.getSavingThrowBonus(AbilityEnum.CONSTITUTION).toString()
    val intSavingThrowBonus = dndCharacter.getSavingThrowBonus(AbilityEnum.INTELLIGENCE).toString()
    val wisSavingThrowBonus = dndCharacter.getSavingThrowBonus(AbilityEnum.WISDOM).toString()
    val chaSavingThrowBonus = dndCharacter.getSavingThrowBonus(AbilityEnum.CHARISMA).toString()

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (str, dex, con, int, wis, cha) = createRefs()
            SavingThrowCard(
                name = strengthString,
                bonus = strSavingThrowBonus,
                proficiency = dndCharacter.isProficientInAbility(AbilityEnum.STRENGTH),
                modifier = Modifier
                    .constrainAs(str) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(dex.start)
                    }
            )
            SavingThrowCard(
                modifier = Modifier
                    .constrainAs(dex) {
                        start.linkTo(str.end)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                name = dexterityString,
                bonus = dexSavingThrowBonus,
                proficiency = dndCharacter.isProficientInAbility(AbilityEnum.DEXTERITY)
            )
            SavingThrowCard(
                modifier = Modifier
                    .constrainAs(con) {
                        start.linkTo(parent.start)
                        top.linkTo(str.bottom)
                        end.linkTo(int.start)
                    },
                name = constitutionString,
                bonus = conSavingThrowBonus,
                proficiency = dndCharacter.isProficientInAbility(AbilityEnum.CONSTITUTION)
            )
            SavingThrowCard(
                modifier = Modifier
                    .constrainAs(int) {
                        start.linkTo(con.end)
                        top.linkTo(dex.bottom)
                        end.linkTo(parent.end)
                    },
                name = intelligenceString,
                bonus = intSavingThrowBonus,
                proficiency = dndCharacter.isProficientInAbility(AbilityEnum.INTELLIGENCE)
            )
            SavingThrowCard(
                modifier = Modifier
                    .constrainAs(wis) {
                        start.linkTo(parent.start)
                        top.linkTo(con.bottom)
                        end.linkTo(cha.start)
                    },
                name = wisdomString,
                bonus = wisSavingThrowBonus,
                proficiency = dndCharacter.isProficientInAbility(AbilityEnum.WISDOM)
            )
            SavingThrowCard(
                modifier = Modifier
                    .constrainAs(cha) {
                        start.linkTo(wis.end)
                        top.linkTo(con.bottom)
                        end.linkTo(parent.end)
                    },
                name = charismaString,
                bonus = chaSavingThrowBonus,
                proficiency = dndCharacter.isProficientInAbility(AbilityEnum.CHARISMA)
            )
        }
    }
}

@Composable
fun SavingThrowCard(modifier: Modifier, name: String, bonus: String, proficiency: Boolean){
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
            val (nameText, bonusText, proficiencyCheckBox) = createRefs()
            RoundCheckbox(
                check = proficiency,
                modifier = Modifier
                    .size(CheckBoxMedium)
                    .constrainAs(proficiencyCheckBox) {
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
                        start.linkTo(proficiencyCheckBox.end)
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
fun RoundCheckbox(modifier: Modifier, check: Boolean) {
    MaterialTheme(
        shapes = Shapes(small = CircleShape)
    ) {
        Checkbox(
            modifier = modifier,
            checked = check,
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
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = onClick,
        modifier = modifier
            .padding(end = SmallPadding)
    ) {
        Text(text)
    }
}


/**
 * Composable for the bottom navigation bar shown in:
 *    character sheet screen, inventory screen, and attack screen.
 * */
@Composable
fun CharacterSheetNavBar(navController: NavHostController, characterId: Int) {
    BottomNavigation (
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        BottomNavItem.items.forEach { screen ->
            BottomNavigationItem(
                icon = { },
                label = { Text(screen.label, color = MaterialTheme.colorScheme.onSurface) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    var route = "home"
                    if (screen.route != "home"){
                        route = "${screen.route}/$characterId"
                    }
                    Log.i("CharacterSheetNavBar", "route: $route")
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id)

                        launchSingleTop = true
                        restoreState = true
                    }
                },

            )
        }
    }
}

sealed class BottomNavItem(val route: String, val label: String) {
    data object Home : BottomNavItem("home", "Home")
    data object Sheet : BottomNavItem("sheet", "Sheet")
    data object Inventory : BottomNavItem("inventory", "Inventory")
    data object Attack : BottomNavItem("attack", "Attack & Spells")
    companion object {
        val items = listOf(Home, Sheet, Inventory, Attack)
    }
}




