package com.example.gestion_absences

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.app.DatePickerDialog
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import java.util.*

import android.widget.ArrayAdapter
import android.widget.Spinner

class AddClasseActivity : AppCompatActivity() {

    private lateinit var classNameInput: EditText
    private lateinit var classDateInput: EditText
    private lateinit var teacherSpinner: Spinner
    private lateinit var addButton: Button

    private val apiService: ClassApi by lazy {
        ApiClient.getClient().create(ClassApi::class.java)
    }

    private var teachers: List<User> = emptyList()
    private var selectedTeacherId: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_class)

        classNameInput = findViewById(R.id.edit_text_class_name)
        classDateInput = findViewById(R.id.edit_text_class_date)
        teacherSpinner = findViewById(R.id.spinner_teacher)
        addButton = findViewById(R.id.button_add_class)

        // Charger la liste des enseignants
        loadTeachers()

        // Configurer le DatePicker
        classDateInput.setOnClickListener {
            showDatePickerDialog()
        }

        addButton.setOnClickListener {
            addNewClass()
        }
    }

    private fun loadTeachers() {
        apiService.getTeachers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    teachers = response.body() ?: emptyList()
                    setupTeacherSpinner()
                } else {
                    Toast.makeText(this@AddClasseActivity, "Erreur lors du chargement des enseignants", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@AddClasseActivity, "Échec de connexion: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupTeacherSpinner() {
        val teacherNames = teachers.map { it.username }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, teacherNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        teacherSpinner.adapter = adapter

        // Gérer la sélection
        teacherSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTeacherId = teachers[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedTeacherId = null
            }
        })
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format de la date : JJ/MM/AAAA
                val dateString = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                classDateInput.setText(dateString)
            },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun addNewClass() {
        val className = classNameInput.text.toString()
        val date = classDateInput.text.toString()

        if (className.isEmpty() || selectedTeacherId == null || date.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        val newClass = Classe(
            id = 0,
            nom = className,
            id_teacher = selectedTeacherId!!,
            date = date
        )

        apiService.addClass(newClass).enqueue(object : Callback<Classe> {
            override fun onResponse(call: Call<Classe>, response: Response<Classe>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddClasseActivity, "Classe ajoutée avec succès!", Toast.LENGTH_SHORT).show()
                    finish() // Retour à l'activité précédente
                } else {
                    Toast.makeText(this@AddClasseActivity, "Erreur: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Classe>, t: Throwable) {
                Toast.makeText(this@AddClasseActivity, "Erreur de connexion: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
