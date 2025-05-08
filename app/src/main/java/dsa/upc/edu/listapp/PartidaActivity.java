package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import dsa.upc.edu.listapp.store.Partida;

public class PartidaActivity extends AppCompatActivity {

    private TextView textViewPartidaInfo;
    private Button btnTienda, btnInventario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        textViewPartidaInfo = findViewById(R.id.textViewPartidaInfo);
        btnTienda           = findViewById(R.id.btnTienda);
        btnInventario       = findViewById(R.id.btnInventario);

        // Recuperamos el objeto Partida
        Partida partida = (Partida) getIntent().getSerializableExtra("partida");
        if (partida != null) {
            String info = "ID: "       + partida.getId_partida() + "\n"
                    + "Usuario: " + partida.getId_usuario() + "\n"
                    + "Vidas: "   + partida.getVidas()      + "\n"
                    + "Monedas: " + partida.getMonedas()    + "\n"
                    + "Puntuación: " + partida.getPuntuacion();
            textViewPartidaInfo.setText(info);

            String idPartida = partida.getId_partida();

            btnTienda.setOnClickListener(v -> {
                Intent intent = new Intent(PartidaActivity.this, StoreActivity.class);
                intent.putExtra("idPartida", idPartida);
                startActivity(intent);
            });

            btnInventario.setOnClickListener(v -> {
                Intent intent = new Intent(PartidaActivity.this, InventarioActivity.class);
                intent.putExtra("idPartida", idPartida);
                startActivity(intent);
            });

        } else {
            textViewPartidaInfo.setText("No se pudo cargar la partida.");
            // Deshabilitar botones si no hay partida
            btnTienda.setEnabled(false);
            btnInventario.setEnabled(false);
        }
    }
}

