package com.example.gestion_absences

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateStudentActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var studentImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var saveButton: Button

    private var selectedImageBitmap: Bitmap? = null

    private val IMAGE_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student)

        // Initialiser les vues
        firstNameEditText = findViewById(R.id.editFirstName)
        lastNameEditText = findViewById(R.id.editLastName)
        emailEditText = findViewById(R.id.editEmail)
        studentImage = findViewById(R.id.studentImage)
        selectImageButton = findViewById(R.id.selectImageButton)
        saveButton = findViewById(R.id.saveButton)

        // Récupérer les données de l'étudiant via l'intent
        val student = intent.getParcelableExtra<Student>("student") // Utilise Parcelable
        student?.let {
            firstNameEditText.setText(it.first_name)
            lastNameEditText.setText(it.last_name)
            emailEditText.setText(it.email)

            // Afficher l'image existante si elle existe et est valide
            if (it.photo_url != null && it.photo_url.isNotEmpty()) {
                Glide.with(this)
                    .load(it.photo_url) // Assurez-vous que c'est une URL valide
                    .placeholder(R.drawable.ic_placeholder) // Optionnel : ajouter une image de remplacement
                    .error(R.drawable.error_image) // Optionnel : ajouter une image d'erreur
                    .into(studentImage)
            }
        }


        // Permettre à l'utilisateur de sélectionner une nouvelle image
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }

        // Sauvegarder les modifications
        saveButton.setOnClickListener {
            // Validation des champs
            if (firstNameEditText.text.isBlank() || lastNameEditText.text.isBlank() || emailEditText.text.isBlank()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Collecter les données mises à jour
            val updatedStudent = Student(
                id = student?.id ?: 0,
                first_name = firstNameEditText.text.toString(),
                last_name = lastNameEditText.text.toString(),
                email = emailEditText.text.toString(),
                photo_url = selectedImageBitmap?.let { bitmapToBase64(it) } ?: student?.photo_url
            )

            // Envoyer les données au serveur via une API (Retrofit)
            updateStudentOnServer(updatedStudent)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
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
                    // Retourner les données mises à jour
                    val intent = Intent().apply {
                        putExtra("isStudentUpdated", true)
                        putExtra("updatedStudent", student)
                    }
                    setResult(RESULT_OK, intent)
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

    // Gérer la sélection d'une nouvelle image depuis la galerie
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                // Convertir l'URI en Bitmap
                val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                // Convertir le Bitmap en base64
                selectedImageBitmap = imageBitmap
                val base64Image = bitmapToBase64(imageBitmap)

                // Mettre à jour l'ImageView avec l'image choisie
                studentImage.setImageBitmap(imageBitmap)

                // Vous pouvez maintenant envoyer le base64Image lors de l'envoi de la requête au serveur
            }
        }
    }


}

