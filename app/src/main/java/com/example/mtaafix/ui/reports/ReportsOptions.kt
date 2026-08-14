package com.example.mtaafix.ui.reports

enum class ReportCategory(val label: String) {
    POTHOLE("Potholes"),
    STREETLIGHT("Streetlights"),
    WATER_LEAK("Water leaks"),
    GARBAGE("Garbage"),
    ROADS("Roads"),
    ELECTRICAL("Electrical"),
    OTHER("Other")
}

enum class Severity(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}