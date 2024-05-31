package it.brokenengineers.dnd_character_manager.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun StatIncrease(statName: String, currentValue: Int, newValue: Int) {
    Row {
        Text(stringResource(R.string.stat_upgrade, statName), style = MaterialTheme.typography.titleMedium)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = currentValue.toString(), style = MaterialTheme.typography.bodyLarge)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(R.string.stat_upgrade, statName),
            modifier = Modifier.padding(horizontal = SmallPadding)
        )
        Text(text = newValue.toString(), style = MaterialTheme.typography.bodyLarge)
    }
}