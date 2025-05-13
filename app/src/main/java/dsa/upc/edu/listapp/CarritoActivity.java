package dsa.upc.edu.listapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import dsa.upc.edu.listapp.auth.ApiClient;
import dsa.upc.edu.listapp.store.MonedasResponse;
import dsa.upc.edu.listapp.store.Producto;
import dsa.upc.edu.listapp.store.StoreAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private CarritoAdapter adapter;
    private SwipeRefreshLayout swipe;
    private TextView tvTotal, tvCoins;
    private Button btnEmptyCart, btnComprar;
    private StoreAPI api;
    private String idPartida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        FloatingActionButton fabOpenMenu = findViewById(R.id.fabOpenMenu);
        fabOpenMenu.setOnClickListener(v -> {
            NavigationBottomSheet.showNavigationMenu(this, idPartida);
        });

        // Recuperar idPartida
        idPartida = getIntent().getStringExtra("idPartida");

        rv = findViewById(R.id.rvCart);
        rv.setLayoutManager(new LinearLayoutManager(this));
        swipe = findViewById(R.id.swipeCart);
        tvTotal = findViewById(R.id.tvTotal);
        tvCoins = findViewById(R.id.tvCoins);
        btnEmptyCart = findViewById(R.id.btnEmptyCart);
        btnComprar   = findViewById(R.id.btnComprar);
        Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        api = ApiClient.getClient(this).create(StoreAPI.class);

        // Adapter con listener de eliminar
        adapter = new CarritoAdapter();
        adapter.setOnRemoveClickListener((prod, pos) -> eliminarDelCarrito(prod.getId(), pos));
        rv.setAdapter(adapter);

        // Pull-to-refresh
        swipe.setOnRefreshListener(this::cargarCarrito);

        // Vaciar carrito
        btnEmptyCart.setOnClickListener(v -> vaciarCarrito());

        // Comprar
        btnComprar.setOnClickListener(v -> realizarCompra());

        // Carga inicial
        cargarCarrito();
        actualizarMonedas();
    }

    private void cargarCarrito() {
        swipe.setRefreshing(true);
        api.getProductosDelCarrito().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> resp) {
                swipe.setRefreshing(false);
                if (resp.isSuccessful() && resp.body() != null) {
                    List<Producto> lista = resp.body();
                    adapter.setData(lista);
                    actualizarTotal(lista);
                } else {
                    Toast.makeText(CarritoActivity.this, "Error cargando carrito", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                swipe.setRefreshing(false);
                Toast.makeText(CarritoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarTotal(List<Producto> lista) {
        double total = 0;
        for (Producto p : lista) total += p.getPrecio();
        tvTotal.setText(String.format("Total: %.2f€", total));
    }

    private void eliminarDelCarrito(String idProd, int pos) {
        api.eliminarProductoDelCarrito(idProd).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resp) {
                if (resp.isSuccessful()) {
                    adapter.remove(pos);
                    // recalcular total
                    actualizarTotal(adapter.getData());
                    Toast.makeText(CarritoActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarritoActivity.this, "Error eliminando", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CarritoActivity.this, "Error red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void vaciarCarrito() {
        api.vaciarCarrito().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resp) {
                if (resp.isSuccessful()) {
                    adapter.clear();
                    actualizarTotal(adapter.getData());
                    Toast.makeText(CarritoActivity.this, "Carrito vaciado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CarritoActivity.this, "Error al vaciar", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CarritoActivity.this, "Error red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void realizarCompra() {
        api.realizarCompra(idPartida).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> resp) {
                if (resp.isSuccessful()) {
                    adapter.clear();
                    actualizarTotal(adapter.getData());
                    Toast.makeText(CarritoActivity.this, "Compra realizada", Toast.LENGTH_SHORT).show();
                    actualizarMonedas();
                } else {
                    Toast.makeText(CarritoActivity.this, "Compra fallida", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CarritoActivity.this, "Error red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Refresca también el TextView de monedas tras cambios */
    private void actualizarMonedas() {
        api.getMonedas(idPartida).enqueue(new Callback<MonedasResponse>() {
            @Override
            public void onResponse(Call<MonedasResponse> call, Response<MonedasResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    tvCoins.setText("Monedas: " + resp.body().getMonedas());
                }
            }
            @Override
            public void onFailure(Call<MonedasResponse> call, Throwable t) {
                // opcional: mostrar error
            }
        });
    }
}


