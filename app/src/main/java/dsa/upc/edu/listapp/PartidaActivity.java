package dsa.upc.edu.listapp;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import dsa.upc.edu.listapp.store.Partida;

public class PartidaActivity extends AppCompatActivity {

    private TextView textViewPartidaInfo;
    private Button btnTienda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        textViewPartidaInfo = findViewById(R.id.textViewPartidaInfo);
        btnTienda = findViewById(R.id.btnTienda);

        Partida partida = (Partida) getIntent().getSerializableExtra("partida");

        if (partida != null) {
            textViewPartidaInfo.setText(
                    "ID: " + partida.getId_partida() + "\n" +
                            "Usuario: " + partida.getId_usuario() + "\n" +
                            "Vidas: " + partida.getVidas() + "\n" +
                            "Monedas: " + partida.getMonedas() + "\n" +
                            "Puntuación: " + partida.getPuntuacion()
            );
        } else {
            textViewPartidaInfo.setText("No se pudo cargar la partida.");
        }

        btnTienda.setOnClickListener(v -> {
            Intent intent = new Intent(PartidaActivity.this, StoreActivity.class);
            startActivity(intent);
        });
    }
}
