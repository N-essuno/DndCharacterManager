package it.brokenengineers.dnd_character_manager.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import it.brokenengineers.dnd_character_manager.data.Character
import it.brokenengineers.dnd_character_manager.data.DndClass
import it.brokenengineers.dnd_character_manager.data.Race
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import kotlinx.coroutines.launch
import java.util.Locale

class BuildCharacterViewModel: ViewModel(){
    private var characterImage: Uri? = null
    private var character: Character? = null

    fun createCharacter(name: String, race: String, dndClass: String, image: Uri?): Character{
        // convert to Race and DndClass
        val raceObj = RaceEnum.valueOf(race.uppercase()).race
        val dndClassObj = DndClassEnum.valueOf(dndClass.uppercase()).dndClass
        viewModelScope.launch {
            // get other character parameters based on user input
        }
        // TODO: fix after merge
        return Character(name, dndClassObj, raceObj, characterImage)
    }
}