package it.brokenengineers.dnd_character_manager.data.database

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import it.brokenengineers.dnd_character_manager.data.classes.Ability

class AbilityTypeAdapter : TypeAdapter<Ability>() {
    private val tag: String = AbilityTypeAdapter::class.java.simpleName

    override fun write(out: JsonWriter, ability: Ability) {
        out.beginObject()
        out.name("name").value(ability.name) // name: "Gaheris"
        out.endObject()
    }

    override fun read(input: JsonReader): Ability {
        var name = ""
        val token = input.peek() // check next token type
        if (token == JsonToken.STRING) {
            name = input.nextString()
                .removePrefix("Ability(name=")
                .removeSuffix(")")
        } else if (token == JsonToken.BEGIN_OBJECT) {
            input.beginObject()
            while (input.hasNext()) {
                if (input.nextName() == "name") {
                    name = input.nextString()
                }
            }
            input.endObject()
        }
        return Ability(name)
    }
}