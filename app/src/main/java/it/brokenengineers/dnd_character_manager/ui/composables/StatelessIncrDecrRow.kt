package it.brokenengineers.dnd_character_manager.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.viewModel.TestTags

@Composable
fun StatelessIncrDecrRow(
    label: String,
    timesSelected: Int,
    onAdd: () -> Unit,
    enabledAddCondition: () -> Boolean,
    onRemove: () -> Unit,
    enabledRemoveCondition: () -> Boolean
) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            text = label
        )
        Spacer(modifier = Modifier.weight(1f))

        // ADD BUTTON
        IconButton(
            modifier = Modifier.weight(0.2f).testTag(TestTags.ADD_ITEM_BUTTON),
            onClick = onAdd,
            enabled = enabledAddCondition()
        ){
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                contentDescription = "Add"
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // REMOVE BUTTON
        IconButton(
            modifier = Modifier.weight(0.2f),
            onClick = onRemove,
            enabled = enabledRemoveCondition()
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_delete),
                contentDescription = "Remove"
            )
        }

        // TIMES SELECTED
        Text(
            modifier = Modifier
                .weight(0.4f)
                .padding(SmallPadding),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.secondary
            ),
            text = timesSelected.toString()
        )
    }
}