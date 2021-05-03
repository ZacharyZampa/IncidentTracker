package com.zacharyzampa.incidenttracker.model

data class Config(
    val to: String,
    val cc: String,
    val subject: String,
    val body: String
)