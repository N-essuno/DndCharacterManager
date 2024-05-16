package it.brokenengineers.dnd_character_manager.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.ui.theme.LargePadding
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XSPadding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CharacterSheet(modifier: Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(LargePadding))
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ){
            val (head, charImage, mainInfo, abilityRow1, abilityRow2, skillsRow) = createRefs()
            CharacterSheetHead(modifier = Modifier
                .constrainAs(head) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            CharacterImage(modifier = Modifier
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
                    .constrainAs(abilityRow1) {
                        top.linkTo(mainInfo.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            SkillsRow(modifier = Modifier
                .constrainAs(skillsRow) {
                    top.linkTo(abilityRow1.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
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
                text = "Character Race",
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(cRace) {
                        start.linkTo(parent.start)
                        top.linkTo(cName.bottom)
                    }
            )
            Text(
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
            Text("Hit Points")
        }
    }
}

@Composable
fun RestButton(modifier: Modifier) {
    // square button
    Button(
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = androidx.core.R.drawable.ic_call_answer),
            contentDescription = "Rest")
    }
}

@Composable
fun CharacterImage(modifier: Modifier) {
    MyRow(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Character Image",
            modifier = modifier
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
                name = "INIT.",
                value = "+3"
            )
            MainInfoElem(
                modifier = Modifier
                    .padding(SmallPadding)
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
fun SkillsRow(modifier: Modifier) {
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
                    SkillCard(
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
                    SkillCard(
                        name = it,
                        value = "+3"
                    )
                }
            }
        }
    }
}

@Composable
fun SkillCard(name: String, value: String) {
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
fun MyRow(modifier: Modifier, function: @Composable () -> Unit) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            function()
        }
    }
}

// preview
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DndCharacterManagerTheme {
        CharacterSheet(Modifier)
    }
}

