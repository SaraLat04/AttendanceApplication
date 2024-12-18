package com.example.gestion_absences

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentClassActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentClassAdapter
    private lateinit var studentService: StudentService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_class)

        // Initialisation de RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialisation de l'adaptateur
        studentAdapter = StudentClassAdapter(mutableListOf())
        recyclerView.adapter = studentAdapter

        // Initialisation de Retrofit et du service StudentService
        studentService = ApiClient.getClient().create(StudentService::class.java)

        // Charger les étudiants
        fetchAllStudents()
    }

    // Méthode pour récupérer les étudiants
    private fun fetchAllStudents() {
        studentService.getStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful && response.body() != null) {
                    val students = response.body()!!
                    studentAdapter.updateStudents(students)
                } else {
                    Toast.makeText(
                        this@StudentClassActivity,
                        "Aucun étudiant trouvé.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(
                    this@StudentClassActivity,
                    "Erreur réseau : ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
