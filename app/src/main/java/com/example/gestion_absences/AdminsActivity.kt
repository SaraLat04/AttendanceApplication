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

        // Ajoutez un écouteur de clic à la carte "Add Students"
        addStudentsCard.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        // Récupérez la vue de la carte "Students List"
        val studentsListCard: View = findViewById(R.id.list_std)

        // Ajoutez un écouteur de clic à la carte "Students List"
        studentsListCard.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
