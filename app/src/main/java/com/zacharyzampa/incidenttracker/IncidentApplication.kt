package com.zacharyzampa.incidenttracker

import android.app.Application
import com.zacharyzampa.incidenttracker.dao.IncidentRepository
import com.zacharyzampa.incidenttracker.dao.IncidentRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class IncidentApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { IncidentRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { IncidentRepository(database.incidentDao()) }
}