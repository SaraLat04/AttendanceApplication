package com.example.gestion_absences

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUserActivity : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var isActiveSwitch: Switch
    private lateinit var roleSpinner: Spinner
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        // Initialisation des vues
        usernameInput = findViewById(R.id.username)
        emailInput = findViewById(R.id.email)
        passwordInput = findViewById(R.id.password)
        isActiveSwitch = findViewById(R.id.isActiveSwitch)
        roleSpinner = findViewById(R.id.roleSpinner)
        submitButton = findViewById(R.id.submitButton)

        // Configuration du spinner des rôles
        val roles = RoleEnum.values().map { it.role }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        // Gestion du clic pour soumettre l'utilisateur
        submitButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val isActive = isActiveSwitch.isChecked
            val roleString = RoleEnum.values()[roleSpinner.selectedItemPosition].role // Récupère la chaîne depuis le spinner

            // Vérification des champs obligatoires
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Création de l'objet User
            val user = User(
                id = 0, // L'ID sera géré automatiquement par l'API
                username = username,
                email = email,
                password = password,
                isActive = isActive,
                role = roleString // Utilisation de la chaîne ici
            )

            // Appel à l'API pour ajouter l'utilisateur
            val service = ApiClient.getClient().create(UserApi::class.java)
            service.addUser(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddUserActivity, "Utilisateur ajouté avec succès", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK, Intent().putExtra("isUserAdded", true))
                        finish()
                    } else {
                        Toast.makeText(this@AddUserActivity, "Erreur : ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@AddUserActivity, "Erreur de réseau : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
}
