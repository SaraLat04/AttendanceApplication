package com.example.gestion_absences

import retrofit2.Call
import retrofit2.http.*

interface ClassApi {

    // Ajouter une nouvelle classe
    @POST("classes/")
    fun addClass(@Body newClass: Classe): Call<Classe>  // Changer AddClassRequest par Classe

    // Récupérer toutes les classes
    @GET("classes/")
    fun getAllClasses(): Call<List<Classe>>

    // Récupérer une classe par ID
    @GET("classes/{id}")
    fun getClassById(@Path("id") id: Int): Call<Classe>

    // Récupérer tous les utilisateurs de type "teacher"
    @GET("users/teachers")
    fun getTeachers(): Call<List<User>>

    // Récupérer les classes d'un enseignant par ID
    @GET("/classes/teacher/{teacherId}")
    fun getClassesForTeacher(@Path("teacherId") teacherId: Int): Call<List<Classe>>
}
