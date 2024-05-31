package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.brokenengineers.dnd_character_manager.R
import it.brokenengineers.dnd_character_manager.data.classes.Feature
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding

@Composable
fun NewFeatures(featureList: List<Feature>) {
    Spacer(modifier = Modifier.padding(SmallPadding))
    Text(
        text = stringResource(R.string.features_unlocked),
        style = MaterialTheme.typography.titleMedium
    )
    // show a card with the ability
    featureList.forEach {
        FeatureCard(it)
    }
}

@Composable
fun FeatureCard(feature: Feature) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SmallPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            Text(
                text = feature.featureName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}