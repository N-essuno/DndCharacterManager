package it.brokenengineers.dnd_character_manager.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import it.brokenengineers.dnd_character_manager.data.Character
import it.brokenengineers.dnd_character_manager.data.DndClass
import it.brokenengineers.dnd_character_manager.data.Race
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import java.util.Locale

class BuildCharacterViewModel: ViewModel(){
    private lateinit var characterName: String
    private lateinit var characterRace: Race
    private lateinit var characterClass: DndClass
    private var characterImage: Uri? = null
    private var character: Character? = null

    fun setCharacterName(characterName: String) {
        this.characterName = characterName
    }

    fun setCharacterRace(characterRace: String) {
        this.characterRace = RaceEnum.valueOf(characterRace.uppercase()).race
    }

    fun setCharacterClass(characterClass: String) {
        this.characterClass = DndClassEnum.valueOf(characterClass.uppercase()).dndClass
    }

    fun setCharacterImage(it: Uri) {
        this.characterImage = it
    }

    fun createCharacter() {

    }
}