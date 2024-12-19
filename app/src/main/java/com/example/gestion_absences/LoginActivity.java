package com.example.gestion_absences;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;
import android.widget.EditText;
import com.example.gestion_absences.ApiClient;

public class LoginActivity extends AppCompatActivity {

    private StudentService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getClient().create(StudentService.class);

        findViewById(R.id.loginButton).setOnClickListener(view -> {
            // Utilisez les identifiants corrects ici
            String email = ((EditText) findViewById(R.id.username)).getText().toString().trim();
            String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();

            Log.d("LoginDebug", "Email: " + email + ", Password: " + password);

            UserAuth userAuth = new UserAuth(email, password);
            if (validateFields(email, password)) {
                login(userAuth);
            } else {
                Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(UserAuth userAuth) {
        apiService.login(userAuth).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    String role = authResponse.getRole();
                    int userId = authResponse.getId(); // Récupération de l'ID utilisateur

                    Intent intent;

                    if ("admin".equals(role)) {
                        intent = new Intent(LoginActivity.this, AdminsActivity.class);
                    } else if ("teacher".equals(role)) {
                        intent = new Intent(LoginActivity.this, TeacherHomeActivity.class);
                        intent.putExtra("USER_ID", userId); // Passez l'ID utilisateur
                    } else {
                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                    }

                    intent.putExtra("ACCESS_TOKEN", authResponse.getAccess_token());
                    intent.putExtra("ROLE", role);

                    startActivity(intent);
                    finish();

                    Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Échec de la connexion : Identifiants invalides.", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean validateFields(String email, String password) {
        return !email.trim().isEmpty() && !password.trim().isEmpty();
    }
}