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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.viewModel.TestTags
import it.brokenengineers.dnd_character_manager.viewModel.TestTags.STAT_INCREASE_OLD_VAL

@Composable
fun StatIncrease(statName: String, oldValue: Int, newValue: Int) {
    val statNameString = stringResource(R.string.stat_upgrade, statName)
    Row {
        Text(
            text = statNameString,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.testTag("${TestTags.STAT_INCREASE_NAME}_$statName")
        )
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = oldValue.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.testTag("${statName}$STAT_INCREASE_OLD_VAL")
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(R.string.stat_upgrade, statName),
            modifier = Modifier.padding(horizontal = SmallPadding)
        )
        Text(
            text = newValue.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.testTag("${statName}${TestTags.STAT_INCREASE_NEW_VAL}")
        )
    }
}