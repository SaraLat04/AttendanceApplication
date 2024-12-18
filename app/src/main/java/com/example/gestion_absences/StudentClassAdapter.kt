package com.example.gestion_absences

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StudentClassAdapter(
    private var students: List<Student>
) : RecyclerView.Adapter<StudentClassAdapter.StudentViewHolder>() {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val studentImage: ImageView = view.findViewById(R.id.studentImage)
        val firstName: TextView = view.findViewById(R.id.firstName)
        val lastName: TextView = view.findViewById(R.id.lastName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.firstName.text = student.first_name
        holder.lastName.text = student.last_name

        val photoUrl = student.photo_url
        if (photoUrl != null) {
            if (photoUrl.startsWith("http")) {
                Glide.with(holder.itemView.context)
                    .load(photoUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.studentImage)
            } else {
                try {
                    val decodedString = android.util.Base64.decode(photoUrl, android.util.Base64.DEFAULT)
                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    holder.studentImage.setImageBitmap(decodedByte)
                } catch (e: IllegalArgumentException) {
                    holder.studentImage.setImageResource(R.drawable.ic_placeholder)
                }
            }
        } else {
            holder.studentImage.setImageResource(R.drawable.ic_placeholder)
        }
    }

    override fun getItemCount(): Int = students.size

    fun updateStudents(newStudents: List<Student>) {
        students = newStudents
        notifyDataSetChanged()
    }
}
