package dsa.upc.edu.listapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import dsa.upc.edu.listapp.store.*;
import retrofit2.*;
import java.util.UUID;

public class AddProductActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        EditText etNombre = findViewById(R.id.etNombre);
        EditText etPrecio = findViewById(R.id.etPrecio);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        String sectionName = getIntent().getStringExtra("sectionName");

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String precioStr = etPrecio.getText().toString();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(precioStr);

            // Generar un ID aleatorio para el producto
            String id = UUID.randomUUID().toString(); // Generar un UUID aleatorio

            // Crear el nuevo producto con el ID generado, nombre y precio
            Producto nuevo = new Producto();
            nuevo.setId(id);  // Establecer el ID aleatorio
            nuevo.setNombre(nombre);
            nuevo.setPrecio(precio);

            // Llamar a la API para añadir el producto
            API.getStoreAPI().addProducto(sectionName, nuevo)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AddProductActivity.this, "Producto añadido", Toast.LENGTH_SHORT).show();
                                finish(); // Volver atrás
                            } else {
                                Toast.makeText(AddProductActivity.this, "Error al añadir producto", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(AddProductActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
