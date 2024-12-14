package com.example.gestion_absences

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StudentService {
    @GET("/students/")
    fun getStudents(): Call<List<Student>>

    @POST("/students/")
    fun addStudent(@Body student: Student): Call<Student>

    @DELETE("/students/{id}")
    fun deleteStudent(@Path("id") id: String): Call<Void>

    @GET("students/{id}")
    fun getStudentById(@Path("id") id: Int): Call<Student>

    @PUT("students/{id}")
    fun updateStudent(@Path("id") id: Int, @Body student: Student): Call<Void>


}
