package it.brokenengineers.dnd_character_manager.ui.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

/**
 * A group of selectable options
 * @param label The label of the group
 * @param options The list of options to be displayed
 * @param selectedOption The currently selected option in the group
 * @param onSelected The callback to be called when an option is selected, should update the
 * selectedOption to the option label
 */
@Composable
fun OptionGroup(
    label: String,
    options: List<String>,
    selectedOption: String,
    onSelected: (String) -> Unit,
    testTags: Boolean? = false
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.padding(SmallPadding))
        Row {
            options.forEach { option ->
                Option(option, selectedOption, onSelected, testTags)
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
fun Option(
    label: String,
    selectedOption: String,
    onSelected: (String) -> Unit,
    testTags: Boolean? = false
) {
    Surface(
        color = if (label == selectedOption) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
        contentColor = if (label == selectedOption) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onTertiary,
        modifier = Modifier
            .clickable { onSelected(label) }
            .testTag(testTags?.let { "option_$label" } ?: "")
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(SmallPadding)
        )
    }
}