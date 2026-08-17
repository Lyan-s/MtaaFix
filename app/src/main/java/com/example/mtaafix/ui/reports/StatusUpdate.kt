package com.example.mtaafix.ui.reports

data class StatusUpdate(
    val id: String = "",
    val reportId: String = "",
    val reportTitle: String = "",
    val userId: String = "",
    val oldStatus: String = "",
    val newStatus: String = "",
    val timestamp: Long = System.currentTimeMillis()
)