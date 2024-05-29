package it.brokenengineers.dnd_character_manager.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import it.brokenengineers.dnd_character_manager.data.classes.Ability
import it.brokenengineers.dnd_character_manager.data.classes.DndCharacter
import it.brokenengineers.dnd_character_manager.data.classes.DndClass
import it.brokenengineers.dnd_character_manager.data.classes.DndClassAbilityCrossRef
import it.brokenengineers.dnd_character_manager.data.classes.Race
import it.brokenengineers.dnd_character_manager.data.enums.AbilityEnum
import it.brokenengineers.dnd_character_manager.data.enums.DndClassEnum
import it.brokenengineers.dnd_character_manager.data.enums.RaceEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch

@Database(
    entities = [
        DndCharacter::class,
        Race::class,
        Ability::class,
        DndClassAbilityCrossRef::class,
        DndClass::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DndCharacterManagerDB: RoomDatabase() {
    abstract fun characterDao(): DndCharacterDao
    abstract fun raceDao(): RaceDao
    abstract fun abilityDao(): AbilityDao
    abstract fun dndClassDao(): DndClassDao

    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: DndCharacterManagerDB? = null
        // latch to wait for the database to be populated
        private val latch = CountDownLatch(1)

        fun getDatabase(context: Context): DndCharacterManagerDB? {
            // TODO remove later. Used to test the database population
//            context.deleteDatabase("dnd_character_manager_database")

            if (INSTANCE == null) {
                // ensure only one instance of the database is created even if multiple threads try to create an instance
                synchronized(DndCharacterManagerDB::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = androidx.room.Room.databaseBuilder(
                            context.applicationContext,
                            DndCharacterManagerDB::class.java,
                            "dnd_character_manager_database"
                        )
                            // how to add a migration
                            .addMigrations(

                            )
                            .addCallback(roomDatabaseCallback)
                            .build()

                    }
                }
            }
            assert(INSTANCE != null)
            // roomDatabaseCallback will be called only after the first use of the DB
            // Dummy query to trigger the callback
            runBlocking { INSTANCE?.raceDao()?.getAllRaces() }
            latch.await()
            return INSTANCE
        }

        /**
         * Override the onOpen method to populate the database.
         */
        private val roomDatabaseCallback: Callback =
            object : Callback() {
                // TODO Need to populate also other tables
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Use a new thread to insert the data
                    CoroutineScope(Dispatchers.IO).launch {
                        val raceDao = INSTANCE!!.raceDao()
                        val abilityDao = INSTANCE!!.abilityDao()
                        val dndClassDao = INSTANCE!!.dndClassDao()
                        val races = RaceEnum.entries.map { it.race }
                        val abilities = AbilityEnum.entries.map { it.ability }
                        val dndClasses = DndClassEnum.entries.map { it.dndClass }

                        Log.i("DndCharacterManagerDB", "Start populating database")

                        raceDao.insertAll(races)
                        abilityDao.insertAll(abilities)
                        dndClassDao.insertAll(dndClasses)

                        Log.i("DndCharacterManagerDB", "DB Populated, calling countDown()")
                        latch.countDown()
                    }
                }
            }
    }

}