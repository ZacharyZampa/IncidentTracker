package com.zacharyzampa.incidenttracker.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zacharyzampa.incidenttracker.entity.Incident
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

@Database(entities = [Incident::class], version = 1, exportSchema = false)
abstract class IncidentRoomDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: IncidentRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): IncidentRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IncidentRoomDatabase::class.java,
                    "incident_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    .fallbackToDestructiveMigration()
                    .addCallback(IncidentDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class IncidentDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.incidentDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(incidentDao: IncidentDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            incidentDao.deleteAll()

            var incident = Incident("Hi, I am a sample incident", Instant.now().toEpochMilli(), 0)
            incidentDao.insert(incident)
            incident = Incident("Add more incidents using the plus button", Instant.now().toEpochMilli(), 0)
            incidentDao.insert(incident)
            incident = Incident("Remove an incident by swiping left or right on it", Instant.now().toEpochMilli(), 0)
            incidentDao.insert(incident)
            incident = Incident("Configure email settings in the top right button", Instant.now().toEpochMilli(), 0)
            incidentDao.insert(incident)
        }
    }
}