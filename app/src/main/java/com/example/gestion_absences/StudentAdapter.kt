package com.example.gestion_absences

import android.content.Intent
import android.graphics.BitmapFactory
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
        val updateButton: ImageButton = view.findViewById(R.id.updateButton)
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

        val photoUrl = student.photo_url
        if (photoUrl != null) {
            if (photoUrl.startsWith("http")) {
                // Charger l'image via URL avec Glide
                Glide.with(holder.itemView.context)
                    .load(photoUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.studentImage)
            } else {
                // Décoder le Base64 si c'est une chaîne Base64
                try {
                    val decodedString = android.util.Base64.decode(photoUrl, android.util.Base64.DEFAULT)
                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    holder.studentImage.setImageBitmap(decodedByte)
                } catch (e: IllegalArgumentException) {
                    // Si le Base64 échoue, on affiche le placeholder
                    holder.studentImage.setImageResource(R.drawable.ic_placeholder)
                }
            }
        } else {
            // Utilisation d'un placeholder si l'URL est null
            holder.studentImage.setImageResource(R.drawable.ic_placeholder)
        }

        // Boutons pour supprimer ou mettre à jour l'étudiant
        holder.deleteButton.setOnClickListener { onDeleteClick(student) }
        holder.updateButton.setOnClickListener { onUpdateClick(student) }

        // Détails de l'étudiant au clic
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, StudentDetailsActivity::class.java)
            intent.putExtra("student", student)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = students.size

    // Fonction pour mettre à jour la liste des étudiants
    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}
