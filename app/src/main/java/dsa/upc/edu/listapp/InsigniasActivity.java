package dsa.upc.edu.listapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dsa.upc.edu.listapp.auth.ApiClient;
import dsa.upc.edu.listapp.auth.ApiService;
import dsa.upc.edu.listapp.auth.Insignia;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsigniasActivity extends AppCompatActivity {

    private RecyclerView rvInsignias;
    private InsigniaAdapter adapter;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insignias);

        // 1. RecyclerView y Adapter
        rvInsignias = findViewById(R.id.rvInsignias);
        rvInsignias.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new InsigniaAdapter();
        rvInsignias.setAdapter(adapter);

        // 2. Construir ApiService
        api = ApiClient.getClient(this).create(ApiService.class);

        // 3. Obtener el nombre de usuario guardado en SharedPreferences
        SharedPreferences prefs = getSharedPreferences("auth", Context.MODE_PRIVATE);
        String usuario = prefs.getString("nombreUsu", "");
        // <-- Asegúrate de que en tu LoginActivity hiciste:
        // prefs.edit().putString("nombreUsu", u.getNombreUsu()).apply();

        // 4. Llamar al endpoint
        api.getInsignias(usuario).enqueue(new Callback<List<Insignia>>() {
            @Override
            public void onResponse(Call<List<Insignia>> call, Response<List<Insignia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setLista(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Insignia>> call, Throwable t) {
                // Manejar fallo (p. ej. Toast)
            }
        });
    }
}
