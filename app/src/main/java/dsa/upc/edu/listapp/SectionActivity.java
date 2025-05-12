package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import dsa.upc.edu.listapp.auth.ApiClient;
import dsa.upc.edu.listapp.store.Producto;
import dsa.upc.edu.listapp.store.StoreAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ProductAdapter adapter;
    private SwipeRefreshLayout swipe;
    private StoreAPI api;
    private String categoria;
    private String idPartida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        FloatingActionButton fabOpenMenu = findViewById(R.id.fabOpenMenu);
        fabOpenMenu.setOnClickListener(v -> {
            NavigationBottomSheet.showNavigationMenu(this, idPartida);
        });

        // Recupera sección e ID de partida
        categoria  = getIntent().getStringExtra("sectionName");
        idPartida  = getIntent().getStringExtra("idPartida");
        if (categoria == null || idPartida == null) {
            Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setTitle(categoria);

        // Botón Carrito
        Button btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            Intent i = new Intent(SectionActivity.this, CarritoActivity.class);
            i.putExtra("idPartida", idPartida);
            startActivity(i);
        });

        // RecyclerView + SwipeRefresh
        rv    = findViewById(R.id.rvProducts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        swipe = findViewById(R.id.swipeRefreshProducts);

        // Adapter “Comprar”
        adapter = new ProductAdapter();
        adapter.setOnBuyClickListener(prod -> {
            String id = prod.getId();
            if (id == null || id.isEmpty()) {
                Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show();
            } else {
                api.agregarProductoAlCarrito(id)
                        .enqueue(new Callback<Void>() {
                            @Override public void onResponse(Call<Void> c, Response<Void> r) {
                                Toast.makeText(SectionActivity.this,
                                        r.isSuccessful() ? "Añadido" : "Fallo al añadir",
                                        Toast.LENGTH_SHORT).show();
                            }
                            @Override public void onFailure(Call<Void> c, Throwable t) {
                                Toast.makeText(SectionActivity.this,
                                        "Error red", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        rv.setAdapter(adapter);

        // Retrofit con interceptor
        api = ApiClient.getClient(this).create(StoreAPI.class);

        swipe.setOnRefreshListener(this::loadProducts);
        loadProducts();
    }

    private void loadProducts() {
        swipe.setRefreshing(true);
        api.getProductosPorCategoria(categoria).enqueue(new Callback<List<Producto>>() {
            @Override public void onResponse(Call<List<Producto>> c, Response<List<Producto>> r) {
                swipe.setRefreshing(false);
                if (r.isSuccessful() && r.body() != null) {
                    adapter.setData(r.body());
                } else {
                    Toast.makeText(SectionActivity.this, "Error cargando", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<List<Producto>> c, Throwable t) {
                swipe.setRefreshing(false);
                Toast.makeText(SectionActivity.this, "Error red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

