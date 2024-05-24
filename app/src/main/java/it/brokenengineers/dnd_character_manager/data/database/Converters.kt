package it.brokenengineers.dnd_character_manager.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.InventoryItem
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.classes.Skill
import it.brokenengineers.dnd_character_manager.data.classes.Spell
import it.brokenengineers.dnd_character_manager.data.classes.Weapon

class Converters {
    private val gson = GsonBuilder()
        .registerTypeAdapter(Ability::class.java, AbilityTypeAdapter())
        .create()

    @TypeConverter
    fun fromRace(race: Race): String {
        return gson.toJson(race)
    }

    @TypeConverter
    fun toRace(raceString: String): Race {
        val type = object : TypeToken<Race>() {}.type
        return gson.fromJson(raceString, type)
    }

    @TypeConverter
    fun fromDndClass(dndClass: DndClass): String {
        return gson.toJson(dndClass)
    }

    @TypeConverter
    fun toDndClass(dndClassString: String): DndClass {
        val type = object : TypeToken<DndClass>() {}.type
        return gson.fromJson(dndClassString, type)
    }

    @TypeConverter
    fun fromAbilityMap(abilityMap: Map<Ability, Int>): String {
        // convert map to json string
        val abilityNamesMap: Map<String, Int> = abilityMap.mapKeys { it.key.name }
        return gson.toJson(abilityNamesMap)
    }

    @TypeConverter
    fun toAbilityMap(abilityMapString: String): Map<Ability, Int> {
        val type = object : TypeToken<Map<Ability, Int>>() {}.type
        val abilityNamesMap: Map<String, Int> = gson.fromJson(abilityMapString, type)
        // convert string map to Ability map
        return abilityNamesMap.mapKeys { Ability(it.key) }
    }

    @TypeConverter
    fun fromSkillSet(skillSet: Set<Skill>): String {
        return gson.toJson(skillSet)
    }

    @TypeConverter
    fun toSkillSet(skillSetString: String): Set<Skill> {
        val type = object : TypeToken<Set<Skill>>() {}.type
        return gson.fromJson(skillSetString, type)
    }

    @TypeConverter
    fun fromSpellSet(spellSet: Set<Spell>?): String {
        return gson.toJson(spellSet)
    }

    @TypeConverter
    fun toSpellSet(spellSetString: String): Set<Spell>? {
        val type = object : TypeToken<Set<Spell>?>() {}.type
        return gson.fromJson(spellSetString, type)
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
    fun fromWeapon(weapon: Weapon?): String {
        return gson.toJson(weapon)
    }

    @TypeConverter
    fun toWeapon(weaponString: String): Weapon? {
        val type = object : TypeToken<Weapon?>() {}.type
        return gson.fromJson(weaponString, type)
    }
}