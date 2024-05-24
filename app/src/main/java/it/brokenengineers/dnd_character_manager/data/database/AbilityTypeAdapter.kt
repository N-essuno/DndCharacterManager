package it.brokenengineers.dnd_character_manager.data.database

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.google.gson.TypeAdapter
import it.brokenengineers.dnd_character_manager.data.classes.Ability

class AbilityTypeAdapter : TypeAdapter<Ability>() {
    override fun write(out: JsonWriter, ability: Ability) {
        out.beginObject()
        out.name("name").value(ability.name)
        out.endObject()
    }

    override fun read(input: JsonReader): Ability {
        val name = input.nextString()
            .removePrefix("Ability(name=")
            .removeSuffix(")")
        return Ability(name)
    }
}