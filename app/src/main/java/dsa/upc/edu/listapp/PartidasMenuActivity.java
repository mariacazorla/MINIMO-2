package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dsa.upc.edu.listapp.store.Partida;
import dsa.upc.edu.listapp.store.StoreAPI;
import dsa.upc.edu.listapp.store.PartidaAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import dsa.upc.edu.listapp.auth.*;

public class PartidasMenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PartidaAdapter adapter;
    private StoreAPI api;
    private List<Partida> partidas;
    private Button btnCrearPartida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas_menu);

        recyclerView = findViewById(R.id.recyclerViewPartidas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnCrearPartida = findViewById(R.id.btnCrearPartida);

        api = ApiClient.getClient(PartidasMenuActivity.this).create(StoreAPI.class);

        String nombreUsu = getIntent().getStringExtra("nombreUsu");

        partidas = new ArrayList<>();

        adapter = new PartidaAdapter(partidas, new PartidaAdapter.OnPartidaClickListener() {
            @Override
            public void onClick(Partida partida) {
                // Aquí pones la lógica para abrir la pantalla de detalles de la partida
                Intent intent = new Intent(PartidasMenuActivity.this, PartidaActivity.class);
                intent.putExtra("partida", partida);
                startActivity(intent);
            }

            @Override
            public void onEliminarPartida(String idPartida, int position) {
                // Llamar a la API para eliminar la partida
                api.deletePartida(idPartida).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            partidas.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(PartidasMenuActivity.this, "Partida eliminada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PartidasMenuActivity.this, "Error al eliminar partida", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(PartidasMenuActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        recyclerView.setAdapter(adapter);

        // Cargar partidas existentes
        cargarPartidas();

        // Listener para crear nueva partida
        btnCrearPartida.setOnClickListener(v -> {
            api.crearPartida().enqueue(new Callback<Partida>() {
                @Override
                public void onResponse(Call<Partida> call, Response<Partida> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Partida nueva = response.body();
                        partidas.add(nueva);
                        adapter.notifyItemInserted(partidas.size() - 1);
                        Toast.makeText(PartidasMenuActivity.this, "Partida creada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PartidasMenuActivity.this, "Error al crear partida", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Partida> call, Throwable t) {
                    Toast.makeText(PartidasMenuActivity.this, "Error de red al crear partida", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void cargarPartidas() {
        api.getPartidas().enqueue(new Callback<List<Partida>>() {
            @Override
            public void onResponse(Call<List<Partida>> call, Response<List<Partida>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    partidas.clear();
                    partidas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PartidasMenuActivity.this, "No se pudieron cargar las partidas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Partida>> call, Throwable t) {
                Toast.makeText(PartidasMenuActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}