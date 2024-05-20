package it.brokenengineers.dnd_character_manager.viewModel

import androidx.lifecycle.ViewModel
import it.brokenengineers.dnd_character_manager.repository.DndCharacterManagerRepository

class DndCharacterManagerViewModel(/* TODO add Database */) : ViewModel()  {
    private val repository = DndCharacterManagerRepository(this /* TODO add Database */)
    var characters = repository.allCharacters
        private set
    var selectedCharacter = repository.selectedCharacter

    fun init() {
        repository.init()
    }

    fun fetchCharacterById(id: Int) {
        repository.getCharacterById(id)
    }
}