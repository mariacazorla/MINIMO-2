package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import dsa.upc.edu.listapp.auth.ApiClient;
import dsa.upc.edu.listapp.store.Seccion;
import dsa.upc.edu.listapp.store.StoreAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {
    private String idPartida;
    private SwipeRefreshLayout swipe;
    private SectionAdapter adapter;
    private static final String TAG = "StoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // o activity_store si es tu layout

        // 1) Recupera el id de partida
        idPartida = getIntent().getStringExtra("idPartida");

        // 2) Setup RecyclerView + Adapter (campo, no variable local)
        RecyclerView rv = findViewById(R.id.rvSections);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SectionAdapter(name -> {
            Intent i = new Intent(StoreActivity.this, SectionActivity.class);
            // name ya es la categoría (String)
            i.putExtra("sectionName", name);
            i.putExtra("idPartida", idPartida);
            startActivity(i);
        });
        rv.setAdapter(adapter);

        // 3) Swipe to refresh
        swipe = findViewById(R.id.swipeRefreshLayout);
        swipe.setOnRefreshListener(this::loadSections);

        // 4) Carga inicial
        loadSections();
    }

    private void loadSections() {
        Log.d(TAG, "Lanzando petición a /tienda/categorias");
        swipe.setRefreshing(true);

        StoreAPI api = ApiClient.getClient(this).create(StoreAPI.class);
        api.getAllSecciones().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> resp) {
                swipe.setRefreshing(false);
                if (resp.isSuccessful() && resp.body() != null) {
                    adapter.setDataFromStrings(resp.body());
                } else {
                    Toast.makeText(StoreActivity.this, "Error al cargar secciones", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                swipe.setRefreshing(false);
                Toast.makeText(StoreActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
