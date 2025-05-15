package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dsa.upc.edu.listapp.store.Partida;

public class PartidaActivity extends AppCompatActivity {

    private String idPartida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idPartida = getIntent().getStringExtra("idPartida");
        setContentView(R.layout.activity_partida);

        // Botón flotante para abrir menú inferior
        FloatingActionButton fabOpenMenu = findViewById(R.id.fabOpenMenu);
        fabOpenMenu.setOnClickListener(v -> {
            NavigationBottomSheet.showNavigationMenu(this, idPartida);
        });

        // Botones tienda e inventario
        ImageButton btnTienda = findViewById(R.id.btnTienda);
        ImageButton btnInventario = findViewById(R.id.btnInventario);

        // Botón de volver
        Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(view -> finish());

        // Nuevos TextView individuales
        TextView tvIdPartida   = findViewById(R.id.tvIdPartida);
        TextView tvUsuario     = findViewById(R.id.tvUsuario);
        TextView tvVidas       = findViewById(R.id.tvVidas);
        TextView tvMonedas     = findViewById(R.id.tvMonedas);
        TextView tvPuntuacion  = findViewById(R.id.tvPuntuacion);

        // Recuperamos el objeto Partida
        Partida partida = (Partida) getIntent().getSerializableExtra("partida");

        if (partida != null) {
            // Mostramos info en los TextViews
            tvIdPartida.setText("ID: " + partida.getId_partida());
            tvUsuario.setText("Usuario: " + partida.getId_usuario());
            tvVidas.setText("Vidas: " + partida.getVidas());
            tvMonedas.setText("Monedas: " + partida.getMonedas());
            tvPuntuacion.setText("Puntuación: " + partida.getPuntuacion());

            String idPartida = partida.getId_partida();

            // Ir a Tienda
            btnTienda.setOnClickListener(v -> {
                Intent intent = new Intent(PartidaActivity.this, StoreActivity.class);
                intent.putExtra("idPartida", idPartida);
                startActivity(intent);
            });

            // Ir a Inventario
            btnInventario.setOnClickListener(v -> {
                Intent intent = new Intent(PartidaActivity.this, InventarioActivity.class);
                intent.putExtra("idPartida", idPartida);
                startActivity(intent);
            });

        } else {
            // Si no hay partida, mostrar mensaje y ocultar campos
            tvIdPartida.setText("No se pudo cargar la partida.");
            tvUsuario.setVisibility(View.GONE);
            tvVidas.setVisibility(View.GONE);
            tvMonedas.setVisibility(View.GONE);
            tvPuntuacion.setVisibility(View.GONE);

            btnTienda.setEnabled(false);
            btnInventario.setEnabled(false);
        }
    }
}


