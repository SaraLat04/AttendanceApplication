package com.example.gestion_absences

import retrofit2.Call
import retrofit2.http.*

interface ClassApi {

    // Ajouter une nouvelle classe
    @POST("seances/")
    fun addClass(@Body newSeance: Classe): Call<Classe>  // Changer AddSeanceRequest par Seance

    // Récupérer toutes les séances
    @GET("seances/")
    fun getAllClasses(): Call<List<Classe>>

    // Récupérer une séance par ID
    @GET("seances/{id}")
    fun getClassById(@Path("id") id: Int): Call<Classe>

    // Récupérer tous les utilisateurs de type "teacher"
    @GET("users/teachers")
    fun getTeachers(): Call<List<User>>

    // Récupérer les séances d'un enseignant par ID
    @GET("/seances/teacher/{teacherId}")
    fun getClassesForTeacher(@Path("teacherId") teacherId: Int): Call<List<Classe>>
}
