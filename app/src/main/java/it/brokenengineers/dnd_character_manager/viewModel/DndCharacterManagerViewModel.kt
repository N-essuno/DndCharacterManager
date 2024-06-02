package it.brokenengineers.dnd_character_manager.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.classes.Weapon
import it.brokenengineers.dnd_character_manager.data.database.DndCharacterManagerDB
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import it.brokenengineers.dnd_character_manager.data.getMaxHpStatic
import it.brokenengineers.dnd_character_manager.data.initAbilityValuesForRace
import it.brokenengineers.dnd_character_manager.data.initProficienciesForClass
import it.brokenengineers.dnd_character_manager.data.initSpellSlotsForClass
import it.brokenengineers.dnd_character_manager.repository.DndCharacterManagerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DndCharacterManagerViewModel(db: DndCharacterManagerDB) : ViewModel()  {
    private var characterDao = db.characterDao()
    private var raceDao = db.raceDao()
    private var dndClassDao = db.dndClassDao()
    private var abilityDao = db.abilityDao()
    private var skillDao = db.skillDao()
    private var weaponDao = db.weaponDao()
    private var spellDao = db.spellDao()
    private val repository = DndCharacterManagerRepository(
        this,
        characterDao,
        raceDao,
        abilityDao,
        dndClassDao,
        skillDao,
        weaponDao,
        spellDao
    )
    var characters = repository.allCharacters
        private set
    var selectedCharacter = repository.selectedDndCharacter

    fun init() {
        repository.init()
    }

    fun fetchCharacterById(id: Int) {
        repository.getCharacterById(id)
    }

    fun fetchCharacterByName(name: String) {
        repository.fetchCharacterByName(name)
    }

    fun fetchAllCharacters() {
        repository.fetchAllCharacters()
    }

    fun createCharacter(name: String, race: String, dndClass: String, image: Uri?) {
        // TODO clean method after implementation of Spells passed from UI
        viewModelScope.launch {
            // convert to Race and DndClass
            val raceObj: Race = RaceEnum.valueOf(race.uppercase()).race
            val dndClassObj: DndClass = DndClassEnum.valueOf(dndClass.uppercase()).dndClass
            val newDndCharacter: DndCharacter?

            val abilityValues = initAbilityValuesForRace(raceObj)
            val spellSlots = initSpellSlotsForClass(dndClassObj)
            val proficiencies = initProficienciesForClass(dndClassObj)
            val maxHp = getMaxHpStatic(dndClassObj, 1, abilityValues)

            var weapon: Weapon? = null
            if (dndClassObj.name == DndClassEnum.BARBARIAN.dndClass.name) {
                weapon = Weapon(1, "Hammer", "1d12")
            }

//            val spell1: Spell = MockSpells.getSpellByName("Fireball")!!
//            val spell2: Spell = MockSpells.getSpellByName("Magic Missile")!!
//
//            val knownSpells: MutableSet<Spell> = mutableSetOf()
//            val preparedSpells: MutableSet<Spell> = mutableSetOf()
//
//            if (dndClassObj.name == DndClassEnum.WIZARD.dndClass.name) {
//                knownSpells.add(spell1)
//                knownSpells.add(spell2)
//                preparedSpells.add(spell1)
//            }

            newDndCharacter = DndCharacter(
                name = name,
                race = raceObj,
                raceId = raceObj.id,
                dndClass = dndClassObj,
                dndClassId = dndClassObj.id,
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
                weapon = weapon,
                weaponId = weapon?.id ?: 99
            )

            repository.insertCharacter(newDndCharacter)
        }
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
                repository.updateCharacter(newCharacter)
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
                repository.updateCharacter(newCharacter)
            }
        }
    }

    fun loseHp(hp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newRemainingHp = character.remainingHp - hp
                val newCharacter = character.copy(remainingHp = newRemainingHp)
                repository.updateCharacter(newCharacter)
            }
        }
    }

    fun addTempHp(tempHp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newTempHp = character.tempHp + tempHp
                val newCharacter = character.copy(tempHp = newTempHp)
                repository.updateCharacter(newCharacter)
            }
        }
    }

    fun loseTempHp(tempHp: Int) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newTempHp = character.tempHp - tempHp
                val newCharacter = character.copy(tempHp = newTempHp)
                repository.updateCharacter(newCharacter)
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
                repository.updateCharacter(newCharacter)
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
                newInventoryItems?.add(newItem)

                newInventoryItems = orderSetByName(newInventoryItems!!)

                val newCharacter = character.copy(inventoryItems = newInventoryItems)
                repository.updateCharacter(newCharacter)
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
                    newInventoryItems?.add(newItem)
                    newInventoryItems = orderSetByName(newInventoryItems!!)
                }
                val newCharacter = character.copy(inventoryItems = newInventoryItems)
                repository.updateCharacter(newCharacter)
            }
        }
    }

    fun addItemToInventory(name: String, quantity: String, weight: String) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                val newInventoryItems = character.inventoryItems?.toMutableSet()
                val newItem = InventoryItem(name = name, quantity = quantity.toInt(), weight = weight.toDouble())
                newInventoryItems?.add(newItem)
                val newCharacter = character.copy(inventoryItems = newInventoryItems)
                repository.updateCharacter(newCharacter)
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
                repository.updateCharacter(newCharacter)
            }
        }
    }

    private fun orderSetByName(items: MutableSet<InventoryItem>): MutableSet<InventoryItem> {
        val newList = items.toMutableList()
        newList.sortBy { it.name }
        return newList.toMutableSet()
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

    /**
     * Recover HP and spell slots after a short rest
     * @param slotsToRecover a list of the number of slots to recover for each spell level. If null,
     * there are either no slots to recover or the character is not a spellcaster
     * @return the updated character after the short rest
     */
    fun shortRest(slotsToRecover: List<Int>?) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                if (slotsToRecover != null) {
                    val oldSlots = character.availableSpellSlots
                    // convert slotsToRecover to map
                    val mapSlotsToRecover = slotsToRecover.mapIndexed { index, i -> index + 1 to i }.toMap()
                    // sum oldSlots and mapSlotsToRecover into newSlots
                    val newSlots = oldSlots?.mapValues {
                        it.value + mapSlotsToRecover.getOrDefault(it.key, 0)
                    }

                    // TODO spellSlots should be an object, otherwise dao update is not possible
//                    newSlots?.let {characterDao.updateAvailableSpellSlots(character.id, newSlots)}
                }
            }
        }
    }

    /**
     * Increase an ability score by a certain amount
     * @param character the character to level up
     * @param abilityEnum the ability score to increase
     * @param amount the amount to increase the ability score by
     */
    fun increaseAbilityScore(character: DndCharacter, abilityEnum: AbilityEnum, amount: Int) {
        viewModelScope.launch {
            val ability = abilityEnum.ability
            val newAbilityValues = character.abilityValues.toMutableMap()
            newAbilityValues[ability] = character.abilityValues[ability]!! + amount
            // TODO update character in database using repository
            // temporary update of character in repository
            val newCharacter = character.copy(abilityValues = newAbilityValues)
            repository.selectedDndCharacter.value = newCharacter
            updateCharactersList(character, newCharacter)
            // temporary update of character in repository
        }
    }

    fun increaseHpLevelUp(character: DndCharacter) {
        viewModelScope.launch {
            val newRemainingHp = getMaxHpStatic(
                dndClass = character.dndClass!!,
                level = character.level + 1,
                abilityValues = character.abilityValues
            )
            val newCharacter = character.copy(remainingHp = newRemainingHp)
            repository.selectedDndCharacter.value = newCharacter
            updateCharactersList(character, newCharacter)
            // TODO update character in database by repository
        }

    }

    fun increaseLevel(character: DndCharacter) {
        viewModelScope.launch {
            val newCharacter = character.copy(level = character.level + 1)
            repository.selectedDndCharacter.value = newCharacter
            updateCharactersList(character, newCharacter)
            // TODO update character in database by repository
        }
    }

    fun longRest(spellsToPrepare: List<Spell>?) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            var newCharacter: DndCharacter
            if (character != null) {
                // recover HP to max
                val newRemainingHp = getMaxHpStatic(
                    dndClass = character.dndClass!!,
                    level = character.level,
                    abilityValues = character.abilityValues
                )
                newCharacter = character.copy(remainingHp = newRemainingHp)
                // recover spell slots
                spellsToPrepare?.let {
                    newCharacter = character.copy(preparedSpells = spellsToPrepare.toSet())
                    Log.d("ViewModel", "New Prepared spells: ${newCharacter.preparedSpells}")
                }
                repository.selectedDndCharacter.value = newCharacter
                updateCharactersList(character, newCharacter)
                // TODO update character in database by repository
            }
        }
    }

    fun chooseArcaneTradition(character: DndCharacter, name: String) {
        viewModelScope.launch {
            // TODO character should have an arcane tradition field
//            val newCharacter = character.copy(arcaneTradition = name)
//            repository.selectedDndCharacter.value = newCharacter
//            updateCharactersList(character, newCharacter)
            // TODO update character in database by repository
        }
    }

    fun saveNewSpells(newSpells: List<Spell>) {
        viewModelScope.launch {
            val character = selectedCharacter.value
            if (character != null) {
                // get already known spells
                val totalSpellsKnown = character.spellsKnown?.toMutableSet()
                // add new spells passed from UI
//                totalSpellsKnown?.addAll(newSpells)
                val newCharacter = character.copy(spellsKnown = newSpells.toSet())
                updateCharactersList(character, newCharacter) // TODO move into repository
                repository.insertKnownSpells(newCharacter)
            }
        }

    }

    /**
     * Save the image in local storage
     * @param characterImage the Uri of the image to save, it is a temporary file
     * @param context the context of the activity
     * @return the Uri of the saved image
     */
    fun saveImage(characterImage: Uri, context: Context): Uri? {
        val fileName = "character_image${System.currentTimeMillis()}"
        var success = false
        viewModelScope.launch {
            val bitmap = getBitmapFromUri(characterImage, context)
            saveBitmapToLocalFile(fileName, bitmap, context)
            success = true
        }
        return if (success) Uri.parse("file://${context.filesDir}/$fileName.png") else null
    }

    private suspend fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("ViewModel", "Error reading bitmap from uri", e)
            null
        }
    }

    private suspend fun saveBitmapToLocalFile(filename: String, bitmap: Bitmap?, context: Context) = withContext(Dispatchers.IO) {
        try {
            context.openFileOutput("$filename.png", Context.MODE_PRIVATE).use { fos ->
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
        } catch (e: IOException) {
            Log.e("ViewModel", "Error saving bitmap to file", e)
        }
    }

    fun clearSelectedCharacter() {
        repository.clearSelectedCharacter()
    }
}