package com.zacharyzampa.incidenttracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "incident_table", primaryKeys = ["incident", "timestamp"])
data class Incident(
        @ColumnInfo(name = "incident") val incident: String,
        @ColumnInfo(name = "timestamp") val timestamp: Long,
        @ColumnInfo(name = "deleted") var deleted: Boolean
        )