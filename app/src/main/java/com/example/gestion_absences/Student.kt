package com.example.gestion_absences

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val photo_url: String?
) : Parcelable

