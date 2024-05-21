package it.brokenengineers.dnd_character_manager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.repository.DndCharacterManagerRepository
import kotlinx.coroutines.launch

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

    fun addHit(hitValue: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                var newCharacter = character
                if (character.tempHp >= hitValue) {
                    val newTempHp = character.tempHp - hitValue
                    newCharacter = character.copy(tempHp = newTempHp)
                } else {
                    val newHitValue = hitValue - character.tempHp
                    val newTempHp = 0
                    val newRemainHp = character.remainingHp - newHitValue
                    newCharacter = character.copy(tempHp = newTempHp, remainingHp = newRemainHp)
                }
                repository.selectedCharacter.value = newCharacter
                // TODO update character in database by repository
            }
        }
    }

    fun addHp(hp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newRemainingHp = character.remainingHp + hp
                repository.selectedCharacter.value = character.copy(remainingHp = newRemainingHp)
                // TODO update character in database by repository
            }
        }
    }

    fun loseHp(hp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newRemainingHp = character.remainingHp - hp
                repository.selectedCharacter.value = character.copy(remainingHp = newRemainingHp)
                // TODO update character in database by repository
            }
        }
    }

    fun addTempHp(tempHp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newTempHp = character.tempHp + tempHp
                repository.selectedCharacter.value = character.copy(tempHp = newTempHp)
                // TODO update character in database by repository
            }
        }
    }

    fun loseTempHp(tempHp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newTempHp = character.tempHp - tempHp
                repository.selectedCharacter.value = character.copy(tempHp = newTempHp)
                // TODO update character in database by repository
            }
        }
    }
}