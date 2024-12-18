package com.example.gestion_absences
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Classe(
    val id: Int,
    val nom: String,
    val id_teacher: Int,
    val date: String
) : Parcelable
