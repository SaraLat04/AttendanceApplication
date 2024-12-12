package com.example.gestion_absences

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditStudentActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var studentImage: ImageView

    private var studentId: Int = -1  // L'ID de l'étudiant à modifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student)

        // Initialiser les vues
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        saveButton = findViewById(R.id.saveButton)
        studentImage = findViewById(R.id.studentImage)

        // Récupérer l'ID de l'étudiant passé par l'intent
        studentId = intent.getIntExtra("student_id", -1)

        if (studentId != -1) {
            // Charger les détails de l'étudiant et les afficher dans les EditText
            loadStudentDetails(studentId)
        } else {
            Toast.makeText(this, "ID de l'étudiant non spécifié", Toast.LENGTH_SHORT).show()
            finish()
        }

        saveButton.setOnClickListener {
            val classId = intent.getIntExtra("class_id", -1) // Récupérer le classId depuis l'intent
            if (classId != -1) {
                if (validateInputs()) {
                    updateStudentDetails(classId)
                }
            } else {
                Toast.makeText(this, "Class ID non spécifié", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Méthode pour charger les détails de l'étudiant depuis l'API
    private fun loadStudentDetails(studentId: Int) {
        val service = ApiClient.getClient().create(StudentService::class.java)
        service.getStudentById(studentId).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    val student = response.body()
                    student?.let {
                        firstNameEditText.setText(it.first_name)
                        lastNameEditText.setText(it.last_name)
                        emailEditText.setText(it.email)
                        Glide.with(this@EditStudentActivity)
                            .load(it.photo_url)
                            .placeholder(R.drawable.ic_placeholder)
                            .into(studentImage)
                    }
                } else {
                    Toast.makeText(this@EditStudentActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@EditStudentActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Méthode pour valider les champs avant l'envoi
    private fun validateInputs(): Boolean {
        return when {
            firstNameEditText.text.isNullOrBlank() -> {
                Toast.makeText(this, "Le prénom est requis", Toast.LENGTH_SHORT).show()
                false
            }
            lastNameEditText.text.isNullOrBlank() -> {
                Toast.makeText(this, "Le nom est requis", Toast.LENGTH_SHORT).show()
                false
            }
            emailEditText.text.isNullOrBlank() -> {
                Toast.makeText(this, "L'email est requis", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    // Méthode pour mettre à jour les informations de l'étudiant
    private fun updateStudentDetails(classId: Int) {
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val email = emailEditText.text.toString()

        val updatedStudent = Student(
            id = studentId,
            first_name = firstName,
            last_name = lastName,
            email = email,
            photo_url = "",  // Vous pouvez gérer l'image de manière similaire
            class_id = classId  // Ajouter le class_id ici
        )

        val service = ApiClient.getClient().create(StudentService::class.java)
        service.updateStudent(studentId, classId, updatedStudent).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditStudentActivity, "Étudiant mis à jour", Toast.LENGTH_SHORT).show()
                    // Retourner à l'activité précédente avec un résultat OK
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@EditStudentActivity, "Erreur de mise à jour", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@EditStudentActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
