package com.example.gestion_absences

import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @GET("users")
    fun getAllUsers(): Call<List<User>>

    @POST("users")
    fun addUser(@Body user: User): Call<User>

    @PUT("users/{id}")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<User>

    @GET("users/role")  // Modification ici pour différencier les appels
    fun getUsersByRole(@Query("role") role: String): Call<List<User>>
}

