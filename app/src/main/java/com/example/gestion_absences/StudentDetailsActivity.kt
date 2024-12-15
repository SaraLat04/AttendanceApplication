package com.example.gestion_absences

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class StudentDetailsActivity : AppCompatActivity() {

    private lateinit var studentImage: ImageView
    private lateinit var firstName: TextView
    private lateinit var lastName: TextView
    private lateinit var email: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        // Initialiser les vues
        studentImage = findViewById(R.id.studentImage)
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        email = findViewById(R.id.email)

        // Récupérer les données de l'intent
        val student: Student? = intent.getParcelableExtra("student")

        student?.let {
            firstName.text = it.first_name
            lastName.text = it.last_name
            email.text = it.email

            // Charger l'image avec Glide
            val photoUrl = it.photo_url
            if (photoUrl != null) {
                if (photoUrl.startsWith("http")) {
                    Glide.with(this)
                        .load(photoUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(studentImage)
                } else {
                    try {
                        val decodedString = android.util.Base64.decode(photoUrl, android.util.Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        studentImage.setImageBitmap(decodedByte)
                    } catch (e: IllegalArgumentException) {
                        studentImage.setImageResource(R.drawable.ic_placeholder)
                    }
                }
            } else {
                studentImage.setImageResource(R.drawable.ic_placeholder)
            }

        }
    }
}
