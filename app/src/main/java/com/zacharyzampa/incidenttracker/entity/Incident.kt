package com.zacharyzampa.incidenttracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_table")
data class Incident(@PrimaryKey @ColumnInfo(name = "incident") val incident: String)