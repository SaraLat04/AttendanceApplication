package com.example.gestion_absences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClasseAdapter(private val classes: List<Classe>, private val teachers: List<User>) : RecyclerView.Adapter<ClasseAdapter.ClasseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClasseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return ClasseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClasseViewHolder, position: Int) {
        val classe = classes[position]
        holder.bind(classe, teachers)
    }

    override fun getItemCount(): Int = classes.size

    class ClasseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val className: TextView = itemView.findViewById(R.id.class_name)
        private val teacherName: TextView = itemView.findViewById(R.id.teacher_name)
        private val classDate: TextView = itemView.findViewById(R.id.class_date)

        fun bind(classe: Classe, teachers: List<User>) {
            className.text = classe.nom
            // Trouver l'enseignant correspondant à cette classe
            val teacher = teachers.find { it.id == classe.id_teacher }
            teacherName.text = teacher?.username ?: "Inconnu"
            classDate.text = classe.date
        }
    }
}
