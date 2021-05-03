//package com.zacharyzampa.incidenttracker.dao
//
//import androidx.annotation.WorkerThread
//import com.zacharyzampa.incidenttracker.entity.Config
//import kotlinx.coroutines.flow.Flow
//
//class ConfigRepository(private val configDao: ConfigDao) {
//
//    // Room executes all queries on a separate thread.
//    // Observed Flow will notify the observer when the data has changed.
//    val config: Flow<Config> = configDao.getIncidents()
//
//    // By default Room runs suspend queries off the main thread, therefore, we don't need to
//    // implement anything else to ensure we're not doing long running database work
//    // off the main thread.
//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun insert(config: Config) {
//        configDao.insert(config)
//    }
//
//    @WorkerThread
//    suspend fun delete(config: Config) {
//        configDao.delete(config)
//    }
//}