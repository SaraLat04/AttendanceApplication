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
import com.example.gestion_absences.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStudentActivity : AppCompatActivity() {
    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var submitButton: Button
    private lateinit var selectImageButton: Button
    private lateinit var studentImage: ImageView

    private var selectedImageBitmap: Bitmap? = null

    private val IMAGE_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        // Initialisation des vues
        firstNameInput = findViewById(R.id.firstName)
        lastNameInput = findViewById(R.id.lastName)
        emailInput = findViewById(R.id.email)
        submitButton = findViewById(R.id.submitButton)
        selectImageButton = findViewById(R.id.selectImageButton)
        studentImage = findViewById(R.id.studentImage)

        // Gestion du clic pour sélectionner une image
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }

        // Gestion du clic pour soumettre l'étudiant
        submitButton.setOnClickListener {


            // Convertir l'image en base64 ou en fichier (selon votre API)
            val student = Student(
                id = 0,
                first_name = firstNameInput.text.toString(),
                last_name = lastNameInput.text.toString(),
                email = emailInput.text.toString(),
                photo_url = selectedImageBitmap?.let { bitmapToBase64(it) }
            )

            val service = ApiClient.getClient().create(StudentService::class.java)
            service.addStudent(student).enqueue(object : Callback<Student> {
                override fun onResponse(call: Call<Student>, response: Response<Student>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddStudentActivity, "Étudiant ajouté", Toast.LENGTH_SHORT).show()

                        // Indiquer que l'étudiant a été ajouté avec succès
                        val intent = Intent()
                        intent.putExtra("isStudentAdded", true)
                        setResult(RESULT_OK, intent)

                        finish()
                    } else {
                        Toast.makeText(this@AddStudentActivity, "Erreur : ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }


                override fun onFailure(call: Call<Student>, t: Throwable) {
                    Toast.makeText(this@AddStudentActivity, "Erreur de réseau : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Gérer la sélection de l'image depuis la galerie
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                selectedImageBitmap = imageBitmap
                studentImage.setImageBitmap(imageBitmap)
            }
        }
    }

    // Convertir l'image Bitmap en Base64 (si nécessaire pour l'API)
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    }
}