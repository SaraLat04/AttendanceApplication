package com.example.gestion_absences

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var userAdapter: UserAdapter
    private val users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(users, { user ->
            // Logique pour la mise à jour
            val intent = Intent(this, EditUserActivity::class.java)
            intent.putExtra("user", user)
            startActivityForResult(intent, 100)
        }, { user ->
            deleteUser(user)
        })

        recyclerView.adapter = userAdapter

        addButton.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivityForResult(intent, 100)
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        val api = ApiClient.getClient().create(UserApi::class.java)
        api.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    users.clear()
                    users.addAll(response.body()!!)
                    userAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@UserActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@UserActivity, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun deleteUser(user: User) {
        val api = ApiClient.getClient().create(UserApi::class.java)
        api.deleteUser(user.id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@UserActivity, "Utilisateur supprimé", Toast.LENGTH_SHORT).show()
                    fetchUsers()  // Rafraîchir la liste des utilisateurs après suppression
                } else {
                    Toast.makeText(this@UserActivity, "Erreur de suppression", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@UserActivity, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fetchUsers() // Recharger la liste des utilisateurs
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val updatedUser = data.getSerializableExtra("updatedUser") as User
            // Trouver l'utilisateur mis à jour et rafraîchir la liste
            val index = users.indexOfFirst { it.id == updatedUser.id }
            if (index != -1) {
                users[index] = updatedUser
                userAdapter.notifyItemChanged(index)
            }
        }
    }
}
