package dsa.upc.edu.listapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dsa.upc.edu.listapp.auth.ApiClient;
import dsa.upc.edu.listapp.store.Objeto;
import dsa.upc.edu.listapp.store.StoreAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomActivity extends AppCompatActivity {
    private static final String TAG = "RandomActivity";

    //private TextView tvNombre, tvCategoria, tvPrecio;
    private ImageView ivIcono;
    private Button btnComprarAleatorio;

    private String idPartida;
    private Objeto productoMostrado; // para mostrar qué compramos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        // Inicializar vistas
        //tvNombre = findViewById(R.id.tvNombreProducto);
        //tvCategoria = findViewById(R.id.tvCategoriaProducto);
        //tvPrecio = findViewById(R.id.tvPrecioProducto);
        ivIcono = findViewById(R.id.ivIconoProducto);
        btnComprarAleatorio = findViewById(R.id.btnComprarAleatorio);

        // Obtener id de partida
        idPartida = getIntent().getStringExtra("idPartida");
        if (idPartida == null) {
            Toast.makeText(this, "ID de partida no disponible", Toast.LENGTH_SHORT).show();
            finish(); // salir de la actividad si no se puede continuar
            return;
        }

        // Cargar producto aleatorio
        cargarProductoAleatorio();

        // Configurar compra
        btnComprarAleatorio.setOnClickListener(v -> realizarCompraAleatoria());
    }

    private void cargarProductoAleatorio() {
        StoreAPI api = ApiClient.getClient(this).create(StoreAPI.class);
        api.getProductoAleatorio().enqueue(new Callback<Objeto>() {
            @Override
            public void onResponse(Call<Objeto> call, Response<Objeto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productoMostrado = response.body();
                    //tvNombre.setText(productoMostrado.getNombre());
                    //tvCategoria.setText("Categoría: " + productoMostrado.getCategoria());
                    //tvPrecio.setText("Precio: " + productoMostrado.getPrecio() + " monedas");

                    ivIcono.setImageResource(android.R.drawable.ic_menu_help); // o usa Glide si tienes URL
                } else {
                    Toast.makeText(RandomActivity.this, "No se pudo obtener el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Objeto> call, Throwable t) {
                Log.e(TAG, "Error al obtener producto aleatorio", t);
                Toast.makeText(RandomActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void realizarCompraAleatoria() {
        StoreAPI api = ApiClient.getClient(this).create(StoreAPI.class);
        api.comprarProductoAleatorio(idPartida).enqueue(new Callback<Objeto>() {
            @Override
            public void onResponse(Call<Objeto> call, Response<Objeto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Objeto comprado = response.body();
                    //Toast.makeText(RandomActivity.this, "¡Compraste: " + comprado.getNombre() + "!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RandomActivity.this, "No se pudo realizar la compra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Objeto> call, Throwable t) {
                Log.e(TAG, "Error al realizar compra aleatoria", t);
                Toast.makeText(RandomActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
