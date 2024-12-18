package com.example.gestion_absences

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val users: List<User>, private val onClick: (User) -> Unit, private val onDelete: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.usernameTextView)
        val email: TextView = view.findViewById(R.id.emailTextView)
        val userImage: CircleImageView = view.findViewById(R.id.userImage)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        val updateButton: ImageButton = view.findViewById(R.id.updateButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.username.text = user.username
        holder.email.text = user.email

        // Vérifier le rôle de l'utilisateur et définir l'image de profil en conséquence
        if (user.role == "admin") {
            holder.userImage.setImageResource(R.drawable.ic_user_placeholder) // Image pour les admins
        } else if (user.role == "teacher") {
            holder.userImage.setImageResource(R.drawable.ic_teacher_placeholder) // Image pour les teachers
        } else {
            holder.userImage.setImageResource(R.drawable.ic_user_placeholder) // Image par défaut
        }

        // Gérer les clics sur les boutons
        holder.itemView.setOnClickListener { onClick(user) }

        // Gestion du clic sur le bouton de suppression
        holder.deleteButton.setOnClickListener {
            onDelete(user)  // Passer l'objet user à la méthode de suppression
        }


        // Exemple de gestion du clic sur le bouton de mise à jour
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditUserActivity::class.java)
            intent.putExtra("user", user) // Passer l'objet User à EditUserActivity
            holder.itemView.context.startActivity(intent)
        }


    }

    override fun getItemCount() = users.size
}
