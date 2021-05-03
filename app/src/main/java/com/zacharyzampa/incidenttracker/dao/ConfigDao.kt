//package com.zacharyzampa.incidenttracker.dao
//
//import androidx.room.*
//import com.zacharyzampa.incidenttracker.entity.Config
//import com.zacharyzampa.incidenttracker.entity.Incident
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface ConfigDao {
//
//    @Query("SELECT * FROM config_table LIMIT 1")
//    fun getIncidents(): Flow<Config>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(config: Config)
//
//    @Query("DELETE FROM config_table")
//    suspend fun deleteAll()
//
//    @Delete
//    suspend fun delete(config: Config)
//}