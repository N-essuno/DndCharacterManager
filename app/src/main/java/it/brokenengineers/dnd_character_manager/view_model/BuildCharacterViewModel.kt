package it.brokenengineers.dnd_character_manager.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel

class BuildCharacterViewModel: ViewModel(){
    private var characterName: String = ""
    private var characterRace: String = ""
    private var characterClass: String = ""
    private var characterImage: Uri? = null

    fun setCharacterName(characterName: String) {
        this.characterName = characterName
    }

    fun setCharacterRace(characterRace: String) {
        this.characterRace = characterRace
    }

    fun setCharacterClass(characterClass: String) {
        this.characterClass = characterClass
    }

    fun setCharacterImage(it: Uri) {
        this.characterImage = it
    }
}