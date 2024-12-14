package com.example.gestion_absences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StudentAdapter(
    private var students: List<Student>,
    private val onDeleteClick: (Student) -> Unit,
    private val onUpdateClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val studentImage: ImageView = view.findViewById(R.id.studentImage)
        val firstName: TextView = view.findViewById(R.id.firstName)
        val lastName: TextView = view.findViewById(R.id.lastName)
        val email: TextView = view.findViewById(R.id.email)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        val updateButton: ImageButton = view.findViewById(R.id.updateButton) // Nouveau bouton de mise à jour
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.firstName.text = student.first_name
        holder.lastName.text = student.last_name
        holder.email.text = student.email

        // Charger l'image avec Glide
        val photoUrl = student.photo_url
        if (photoUrl != null) {
            Glide.with(holder.itemView.context)
                .load(photoUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.studentImage)
        } else {
            holder.studentImage.setImageResource(R.drawable.ic_placeholder)
        }

        // Gestion des clics sur les boutons
        holder.deleteButton.setOnClickListener {
            onDeleteClick(student)
        }
        holder.updateButton.setOnClickListener {
            onUpdateClick(student) // Gestion du clic sur "Update"
        }
    }

    override fun getItemCount(): Int = students.size

    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}

