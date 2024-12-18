package com.example.gestion_absences

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AdminsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admins)

        // Carte "Add Students"
        val addStudentsCard: View = findViewById(R.id.addStudents)
        addStudentsCard.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        // Carte "Students List"
        val studentsListCard: View = findViewById(R.id.list_std)
        studentsListCard.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Carte "Add Users"
        val addUsersCard: View = findViewById(R.id.addUsers)
        addUsersCard.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        // **Carte "Add Class"**
        val addClassCard: View = findViewById(R.id.create_class_card)  // Assurez-vous que l'ID est correct dans le XML
        addClassCard.setOnClickListener {
            val intent = Intent(this, ClasseActivity::class.java) // Redirige vers ClasseActivity
            startActivity(intent)
        }
    }
}
