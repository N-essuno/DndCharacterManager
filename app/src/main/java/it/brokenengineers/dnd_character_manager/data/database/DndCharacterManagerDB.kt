package it.brokenengineers.dnd_character_manager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DndCharacter::class], version = 1, exportSchema = false)
abstract class DndCharacterManagerDB: RoomDatabase() {
    abstract fun characterDao(): DndCharacterDao

    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: DndCharacterManagerDB? = null

        fun getDatabase(context: Context): DndCharacterManagerDB? {
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
            return INSTANCE
        }

        /**
         * Override the onOpen method to populate the database.
         */
        private val roomDatabaseCallback: Callback =
            object : Callback() {
                // TODO potential improvement. When we have RACE and DNDCLASS or other tables populate with enum values for each of those
            }
    }

}