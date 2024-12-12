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

    @PUT("students/{id}/class/{class_id}")
    fun updateStudent(
        @Path("id") studentId: Int,
        @Path("class_id") classId: Int,
        @Body student: Student
    ): Call<Student>


}
