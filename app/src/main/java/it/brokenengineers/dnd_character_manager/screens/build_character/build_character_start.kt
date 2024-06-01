package it.brokenengineers.dnd_character_manager.screens.build_character

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.ui.composables.OptionGroup
import it.brokenengineers.dnd_character_manager.ui.theme.LargePadding
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.viewModel.DndCharacterManagerViewModel

/**
 * Composable for the character creation screen.
 * The user can create a new character by providing
 * - character name
 * - race (among the available, currently Dwarf and Eladrin)
 * - class (among the available, currently Barbarian and Wizard)
 * - (optional) an image of the character
 * When the user confirms the creation, the character creation either progresses to the next screen
 * or completes, bringing to the sheet of the newly created character.
 * @param navController: NavController
 * @param viewModel: DndCharacterManagerViewModel
 */
@Composable
fun BuildCharacterStart(navController: NavController, viewModel: DndCharacterManagerViewModel) {
    val tag = "BuildCharacterStart"
    val context = LocalContext.current

    var characterName by remember { mutableStateOf("") }
    var characterRace by remember { mutableStateOf("") }
    var characterClass by remember { mutableStateOf("") }
    var characterImage by remember { mutableStateOf<Uri?>(null) }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            characterImage = uri
            Log.d(tag, "Selected URI: $uri")
            Toast.makeText(context, context.getString(R.string.character_image_added), Toast.LENGTH_SHORT).show()
        } else {
            Log.d(tag, "No media selected")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(LargePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {

            Text(text = stringResource(R.string.character_builder), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(SmallPadding))
            Text(text = stringResource(R.string.name), style = MaterialTheme.typography.bodyLarge)
            // text field for character name
            OutlinedTextField(
                modifier = Modifier.testTag("character_name_field"),
                value = characterName,
                onValueChange = { characterName = it },
                label = { Text(stringResource(R.string.character_name)) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true
            )
            // display character name
            Text(
                text = characterName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(SmallPadding),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.padding(SmallPadding))
            // option group for race
            OptionGroup(
                label = stringResource(R.string.race),
                // get races from race enum
                options = RaceEnum.entries.map { it.name },
                selectedOption = characterRace,
                onSelected = { characterRace = it }
            )
            Spacer(modifier = Modifier.padding(MediumPadding))
            // option group for class
            OptionGroup(
                label = stringResource(R.string.dndClass),
                options = DndClassEnum.entries.map { it.name },
                selectedOption = characterClass,
                onSelected = { characterClass = it }
            )
            Spacer(modifier = Modifier.padding(MediumPadding))
            // image picker button
            Button(onClick = {
                pickMedia.launch(PickVisualMediaRequest())
            }) {
                Text(text = stringResource(R.string.add_character_image))
            }
            CharacterImageCard(characterImage)
        }


        Button(
            modifier = Modifier.testTag("create_character_button"),
            onClick = {
                var characterImageStorage: Uri? = null
                if (characterImage != null){
                    Log.d(tag, "Trying to save image")
                    // save image to storage
                    characterImageStorage = viewModel.saveImage(characterImage!!, context)
                }
                val ch = viewModel.createCharacter(
                    name = characterName,
                    race = characterRace,
                    dndClass = characterClass,
                    image = characterImageStorage
                )
                if (ch != null) {
                    // if character is wizard go to new spells screen
                    if(ch.dndClass == DndClassEnum.WIZARD.dndClass){
                        navController.navigate("choose_spells/${ch.id}"){
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                } else {
                    Toast.makeText(context,
                        context.getString(R.string.error_creating_character), Toast.LENGTH_SHORT).show()
                }
            },
            enabled = characterName.isNotEmpty() &&
                        characterRace.isNotEmpty() &&
                        characterClass.isNotEmpty()
        ) {
            Text(text = stringResource(R.string.confirm))
        }
    }
}

@Composable
private fun CharacterImageCard(characterImage: Uri?) {
    var characterImage1 = characterImage
    characterImage1?.let {
        Card(modifier = Modifier.size(200.dp)) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = characterImage1,
                    contentDescription = stringResource(id = R.string.character_image),
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { characterImage1 = null },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete_button))
                }
            }
        }
    }
}

