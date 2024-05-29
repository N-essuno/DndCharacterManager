package it.brokenengineers.dnd_character_manager.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavHostController
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.ui.theme.CharacterCardWidth
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.viewModel.TestTags

@Composable
fun CharacterCard(onHomePage: Boolean, dndCharacter: DndCharacter, navController: NavHostController, testCard : Boolean = false) {
    var cardModifier: Modifier
    val columnModifier: Modifier
    if (onHomePage) {
        cardModifier = Modifier
            .width(CharacterCardWidth)
            .clickable {
                // navigate to character sheet
                navController.navigate("sheet/${dndCharacter.id}") {
                    // pop up to the home screen
                    popUpTo("home_route") { inclusive = false }
                }
            }
            .semantics { contentDescription = "Character Card"}
        columnModifier = Modifier.fillMaxWidth()
    } else {
        cardModifier = Modifier
        columnModifier = Modifier
    }

    if (testCard) {
        cardModifier = cardModifier.testTag(TestTags.TEST_CARD)
    }

    Card(
        modifier = cardModifier
            .padding(start = MediumPadding, end = MediumPadding),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = columnModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image( // TODO meybe use async image to retrieve image from url
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Character Image"
            )
            Text(
                text = dndCharacter.name,
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding)
            )
            Text(
                text = dndCharacter.dndClass!!.name,
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding)
            )
            Text(
                text = dndCharacter.level.toString(),
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding)
            )
            Text(
                text = "Max Hp: ${dndCharacter.getMaxHp()} left: ${dndCharacter.remainingHp}",
                modifier = Modifier.padding(start = SmallPadding, end = SmallPadding)
            )
            Spacer(modifier = Modifier.padding(SmallPadding))
        }
    }
}