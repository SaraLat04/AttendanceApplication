package com.example.gestion_absences

import java.io.Serializable

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val isActive: Boolean,
    val role: String // Le rôle est maintenant une chaîne de caractères
) : Serializable


// Enumération pour gérer les rôles si vous souhaitez les utiliser dans l'interface
enum class RoleEnum(val role: String) {
    TEACHER("teacher"),
    ADMIN("admin"),
    ETUDIANT("etudiant")
}
