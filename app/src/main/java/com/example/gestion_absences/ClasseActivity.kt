package com.example.gestion_absences

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_absences.AddClasseActivity
import com.example.gestion_absences.ApiClient
import com.example.gestion_absences.ClasseAdapter
import com.example.gestion_absences.ClassApi
import com.example.gestion_absences.Classe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClasseActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var classAdapter: ClasseAdapter
    private var classList = mutableListOf<Classe>()

    private lateinit var addSessionButton: Button

    private val apiService: ClassApi by lazy {
        ApiClient.getClient().create(ClassApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)

        // Initialisation de RecyclerView
        recyclerView = findViewById(R.id.recycler_view_classes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialisation du bouton pour ajouter une séance
        addSessionButton = findViewById(R.id.button_add_session)
        addSessionButton.setOnClickListener {
            val intent = Intent(this, AddClasseActivity::class.java)
            startActivityForResult(intent, 1) // Lancez l'add class activity avec un code de requête
        }

        // Appel pour récupérer les classes
        fetchClasses()
    }

    // Traitement du résultat de l'ajout de la classe
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val newClass = data?.getParcelableExtra<Classe>("new_class")
            newClass?.let {
                classList.add(0, it) // Ajoutez la nouvelle classe en haut de la liste
                classAdapter.notifyItemInserted(0) // Avertir l'adaptateur que l'élément a été inséré
            }
        }
    }


    private fun fetchClasses() {
        apiService.getAllClasses().enqueue(object : Callback<List<Classe>> {
            override fun onResponse(call: Call<List<Classe>>, response: Response<List<Classe>>) {
                if (response.isSuccessful) {
                    classList = response.body()?.toMutableList() ?: mutableListOf()
                    // Maintenant que nous avons les classes, récupérons les enseignants pour les passer à l'adaptateur
                    apiService.getTeachers().enqueue(object : Callback<List<User>> {
                        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                            if (response.isSuccessful) {
                                val teachers = response.body() ?: emptyList()
                                // Initialiser l'adaptateur avec les classes et les enseignants
                                classAdapter = ClasseAdapter(classList, teachers)
                                recyclerView.adapter = classAdapter
                            } else {
                                Toast.makeText(this@ClasseActivity, "Erreur lors du chargement des enseignants", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            Toast.makeText(this@ClasseActivity, "Échec de connexion: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this@ClasseActivity, "Erreur: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Classe>>, t: Throwable) {
                Toast.makeText(this@ClasseActivity, "Échec de connexion: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}