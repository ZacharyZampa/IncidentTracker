package com.zacharyzampa.incidenttracker.dao

import androidx.room.*
import com.zacharyzampa.incidenttracker.entity.Incident
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {

    @Query("SELECT * FROM incident_table WHERE deleted = FALSE")
    fun getIncidents(): Flow<List<Incident>>

    @Query("SELECT * FROM incident_table")
    fun getAllOccurrencesOfIncidents(): Flow<List<Incident>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incident: Incident)

    @Query("DELETE FROM incident_table")
    suspend fun deleteAll()

    @Update
    suspend fun delete(incident: Incident)
}