package it.brokenengineers.dnd_character_manager.data

data class InventoryItem (
    val id: Int,
    val name: String,
    val quantity: Int,
    val weight: Double,
)