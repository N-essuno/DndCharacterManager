package it.brokenengineers.dnd_character_manager.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.data.Character
import it.brokenengineers.dnd_character_manager.data.InventoryItem
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
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun addHp(hp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newRemainingHp = character.remainingHp + hp
                val newCharacter = character.copy(remainingHp = newRemainingHp)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun loseHp(hp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newRemainingHp = character.remainingHp - hp
                val newCharacter = character.copy(remainingHp = newRemainingHp)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun addTempHp(tempHp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newTempHp = character.tempHp + tempHp
                val newCharacter = character.copy(tempHp = newTempHp)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun loseTempHp(tempHp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newTempHp = character.tempHp - tempHp
                val newCharacter = character.copy(tempHp = newTempHp)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun deleteInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newInventoryItems = character.inventoryItems?.toMutableSet()
                newInventoryItems?.remove(item)
                val newCharacter = character.copy(inventoryItems = newInventoryItems)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun incrementInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                var newInventoryItems = character.inventoryItems?.toMutableSet()
                newInventoryItems?.remove(item)

                val newItem = item.copy(quantity = item.quantity + 1)
                newInventoryItems?.add(newItem) // TODO check if the id is managed properly in DB

                newInventoryItems = orderSetById(newInventoryItems!!)

                val newCharacter = character.copy(inventoryItems = newInventoryItems)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun decrementInventoryItem(item: InventoryItem) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                var newInventoryItems = character.inventoryItems?.toMutableSet()
                newInventoryItems?.remove(item)
                val newItem = item.copy(quantity = item.quantity - 1)
                if (newItem.quantity > 0) {
                    newInventoryItems?.add(newItem) // TODO check if the id is managed properly in DB
                    newInventoryItems = orderSetById(newInventoryItems!!)
                }
                val newCharacter = character.copy(inventoryItems = newInventoryItems)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun addItemToInventory(name: String, quantity: String, weight: String) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newInventoryItems = character.inventoryItems?.toMutableSet()
                val newItem = InventoryItem(id = 99, name = name, quantity = quantity.toInt(), weight = weight.toDouble())
                newInventoryItems?.add(newItem) // TODO check if the id is managed properly in DB
                val newCharacter = character.copy(inventoryItems = newInventoryItems)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun useSpellSlot(spellLevel: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newSpellSlots = character.availableSpellSlots?.toMutableMap()
                val newSpellSlotValue = newSpellSlots?.get(spellLevel)!! - 1
                newSpellSlots[spellLevel] = newSpellSlotValue
                val newCharacter = character.copy(availableSpellSlots = newSpellSlots)
                repository.selectedCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    private fun orderSetById(inventoryItems: MutableSet<InventoryItem>): MutableSet<InventoryItem> {
        val newInventoryItemsList = inventoryItems.toMutableList()
        newInventoryItemsList.sortBy { it.id }
        return newInventoryItemsList.toMutableSet()
    }

    // TODO to remove after DB implementation, used just for testing
    fun updateCharactersList(oldCharacter: Character, newCharacter: Character) {
        viewModelScope.launch {
            val newChars = repository.allCharacters.value.toMutableList()
            newChars.remove(oldCharacter)
            newChars.add(newCharacter)
            repository.allCharacters.value = newChars
        }
    }
}