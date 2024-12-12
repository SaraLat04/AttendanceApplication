package com.example.gestion_absences

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_absences.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(listOf(), ::deleteStudent, ::editStudent)
        recyclerView.adapter = adapter

        loadStudents()

        addButton.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivityForResult(intent, ADD_STUDENT_REQUEST_CODE)
        }
    }

    private fun loadStudents() {
        val service = ApiClient.getClient().create(StudentService::class.java)
        service.getStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    adapter.updateStudents(response.body() ?: listOf())
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun deleteStudent(student: Student) {
        // Votre code pour supprimer un étudiant
    }

    private fun editStudent(student: Student) {
        val intent = Intent(this, EditStudentActivity::class.java)
        intent.putExtra("student_id", student.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_STUDENT_REQUEST_CODE && resultCode == RESULT_OK) {
            val isStudentAdded = data?.getBooleanExtra("isStudentAdded", false) ?: false
            if (isStudentAdded) {
                // Recharger la liste des étudiants
                loadStudents()
            }
        }
    }

    companion object {
        const val ADD_STUDENT_REQUEST_CODE = 1002
    }
}