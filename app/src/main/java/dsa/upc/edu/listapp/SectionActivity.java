package dsa.upc.edu.listapp;

import android.os.Bundle;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        // Recupera la sección
        categoria = getIntent().getStringExtra("sectionName");
        if (categoria == null) {
            Toast.makeText(this, "Categoría no especificada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setTitle(categoria);

        // Inicializa vista
        rv = findViewById(R.id.rvProducts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        swipe = findViewById(R.id.swipeRefreshProducts);

        // Adapter y listener de "Comprar"
        adapter = new ProductAdapter();
        adapter.setOnBuyClickListener(producto -> {
            String idProd = producto.getId();
            if (idProd == null || idProd.isEmpty()) {
                Toast.makeText(this, "ID de producto inválido", Toast.LENGTH_SHORT).show();
            } else {
                agregarProductoAlCarrito(idProd);
            }
        });
        rv.setAdapter(adapter);

        // Cliente Retrofit con token
        api = ApiClient.getClient(this).create(StoreAPI.class);

        // Pull-to-refresh
        swipe.setOnRefreshListener(this::loadProducts);

        // Carga inicial
        loadProducts();
    }

    /** Carga productos de la categoría desde /tienda/categorias/{categoria} */
    private void loadProducts() {
        swipe.setRefreshing(true);
        api.getProductosPorCategoria(categoria).enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> resp) {
                swipe.setRefreshing(false);
                if (resp.isSuccessful() && resp.body() != null) {
                    adapter.setData(resp.body());
                } else {
                    Toast.makeText(SectionActivity.this, "Error cargando productos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                swipe.setRefreshing(false);
                Toast.makeText(SectionActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /** Llama al endpoint POST /carrito/{id_producto} */
    private void agregarProductoAlCarrito(String idProducto) {
        api.agregarProductoAlCarrito(idProducto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resp) {
                if (resp.isSuccessful()) {
                    Toast.makeText(SectionActivity.this, "Añadido al carrito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SectionActivity.this, "No se pudo añadir al carrito", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SectionActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

