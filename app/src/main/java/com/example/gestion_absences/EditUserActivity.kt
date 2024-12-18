package com.example.gestion_absences

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditUserActivity : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var isActiveSwitch: Switch
    private lateinit var submitButton: Button
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        usernameInput = findViewById(R.id.username)
        emailInput = findViewById(R.id.email)
        isActiveSwitch = findViewById(R.id.isActiveSwitch)
        submitButton = findViewById(R.id.submitButton)

        // Récupération de l'utilisateur passé en Intent
        user = intent.getSerializableExtra("user") as User
        populateFields(user)

        submitButton.setOnClickListener {
            updateUser()
        }
    }

    private fun populateFields(user: User) {
        usernameInput.setText(user.username)
        emailInput.setText(user.email)
        isActiveSwitch.isChecked = user.isActive
    }

    private fun updateUser() {
        val updatedUser = user.copy(
            username = usernameInput.text.toString(),
            email = emailInput.text.toString(),
            isActive = isActiveSwitch.isChecked
        )

        val service = ApiClient.getClient().create(UserApi::class.java)
        service.updateUser(updatedUser.id, updatedUser).enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(call: retrofit2.Call<User>, response: retrofit2.Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditUserActivity, "Utilisateur mis à jour avec succès", Toast.LENGTH_SHORT).show()

                    // Créer un Intent pour renvoyer les données mises à jour
                    val resultIntent = Intent()
                    resultIntent.putExtra("updatedUser", updatedUser)
                    setResult(RESULT_OK, resultIntent)  // Retourner les données mises à jour
                    finish()  // Retourner à UserActivity
                } else {
                    Toast.makeText(this@EditUserActivity, "Erreur : ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<User>, t: Throwable) {
                Toast.makeText(this@EditUserActivity, "Erreur de connexion", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
