package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
import it.brokenengineers.dnd_character_manager.ui.theme.SmallPadding
import it.brokenengineers.dnd_character_manager.ui.theme.XXLPadding

@Composable
fun LevelUp(){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = XXLPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        item {CharacterCard()}
        item {Text(text = "Level Up!", style = MaterialTheme.typography.titleLarge) }
        // hp upgrade from previous to current level
        item { HPUpgrade(10, 12)}
//        item { NewMagic() }
//        item { ArcaneTradition() }
//        item { AbilityScoreImprovement() }
//        item { NewFeatures() }
    }
}

@Composable
fun HPUpgrade(previousHP: Int, newHP: Int) {
    Row {
        Text("HP Upgrade", style = MaterialTheme.typography.titleMedium)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = previousHP.toString(), style = MaterialTheme.typography.bodyLarge)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "HP upgrade",
            modifier = Modifier.padding(horizontal = SmallPadding)
        )
        Text(text = newHP.toString(), style = MaterialTheme.typography.bodyLarge)
    }
}

