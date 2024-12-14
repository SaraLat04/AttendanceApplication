package com.example.gestion_absences

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AdminsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admins)

        // Récupérez la vue de la carte "Add Students"
        val addStudentsCard: View = findViewById(R.id.addStudents)

        // Ajoutez un écouteur de clic à la carte
        addStudentsCard.setOnClickListener {
            // Redirigez vers HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}

