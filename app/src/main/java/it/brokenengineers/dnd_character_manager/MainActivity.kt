package it.brokenengineers.dnd_character_manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.brokenengineers.dnd_character_manager.ui.theme.DndCharacterManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DndCharacterManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
                    HomePage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){// body large text
        Text("Welcome back adventurer!",
            style = MaterialTheme.typography.titleLarge)

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(3){
                CharacterCard()
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { /*TODO*/ }
        ) {
            Text("Create new character")
        }
    }

}

@Composable
fun CharacterCard(){
    Card{
        Column{
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Character Image"
            )
            Text("Character Name")
            Text("Character Class")
            Text("Character Level")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DndCharacterManagerTheme {
        HomePage(Modifier)
    }
}