package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import dsa.upc.edu.listapp.auth.ApiClient;

public class PartidasMenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PartidaAdapter adapter;
    private StoreAPI api;
    private List<Partida> partidas;
    private Button btnCrearPartida, btnVerInsignias;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas_menu);

        FloatingActionButton fabOpenMenu = findViewById(R.id.fabOpenMenu);
        fabOpenMenu.setOnClickListener(v ->
                NavigationBottomSheet.showNavigationMenu(this, null)
        );

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerViewPartidas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnCrearPartida = findViewById(R.id.btnCrearPartida);
        api = ApiClient.getClient(this).create(StoreAPI.class);

        // --- Botón para abrir pantalla de insignias ---
        btnVerInsignias = findViewById(R.id.buttonInsignias);
        btnVerInsignias.setOnClickListener(v -> {
            Intent intent = new Intent(PartidasMenuActivity.this, InsigniasActivity.class);
            startActivity(intent);
        });
        // ----------------------------------------------

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
                new AlertDialog.Builder(PartidasMenuActivity.this)
                        .setTitle("Eliminar partida")
                        .setMessage(
                                partida.getInventario() == null || partida.getInventario().isEmpty()
                                        ? "¿Seguro que quieres eliminar esta partida vacía?"
                                        : "Esta partida tiene objetos comprados.\n¿Seguro que quieres eliminarla?"
                        )
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Sí, eliminar", (d, w) ->
                                borrarPartida(partida.getId_partida(), position)
                        )
                        .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);
        cargarPartidas();

        btnCrearPartida.setOnClickListener(v ->
                api.crearPartida().enqueue(new Callback<Partida>() {
                    @Override
                    public void onResponse(Call<Partida> call, Response<Partida> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            partidas.add(response.body());
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
                })
        );
    }

    private void cargarPartidas() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        api.getPartidas().enqueue(new Callback<List<Partida>>() {
            @Override
            public void onResponse(Call<List<Partida>> call, Response<List<Partida>> resp) {
                progressBar.setVisibility(ProgressBar.GONE);
                if (resp.isSuccessful() && resp.body() != null) {
                    partidas.clear();
                    partidas.addAll(resp.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PartidasMenuActivity.this, "No se pudieron cargar las partidas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Partida>> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                Toast.makeText(PartidasMenuActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void borrarPartida(String idPartida, int position) {
        api.deletePartida(idPartida).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resp) {
                if (resp.isSuccessful()) {
                    Toast.makeText(PartidasMenuActivity.this, "Partida eliminada", Toast.LENGTH_SHORT).show();
                    cargarPartidas();
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
