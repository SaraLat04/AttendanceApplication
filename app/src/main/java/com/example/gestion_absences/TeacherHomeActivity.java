package com.example.gestion_absences;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        // Récupérer les données de l'intent
        String accessToken = getIntent().getStringExtra("ACCESS_TOKEN");
        int userId = getIntent().getIntExtra("USER_ID", -1);

        // Configurer le message de bienvenue
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Bienvenue dans l'espace des enseignants");

        // Bouton "Voir mes séances"
        Button viewSessionsButton = findViewById(R.id.viewSessionsButton);
        viewSessionsButton.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherHomeActivity.this, TeacherActivity.class);
            intent.putExtra("ACCESS_TOKEN", accessToken);
            intent.putExtra("USER_ID", userId); // Passez l'ID utilisateur
            startActivity(intent);
        });

        // Bouton "Déconnexion"
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Efface l'historique
            startActivity(intent);
            finish();
        });
    }
}
