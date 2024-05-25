package it.brokenengineers.dnd_character_manager.data.classes
//
//import it.brokenengineers.dnd_character_manager.data.classes.Ability
//import it.brokenengineers.dnd_character_manager.data.classes.DndClass
//import it.brokenengineers.dnd_character_manager.data.classes.Race
//import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
//import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
//import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
//import it.brokenengineers.dnd_character_manager.data.enums.SkillEnum
//
//data class OldCharacter (
//    val id: Int,
//    val name: String,
//    val race: Race,
//    val dndClass: DndClass,
//    val level: Int,
//    val abilityValues: Map<Ability, Int>,
//    val skillProficiencies: Set<Skill>,
//    var remainingHp: Int,
//    var tempHp: Int,
//    val spellsKnown: Set<Spell>?,
//    val preparedSpells: Set<Spell>?,
//    val availableSpellSlots: Map<Int, Int>?,
//    var inventoryItems: Set<InventoryItem>?,
//    val weapon: Weapon?
//) {
//    fun getProficiencyBonus(): Int {
//        return when (level) {
//            in 1..4 -> 2
//            in 5..8 -> 3
//            in 9..12 -> 4
//            in 13..16 -> 5
//            in 17..20 -> 6
//            else -> -1
//        }
//    }
//
//    fun getArmorClass(): Int {
//        val dexterityModifier = getAbilityModifier(AbilityEnum.DEXTERITY)
//        if (dndClass == DndClassEnum.BARBARIAN.dndClass) {
//            val constitutionModifier = getAbilityModifier(AbilityEnum.CONSTITUTION)
//            return 10 + constitutionModifier + dexterityModifier
//        } else {
//            return 10 + dexterityModifier
//        }
//    }
//
//    fun getInitiative(): Int {
//        return getAbilityModifier(AbilityEnum.DEXTERITY)
//    }
//
//    fun getWalkSpeed(): Int {
//        return race.walkSpeed
//    }
//
//    fun getMaxHp(): Int {
//        if (dndClass == DndClassEnum.BARBARIAN.dndClass) {
//            if (level == 1) {
//                return 12 + getAbilityModifier(AbilityEnum.CONSTITUTION)
//            } else {
//                return 12 + (7 + getAbilityModifier(AbilityEnum.CONSTITUTION)) * (level - 1)
//            }
//        } else if (dndClass == DndClassEnum.WIZARD.dndClass) {
//            if (level == 1) {
//                return 6 + getAbilityModifier(AbilityEnum.CONSTITUTION)
//            } else {
//                return 6 + (4 + getAbilityModifier(AbilityEnum.CONSTITUTION)) * (level - 1)
//            }
//        } else {
//            return -1
//        }
//    }
//
//    fun getAbilityModifier(abilityEnum: AbilityEnum): Int {
//        return (abilityValues[abilityEnum.ability]!! - 10) / 2
//    }
//
//    private fun getAbilityModifier(ability: Ability): Int {
//        return (abilityValues[ability]!! - 10) / 2
//    }
//
//    fun getSkills(): List<Pair<Skill, Int>> {
//        val skills = mutableListOf<Pair<Skill, Int>>()
//        for (enumEntry in SkillEnum.entries) {
//            var skillValue = getAbilityModifier(enumEntry.skill.ability)
//            if (skillProficiencies.contains(enumEntry.skill)) {
//                skillValue += getProficiencyBonus()
//            }
//            val pair = Pair(enumEntry.skill, skillValue)
//            skills.add(pair)
//        }
//        return skills
//    }
//
//    fun getSavingThrowBonus(abilityEnum: AbilityEnum): Int {
//        val abilityModifier = getAbilityModifier(abilityEnum)
//        if (dndClass.savingThrowProficiencies.contains(abilityEnum.ability)) {
//            return abilityModifier + getProficiencyBonus()
//        } else {
//            return abilityModifier
//        }
//    }
//
//    fun isProficientInAbility(abilityEnum: AbilityEnum): Boolean {
//        return dndClass.savingThrowProficiencies.contains(abilityEnum.ability)
//    }
//
//    fun getMaxCarryWeight(): Double {
//        return abilityValues[AbilityEnum.STRENGTH.ability]!! * 15.0
//    }
//
//    fun getCurrentCarryWeight(): Double {
//        var totalWeight = 0.0
//        inventoryItems?.forEach {
//            totalWeight += it.weight * it.quantity
//        }
//        return totalWeight
//    }
//
//    fun getAttackBonus(): Int {
//        return abilityValues[dndClass.primaryAbility]!! + getProficiencyBonus()
//    }
//
//    fun getSpellDcSavingThrow(): Int {
//        return 8 + getProficiencyBonus() + getAbilityModifier(dndClass.primaryAbility)
//    }
//
//    fun isSpellPrepared(spell: Spell): Boolean {
//        return preparedSpells?.map { it.name }?.contains(spell.name) ?: false
//    }
//
//    fun getMaxPreparedSpells(): Int {
//        if (dndClass == DndClassEnum.WIZARD.dndClass) {
//            return level + getAbilityModifier(dndClass.primaryAbility)
//        } else {
//            return 0
//        }
//    }
//
//    fun getMaxSpellSlots(spellLevel: Int): Int {
//        if (dndClass == DndClassEnum.WIZARD.dndClass) {
//            when (level) {
//                1 -> return when (spellLevel) {
//                    1 -> 2
//                    else -> 0
//                }
//
//                2 -> return when (spellLevel) {
//                    1 -> 3
//                    else -> 0
//                }
//
//                3 -> return when (spellLevel) {
//                    1 -> 4
//                    2 -> 2
//                    else -> 0
//                }
//
//                4 -> return when (spellLevel) {
//                    1 -> 4
//                    2 -> 3
//                    else -> 0
//                }
//
//                5 -> return when (spellLevel) {
//                    1 -> 4
//                    2 -> 3
//                    3 -> 2
//                    else -> 0
//                }
//
//                6 -> return when (spellLevel) {
//                    1 -> 4
//                    2 -> 3
//                    3 -> 3
//                    else -> 0
//                }
//
//                7 -> return when (spellLevel) {
//                    1 -> 4
//                    2 -> 3
//                    3 -> 3
//                    4 -> 1
//                    else -> 0
//                }
//
//                8 -> return when (spellLevel) {
//                    1 -> 4
//                    2 -> 3
//                    3 -> 3
//                    4 -> 2
//                    else -> 0
//                }
//
//                9 -> return when (spellLevel) {
//                    1 -> 4
//                    2 -> 3
//                    3 -> 3
//                    4 -> 3
//                    5 -> 1
//                    else -> 0
//                }
//
//                10 -> return when (spellLevel) {
//                    1 -> 4
//                    2 -> 3
//                    3 -> 3
//                    4 -> 3
//                    5 -> 2
//                    else -> 0
//                }
//
//                else -> return 0
//            }
//        } else {
//            return 0
//        }
//    }
//
//    fun computeId(): Int {
//        // TODO when database is implemented, return the id from the database
//        return 0
//    }
//}
//
//private fun getAbilityValuesForRace(race: Race): Map<Ability, Int> {
//    when (race) {
//        RaceEnum.DWARF.race -> return mapOf(
//            AbilityEnum.STRENGTH.ability to 14,
//            AbilityEnum.DEXTERITY.ability to 10,
//            AbilityEnum.CONSTITUTION.ability to 16,
//            AbilityEnum.INTELLIGENCE.ability to 8,
//            AbilityEnum.WISDOM.ability to 12,
//            AbilityEnum.CHARISMA.ability to 10
//        )
//
//        RaceEnum.ELADRIN.race -> return mapOf(
//            AbilityEnum.STRENGTH.ability to 10,
//            AbilityEnum.DEXTERITY.ability to 12,
//            AbilityEnum.CONSTITUTION.ability to 8,
//            AbilityEnum.INTELLIGENCE.ability to 14,
//            AbilityEnum.WISDOM.ability to 10,
//            AbilityEnum.CHARISMA.ability to 16
//        )
//
//        else -> return mapOf(
//            AbilityEnum.STRENGTH.ability to 10,
//            AbilityEnum.DEXTERITY.ability to 10,
//            AbilityEnum.CONSTITUTION.ability to 10,
//            AbilityEnum.INTELLIGENCE.ability to 10,
//            AbilityEnum.WISDOM.ability to 10,
//            AbilityEnum.CHARISMA.ability to 10
//        )
//    }
//}
//
//private fun initializeSpellSlots(dndClass: DndClass): Map<Int, Int> {
//    return when (dndClass) {
//        DndClassEnum.BARBARIAN.dndClass -> mapOf()
//        DndClassEnum.WIZARD.dndClass -> mapOf(
//            1 to 2
//        )
//
//        else -> mapOf()
//    }
//}
//
//private fun getProficienciesForClass(dndClass: DndClass): Set<Skill> {
//    return when (dndClass) {
//        DndClassEnum.BARBARIAN.dndClass -> setOf(
//            SkillEnum.ANIMAL_HANDLING.skill,
//            SkillEnum.ATHLETICS.skill,
//            SkillEnum.INTIMIDATION.skill,
//        )
//
//        DndClassEnum.WIZARD.dndClass -> setOf(
//            SkillEnum.ARCANA.skill,
//            SkillEnum.HISTORY.skill,
//            SkillEnum.INSIGHT.skill,
//        )
//
//        else -> setOf()
//    }
//}
//
