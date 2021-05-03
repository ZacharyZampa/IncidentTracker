package com.zacharyzampa.incidenttracker.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config_table")
data class Config(
    @PrimaryKey @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "to") val to: String,
    @ColumnInfo(name = "cc") val cc: String,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "body") val body: String
)