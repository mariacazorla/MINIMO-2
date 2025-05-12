package dsa.upc.edu.listapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private Button btnCrearPartida, btnLogout;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas_menu);
        FloatingActionButton fabOpenMenu = findViewById(R.id.fabOpenMenu);
        fabOpenMenu.setOnClickListener(v -> {
            NavigationBottomSheet.showNavigationMenu(this, null);
        });

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerViewPartidas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnCrearPartida = findViewById(R.id.btnCrearPartida);

        api = ApiClient.getClient(PartidasMenuActivity.this).create(StoreAPI.class);

        partidas = new ArrayList<>();

        adapter = new PartidaAdapter(partidas, new PartidaAdapter.OnPartidaClickListener() {
            @Override
            public void onClick(Partida partida) {
                Intent intent = new Intent(PartidasMenuActivity.this, PartidaActivity.class);
                intent.putExtra("partida", partida);
                startActivity(intent);
            }

            @Override
            public void onEliminarPartida(Partida partida, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PartidasMenuActivity.this);
                builder.setTitle("Eliminar partida")
                        .setMessage(partida.getInventario() == null || partida.getInventario().isEmpty()
                                ? "¿Seguro que quieres eliminar esta partida vacía?"
                                : "Esta partida tiene objetos comprados.\n¿Seguro que quieres eliminarla?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                            borrarPartida(partida.getId_partida(), position);
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> {
                            dialog.dismiss();
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        recyclerView.setAdapter(adapter);

        cargarPartidas();

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
        progressBar.setVisibility(View.VISIBLE); // Mostrar spinner al empezar

        api.getPartidas().enqueue(new Callback<List<Partida>>() {
            @Override
            public void onResponse(Call<List<Partida>> call, Response<List<Partida>> response) {
                progressBar.setVisibility(View.GONE); // Ocultar spinner al recibir respuesta

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
                progressBar.setVisibility(View.GONE); // También ocultar en caso de fallo
                Toast.makeText(PartidasMenuActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void borrarPartida(String idPartida, int position) {
        api.deletePartida(idPartida).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PartidasMenuActivity.this, "Partida eliminada", Toast.LENGTH_SHORT).show();
                    cargarPartidas(); // Recargar TODAS las partidas desde el servidor
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
}
