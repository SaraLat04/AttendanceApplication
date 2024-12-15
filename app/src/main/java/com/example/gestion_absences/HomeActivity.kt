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
    private var students: List<Student> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(
            students,
            { student -> deleteStudent(student) },  // Suppression
            { student -> navigateToUpdateStudent(student) } // Mise à jour
        )
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
                    students = response.body() ?: listOf()
                    adapter.updateStudents(students)
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteStudent(student: Student) {
        val service = ApiClient.getClient().create(StudentService::class.java)
        service.deleteStudent(student.id.toString()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@HomeActivity, "Étudiant supprimé", Toast.LENGTH_SHORT).show()
                    students = students.filter { it.id != student.id }
                    adapter.updateStudents(students)
                } else {
                    Toast.makeText(this@HomeActivity, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Échec de la connexion", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToUpdateStudent(student: Student) {
        val intent = Intent(this, UpdateStudentActivity::class.java)
        intent.putExtra("student", student)
        startActivityForResult(intent, UPDATE_STUDENT_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_STUDENT_REQUEST_CODE && resultCode == RESULT_OK) {
            val isStudentAdded = data?.getBooleanExtra("isStudentAdded", false) ?: false
            if (isStudentAdded) {
                loadStudents()
            }
        } else if (requestCode == UPDATE_STUDENT_REQUEST_CODE && resultCode == RESULT_OK) {
            val isStudentUpdated = data?.getBooleanExtra("isStudentUpdated", false) ?: false
            if (isStudentUpdated) {
                val updatedStudent = data?.getParcelableExtra<Student>("updatedStudent")
                updatedStudent?.let {
                    students = students.map { student ->
                        if (student.id == it.id) it else student
                    }
                    adapter.updateStudents(students)
                }
            }
        }
    }

    companion object {
        const val ADD_STUDENT_REQUEST_CODE = 1002
        const val UPDATE_STUDENT_REQUEST_CODE = 1003
    }
}
