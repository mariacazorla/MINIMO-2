package dsa.upc.edu.listapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dsa.upc.edu.listapp.auth.ApiClient;
import dsa.upc.edu.listapp.store.Objeto;
import dsa.upc.edu.listapp.store.Partida;
import dsa.upc.edu.listapp.store.StoreAPI;
import dsa.upc.edu.listapp.store.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventarioActivity extends AppCompatActivity {

    private RecyclerView rv;
    private InventarioAdapter adapter;
    private StoreAPI api;
    private String idPartida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        idPartida = getIntent().getStringExtra("idPartida");
        if (idPartida == null) {
            Toast.makeText(this, "Partida no especificada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rv = findViewById(R.id.rvInventario);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InventarioAdapter();
        rv.setAdapter(adapter);

        api = ApiClient.getClient(this).create(StoreAPI.class);
        cargarInventario();
    }

    private void cargarInventario() {
        api.getPartidaDetalle(idPartida)
                .enqueue(new Callback<Partida>() {
                    @Override
                    public void onResponse(Call<Partida> call, Response<Partida> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            List<Objeto> inv = resp.body().getInventario();
                            adapter.setData(inv);
                        } else {
                            Toast.makeText(InventarioActivity.this,
                                    "Error cargando inventario", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Partida> call, Throwable t) {
                        Toast.makeText(InventarioActivity.this,
                                "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
