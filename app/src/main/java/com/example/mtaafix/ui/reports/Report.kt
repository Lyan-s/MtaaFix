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
    val latitude: Double? = null,
    val longitude: Double? = null,
    val upvotes: Int = 0,
    val upvotedBy: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)