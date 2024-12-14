package com.example.gestion_absences

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateStudentActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var photoUrlEditText: EditText
    private lateinit var classIdEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student)

        // Initialiser les vues
        firstNameEditText = findViewById(R.id.editFirstName)
        lastNameEditText = findViewById(R.id.editLastName)
        emailEditText = findViewById(R.id.editEmail)
        photoUrlEditText = findViewById(R.id.editPhotoUrl)
        classIdEditText = findViewById(R.id.editClassId)
        saveButton = findViewById(R.id.saveButton)

        // Récupérer les données de l'étudiant via l'intent
        val student = intent.getParcelableExtra<Student>("student") // Utilise Parcelable
        student?.let {
            firstNameEditText.setText(it.first_name) // Remplace par first_name
            lastNameEditText.setText(it.last_name)   // Remplace par last_name
            emailEditText.setText(it.email)
            photoUrlEditText.setText(it.photo_url ?: "") // Remplace par photo_url
            classIdEditText.setText(it.class_id.toString()) // Remplace par class_id
        }

        saveButton.setOnClickListener {
            // Collecter les données mises à jour
            val updatedStudent = Student(
                id = student?.id ?: 0,
                first_name = firstNameEditText.text.toString(), // Utilise first_name
                last_name = lastNameEditText.text.toString(),   // Utilise last_name
                email = emailEditText.text.toString(),
                photo_url = photoUrlEditText.text.toString(),   // Utilise photo_url
                class_id = classIdEditText.text.toString().toIntOrNull() ?: 0 // Utilise class_id
            )

            // Envoyer les données au serveur via une API (Retrofit)
            updateStudentOnServer(updatedStudent)
        }
    }

    private fun updateStudentOnServer(student: Student) {
        val service = ApiClient.getClient().create(StudentService::class.java)
        service.updateStudent(student.id, student).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@UpdateStudentActivity,
                        "Étudiant mis à jour avec succès",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Fermer l'activité et revenir à la précédente
                } else {
                    Toast.makeText(
                        this@UpdateStudentActivity,
                        "Erreur lors de la mise à jour",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@UpdateStudentActivity,
                    "Échec de la connexion au serveur",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
