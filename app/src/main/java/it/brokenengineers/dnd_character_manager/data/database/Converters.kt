package it.brokenengineers.dnd_character_manager.data.database

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.AbilityScoreIncrease
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem

class Converters {
    val tag: String = Converters::class.java.simpleName

    private val gson = GsonBuilder()
        .registerTypeAdapter(Ability::class.java, AbilityTypeAdapter())
        .create()

    @TypeConverter
    fun fromAbilityMap(abilityMap: Map<Ability, Int>): String {
        // convert map to json string
        val abilityNamesMap: Map<String, Int> = abilityMap.mapKeys { it.key.name }
        return gson.toJson(abilityNamesMap)
    }

    @TypeConverter
    fun toAbilityMap(abilityMapString: String): Map<Ability, Int> {
        val type = object : TypeToken<Map<Ability, Int>>() {}.type
        return gson.fromJson(abilityMapString, type)
    }

    @TypeConverter
    fun fromSpellSlotsMap(spellSlotsMap: Map<Int, Int>?): String {
        return gson.toJson(spellSlotsMap)
    }

    @TypeConverter
    fun toSpellSlotsMap(spellSlotsMapString: String): Map<Int, Int>? {
        val type = object : TypeToken<Map<Int, Int>?>() {}.type
        return gson.fromJson(spellSlotsMapString, type)
    }

    @TypeConverter
    fun fromInventorySet(inventorySet: Set<InventoryItem>?): String {
        return gson.toJson(inventorySet)
    }

    @TypeConverter
    fun toInventorySet(inventorySetString: String): Set<InventoryItem>? {
        val type = object : TypeToken<Set<InventoryItem>?>() {}.type
        return gson.fromJson(inventorySetString, type)
    }

    @TypeConverter
    fun fromAbilityScoreIncrease(abilityScoreIncrease: List<AbilityScoreIncrease>): String {
        return gson.toJson(abilityScoreIncrease)
    }

    @TypeConverter
    fun toAbilityScoreIncrease(abilityScoreIncreaseString: String): List<AbilityScoreIncrease> {
        val type = object : TypeToken<List<AbilityScoreIncrease>>() {}.type
        return gson.fromJson(abilityScoreIncreaseString, type)
    }
}