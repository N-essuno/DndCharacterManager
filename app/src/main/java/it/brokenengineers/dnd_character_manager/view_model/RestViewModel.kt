package it.brokenengineers.dnd_character_manager.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RestViewModel : ViewModel() {
    // selected spells contains maps each spell level (9 in total) to the number of spells
    // chosen for that level
    val selectedSpells = mutableMapOf<Int, MutableState<Int>>()
    var slotsAvailable = 4

    init {
        initSelectedSpells()
    }


    fun addSpell(spellLevel: Int) {
        viewModelScope.launch {
            val currentVal = selectedSpells[spellLevel]?.value ?: 0
            selectedSpells[spellLevel]?.value = currentVal + 1

            slotsAvailable -= 1
        }
    }

    /**
     * Remove a spell from the selected spells
     * @param spellLevel the level of the spell to remove
     */
    fun removeSpell(spellLevel: Int) {
        viewModelScope.launch {
            val currentVal = selectedSpells[spellLevel]?.value ?: 0
            selectedSpells[spellLevel]?.value = currentVal - 1

            slotsAvailable += 1
        }
    }

    private fun initSelectedSpells() {
        for (i in 0..9) {
            selectedSpells[i] = mutableIntStateOf(0)
        }
    }
}
