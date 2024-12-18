package com.example.gestion_absences;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClassApi classApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        classApi = ApiClient.getClient().create(ClassApi.class);

        // Récupérez l'ID utilisateur de l'intent
        int teacherId = getIntent().getIntExtra("USER_ID", -1);
        if (teacherId != -1) {
            fetchClassesForTeacher(teacherId);
        } else {
            Toast.makeText(this, "Erreur : ID enseignant manquant.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchClassesForTeacher(int teacherId) {
        classApi.getClassesForTeacher(teacherId).enqueue(new Callback<List<Classe>>() {
            @Override
            public void onResponse(Call<List<Classe>> call, Response<List<Classe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Classe> classes = response.body();
                    // Passez les données au RecyclerView Adapter
                    TeacherClassAdapter adapter = new TeacherClassAdapter(classes);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(TeacherActivity.this, "Aucune classe trouvée.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Classe>> call, Throwable t) {
                Toast.makeText(TeacherActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
