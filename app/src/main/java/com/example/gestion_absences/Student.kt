package com.example.gestion_absences

data class Student(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val photo_url: String?,
    val class_id: Int
)
