package com.zacharyzampa.incidenttracker.dao

import androidx.annotation.WorkerThread
import com.zacharyzampa.incidenttracker.entity.Incident
import kotlinx.coroutines.flow.Flow

class IncidentRepository(private val incidentDao: IncidentDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val incidents: Flow<List<Incident>> = incidentDao.getIncidents()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(incident: Incident) {
        incidentDao.insert(incident)
    }
}