package it.brokenengineers.dnd_character_manager.screens.levelup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import it.brokenengineers.dnd_character_manager.ui.composables.CharacterCard
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
        item { ArcaneTradition() }
        item { AbilityScoreImprovement() }
        item { NewFeatures() }
    }
}