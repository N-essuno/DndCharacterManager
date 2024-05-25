package it.brokenengineers.dnd_character_manager.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.database.DndCharacter
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.data.initAbilityValuesForRace
import it.brokenengineers.dnd_character_manager.data.initProficienciesForClass
import it.brokenengineers.dnd_character_manager.data.initSpellSlotsForClass
import it.brokenengineers.dnd_character_manager.repository.DndCharacterManagerRepository
import kotlinx.coroutines.launch

class DndCharacterManagerViewModel(db: DndCharacterManagerDB) : ViewModel()  {
    private var characterDao = db.characterDao()
    private val repository = DndCharacterManagerRepository(this, characterDao)
    var characters = repository.allCharacters
        private set
    var selectedCharacter = repository.selectedDndCharacter

    fun init() {
        repository.init()
    }

    fun fetchCharacterById(id: Int) {
        repository.getCharacterById(id)
    }

    fun createCharacter(name: String, race: String, dndClass: String, image: Uri?): DndCharacter?{
        // convert to Race and DndClass
        val raceObj: Race
        val dndClassObj: DndClass
        try{
            raceObj = RaceEnum.valueOf(race.uppercase()).race
            dndClassObj = DndClassEnum.valueOf(dndClass.uppercase()).dndClass
        } catch (e: IllegalArgumentException){
            // IllegalArgument exception is thrown if the string is not a valid enum value
            return null
        }
        var newDndCharacter: DndCharacter? = null
        viewModelScope.launch {
            val abilityValues = initAbilityValuesForRace(raceObj)
            val spellSlots = initSpellSlotsForClass(dndClassObj)
            val proficiencies = initProficienciesForClass(dndClassObj)
            val maxHp = getMaxHpStatic(dndClassObj, 1, abilityValues)

            newDndCharacter = DndCharacter(
                name = name,
                race = raceObj,
                dndClass = dndClassObj,
                image = image?.toString(),
                level = 1,
                abilityValues = abilityValues,
                skillProficiencies = proficiencies,
                remainingHp = maxHp,
                tempHp = 0,
                spellsKnown = emptySet(),
                preparedSpells = emptySet(),
                availableSpellSlots = spellSlots,
                inventoryItems = emptySet(),
                weapon = null
            )
            newDndCharacter?.let { repository.addCharacter(newDndCharacter!!) }
        }
        return newDndCharacter
    }

    fun addHit(hitValue: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newCharacter: DndCharacter?
                if (character.tempHp >= hitValue) {
                    val newTempHp = character.tempHp - hitValue
                    newCharacter = character.copy(tempHp = newTempHp)
                } else {
                    val newHitValue = hitValue - character.tempHp
                    val newTempHp = 0
                    val newRemainHp = character.remainingHp - newHitValue
                    newCharacter = character.copy(tempHp = newTempHp, remainingHp = newRemainHp)
                }
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
                repository.selectedDndCharacter.value = newCharacter
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
    fun updateCharactersList(oldDndCharacter: DndCharacter, newDndCharacter: DndCharacter) {
        viewModelScope.launch {
            val newChars = repository.allCharacters.value.toMutableList()
            newChars.remove(oldDndCharacter)
            newChars.add(newDndCharacter)
            repository.allCharacters.value = newChars
        }
    }
}