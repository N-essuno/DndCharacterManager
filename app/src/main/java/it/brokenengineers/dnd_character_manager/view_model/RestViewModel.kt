package it.brokenengineers.dnd_character_manager.view_model

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.brokenengineers.dnd_character_manager.screens.Spell
import kotlinx.coroutines.launch

class RestViewModel : ViewModel() {
    // get name of class for logging
    private val tag = RestViewModel::class.simpleName
    // spellsToRecover for Short Rest
    val spellsToRecover = Array(9) { mutableIntStateOf(0) }
    // spellsToPrepare for Long Rest
    private val spellsToPrepare = mutableListOf<Spell>()

    // data from character, TODO should be retrieved from data model based on character selected
    // should be retrieved from data model based on character selected
    val spellsKnown = listOf(Spell("Magic1", 1), Spell("Magic2", 2), Spell("Magic3", 3), Spell("Magic4", 4))
    // slots available for Short Rest
    var slotsAvailable = 4

    init {
        initSelectedSpells()
    }

    fun prepareSpell(spell: Spell) {
        viewModelScope.launch {
            spellsToPrepare.add(spell)
            Log.d(tag, "Prepare spell: $spell. Spell list size: ${spellsToPrepare.size}")
        }
    }

    fun undoPrepareSpell(spell: Spell) {
        viewModelScope.launch {
            spellsToPrepare.remove(spell)
        }
    }

    fun recoverSpellSlot(spellLevel: Int) {
        val spellIndex = spellLevel - 1
        viewModelScope.launch {
            val currentVal = spellsToRecover[spellIndex].intValue
            spellsToRecover[spellIndex].intValue = currentVal + 1

            slotsAvailable -= 1
        }
    }

    /**
     * Remove a spell from the selected spells
     * @param spellLevel the level of the spell to remove
     */
    fun undoRecoverSpellSlot(spellLevel: Int) {
        val spellIndex = spellLevel - 1
        viewModelScope.launch {
            val currentVal = spellsToRecover[spellIndex].intValue
            spellsToRecover[spellIndex].intValue = currentVal - 1

            slotsAvailable += 1
        }
    }

    fun isSpellPrepared(spell: Spell): Boolean {
        return spellsToPrepare.contains(spell)
    }

    private fun initSelectedSpells() {
        for (i in 0..8) {
            spellsToRecover[i] = mutableIntStateOf(0)
        }
    }
}
