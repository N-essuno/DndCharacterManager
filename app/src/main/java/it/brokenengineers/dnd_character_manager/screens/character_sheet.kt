package it.brokenengineers.dnd_character_manager.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.constraintlayout.compose.ConstraintLayout
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CharacterSheet() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ){
            val (head, charImage, mainInfo) = createRefs()
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
            painter = painterResource(id = R.drawable.vincy_broken),
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
                },
                name = "INITIATIVE",
                value = "+3"
            )
            MainInfoElem(
                modifier = Modifier
                    .padding(SmallPadding)
                    .constrainAs(armorClass) {
                    start.linkTo(init.end)
                    top.linkTo(parent.top)
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
                modifier = Modifier.constrainAs(valueText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            )
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = name,
                modifier = Modifier.constrainAs(nameText) {
                    start.linkTo(parent.start)
                    top.linkTo(valueText.bottom)
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
        CharacterSheet()
    }
}

