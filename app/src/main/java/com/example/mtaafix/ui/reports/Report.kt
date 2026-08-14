package com.example.mtaafix.ui.reports

data class Report(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val severity: String = "",
    val locationLabel: String = "",
    val photoUrl: String = "",
    val status: String = "Pending",
    val createdAt: Long = System.currentTimeMillis()
)