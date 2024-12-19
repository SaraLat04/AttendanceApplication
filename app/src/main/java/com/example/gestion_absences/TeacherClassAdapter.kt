package com.example.gestion_absences

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.text.SimpleDateFormat
import java.util.Locale

class TeacherClassAdapter(
    private val classes: List<Classe>
) : RecyclerView.Adapter<TeacherClassAdapter.ViewHolder>() {

    // Classe interne pour le ViewHolder
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val className: TextView = view.findViewById(R.id.className)
        val classDate: TextView = view.findViewById(R.id.classDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_classe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val classe = classes[position]
        holder.className.text = classe.nom

        // Formater la date
        val dateString = classe.date  // Supposons que 'classe.date' est une chaîne au format ISO 8601 (par exemple "2024-12-19")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Format d'entrée de la date
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale.FRENCH) // Format de sortie "19 décembre 2024"

        try {
            val date = inputFormat.parse(dateString) // Convertir la chaîne en objet Date
            holder.classDate.text = if (date != null) outputFormat.format(date) else "Date invalide"
        } catch (e: Exception) {
            holder.classDate.text = "Date invalide"
        }

        // Redirection vers StudentClassActivity
        holder.itemView.setOnClickListener {
            val context: Context = holder.itemView.context
            val intent = Intent(context, StudentClassActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = classes.size
}
