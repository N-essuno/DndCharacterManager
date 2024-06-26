package it.brokenengineers.dnd_character_manager.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun ExpandableCard(
    title: String, description:
    String, selected: Boolean,
    onSelected: () -> Unit,
    testTag: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(SmallPadding),
        elevation =
        if (expanded) CardDefaults.elevatedCardElevation() // Increase elevation when expanded
        else CardDefaults.cardElevation(), // Default elevation when collapsed
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor =
            if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.tertiary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TITLE
            Text(text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(SmallPadding).weight(1f)
            )
            // EXPAND/COLLAPSE ICON
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Expand or collapse",
                modifier = Modifier.
                    rotate(if (expanded) 180f else 0f).
                    weight(0.1f).
                    padding(end = SmallPadding)
            )
            // SELECT/DESELECT BUTTON
            IconButton(
                onClick = onSelected,
                modifier = Modifier.weight(0.1f).padding(end = SmallPadding).testTag(testTag)
            ) {
                // show a plus icon
                Icon(
                    painter = painterResource(id =
                        if (selected) android.R.drawable.ic_input_delete
                        else android.R.drawable.ic_input_add
                    ),
                    contentDescription = "Select",
                    tint =
                        if (selected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Show the description if the card is expanded
        if (expanded) {
            Text(text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(SmallPadding))
        }
    }
}