package it.brokenengineers.dnd_character_manager.data.enums

import it.brokenengineers.dnd_character_manager.data.classes.Spell

object MockSpells {
    private val spellList = listOf(
        Spell(
            id = 1,
            name = "Fireball",
            description = "A bright streak flashes from your pointing finger to a point you choose within range and then blossoms with a low roar into an explosion of flame.",
            level = 3,
            school = "Evocation"
        ),
        Spell(
            id = 2,
            name = "Magic Missile",
            description = "A missile of magical energy darts forth from your fingertip and strikes its target, dealing 1d4+1 force damage.",
            level = 1,
            school = "Evocation"
        ),
        Spell(
            id = 3,
            name = "Lightning Bolt",
            description = "A stroke of lightning forming a line 100 feet long and 5 feet wide blasts out from you in a direction you choose.",
            level = 3,
            school = "Evocation"
        ),
        Spell(
            id = 4,
            name = "Cure Wounds",
            description = "A creature you touch regains a number of hit points equal to 1d8 + your spellcasting ability modifier.",
            level = 1,
            school = "Evocation"
        ),
        Spell(
            id = 5,
            name = "Cause Fear",
            description = "You awaken the sense of mortality in one creature you can see within range. A construct or an undead is immune to this effect.",
            level = 1,
            school = "Necromancy"
        ),
        Spell(
            id = 6,
            name = "Wither and Bloom",
            description = "You invoke both death and life upon a 10-foot-radius sphere centered on a point within range. Each creature of your choice in that area must make a Constitution saving throw, taking 2d6 necrotic damage on a failed save, or half as much damage on a successful one.",
            level = 2,
            school = "Necromancy"
        ),
        Spell(
            id = 7,
            name = "Trabacca's Touch",
            description = "Trabacca was a powerful necromancer who was able to control the life force of others. You can use this spell to drain the life force of a creature you touch.",
            level = 1,
            school = "Necromancy"
        ),
        Spell(
            id = 8,
            name = "Granny descent",
            description = "You summon the spirit of an old lady who will descend upon your enemies, scaring them to death.",
            level = 1,
            school = "Evocation"
        ),
        Spell(
            id = 9,
            name = "Einar's Embrace",
            description = "You summon the spirit of Einar, the god of the sea, who will embrace your enemies in a watery prison.",
            level = 1,
            school = "Evocation"
        ),
        Spell(
            id = 10,
            name = "Where's the pump?",
            description = "You summon Edoardo's pump that will suck the life force out of your enemies.",
            level = 1,
            school = "Evocation"
        ),
        Spell(
            id = 11,
            name = "Ocru Puntu",
            description = "You summon a sardinan sheep that will blind your enemies spitting on them",
            level = 1,
            school = "Evocation"
        ),
        Spell(
            id = 12,
            name = "Noman's cigar",
            description = "A gargantuan cigar will rise from the ground and burn your enemies to ashes.",
            level = 2,
            school = "Evocation"
        ),
        Spell(
            id = 13,
            name = "Saba's Disgusting flatbread",
            description = "You summon a flatbread that will disgust your enemies to death.",
            level = 2,
            school = "Evocation"
        ),
        Spell(
            id = 14,
            name = "It clogged on its own",
            description = "You tell a series of senseless excuses from your flatmate. Deals 2d10 psychic damage.",
            level = 2,
            school = "Illusion"
        ),
    ) // TODO should be a list retrieved from viewModel, containing spells that the character can learn


    fun getSpellByName(name: String): Spell? {
        return spellList.find { it.name == name }
    }

    fun getAllSpells(): List<Spell> {
        return spellList
    }
}