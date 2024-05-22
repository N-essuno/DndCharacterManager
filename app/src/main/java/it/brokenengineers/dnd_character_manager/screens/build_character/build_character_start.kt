package it.brokenengineers.dnd_character_manager.screens.build_character

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.ui.theme.LargePadding
import it.brokenengineers.dnd_character_manager.ui.theme.MediumPadding
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.view_model.BuildCharacterViewModel

@Composable
fun BuildCharacterStart(navController: NavController) {
    val context = LocalContext.current
    val viewModel = BuildCharacterViewModel()

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
            Log.d("ImagePicker", "Selected URI: $uri")
            Toast.makeText(context, "Character image added", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("ImagePicker", "No media selected")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(LargePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {

            Text(text = "Character Builder", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(SmallPadding))
            Text(text = "Name", style = MaterialTheme.typography.bodyLarge)
            // text field for character name
            OutlinedTextField(
                value = characterName,
                onValueChange = { characterName = it },
                label = { Text("Character Name") },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true
            )
            // display character name
            Text(
                text = characterName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(SmallPadding),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.padding(SmallPadding))
            // option group for race
            OptionGroup(
                label = "Race",
                // get races from race enum
                options = RaceEnum.entries.map { it.name },
                selectedOption = characterRace,
                onSelected = { characterRace = it }
            )
            Spacer(modifier = Modifier.padding(MediumPadding))
            // option group for class
            OptionGroup(
                label = "Class",
                options = DndClassEnum.entries.map { it.name },
                selectedOption = characterClass,
                onSelected = { characterClass = it }
            )
            Spacer(modifier = Modifier.padding(MediumPadding))
            // image picker button
            Button(onClick = {
                pickMedia.launch(PickVisualMediaRequest())
            }) {
                Text(text = "Add character image")
            }
            CharacterImageCard(characterImage)
        }


        Button(
            onClick = {
//                // TODO instead of setting each property, create character directly
//                val ch = viewModel.createCharacter(
//                    name = characterName,
//                    race = characterRace,
//                    dndClass = characterClass,
//                    image = characterImage
//                )
//                navController.navigate("sheet/${ch.id}")
            },
            enabled = characterName.isNotEmpty() &&
                        characterRace.isNotEmpty() &&
                        characterClass.isNotEmpty()
        ) {
            Text(text = "Confirm")
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
                    contentDescription = "Character Image",
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { characterImage1 = null },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Button")
                }
            }
        }
    }
}

/**
 * A group of selectable options
 * @param label The label of the group
 * @param options The list of options to be displayed
 * @param selectedOption The currently selected option in the group
 * @param onSelected The callback to be called when an option is selected, should update the
 * selectedOption to the option label
 */
@Composable
fun OptionGroup(label: String, options: List<String>, selectedOption: String, onSelected: (String) -> Unit) {
    Column {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.padding(SmallPadding))
        Row {
            options.forEach { option ->
                Option(option, selectedOption, onSelected)
                Spacer(modifier = Modifier.width(SmallPadding))
            }
        }
    }
}

/**
 * A selectable option, belonging to a group of options
 * @param label The label of the option
 * @param selectedOption The currently selected option in the group
 * @param onSelected The callback to be called when the option is selected, should update the
 * selectedOption to the option label
 */
@Composable
fun Option(label: String, selectedOption: String, onSelected: (String) -> Unit) {
    Surface(
        color = if (label == selectedOption) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
        contentColor = if (label == selectedOption) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary,
        modifier = Modifier.clickable { onSelected(label) }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(SmallPadding)
        )
    }
}