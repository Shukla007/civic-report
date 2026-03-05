package com.civicreport.data.api

object ApiConstants {
    // Use 10.0.2.2 for Android emulator to connect to localhost
    // For physical device, use your computer's IP address
    const val BASE_URL = "http://10.0.2.2:5000/"
    
    val CATEGORIES = listOf(
        "Road Damage",
        "Street Lighting",
        "Garbage/Sanitation",
        "Water Supply",
        "Drainage",
        "Parks & Recreation",
        "Traffic Signal",
        "Other"
    )
    
    // Must match MongoDB Report schema enum: ['low', 'medium', 'high']
    val PRIORITIES = listOf("low", "medium", "high")

    // Must match MongoDB Report schema enum: ['pending', 'acknowledged', 'in-progress', 'resolved', 'rejected']
    val STATUSES = listOf("pending", "acknowledged", "in-progress", "resolved", "rejected")
}
