package dsa.upc.edu.listapp;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.List;
import dsa.upc.edu.listapp.store.*;
import retrofit2.*;

public class SectionActivity extends AppCompatActivity {
    private RecyclerView rv;
    private SwipeRefreshLayout swipe;
    private ProductAdapter adapter;
    private String sectionName;
    private static final String TAG="SectionActivity";

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_section);
        sectionName=getIntent().getStringExtra("sectionName");
        setTitle(sectionName);

        rv=findViewById(R.id.rvProducts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ProductAdapter();
        rv.setAdapter(adapter);

        adapter = new ProductAdapter();
        rv.setAdapter(adapter);

        swipe=findViewById(R.id.swipeRefreshProducts);
        swipe.setOnRefreshListener(this::loadProducts);

        Button btnAdd = findViewById(R.id.btnAddProduct);
        btnAdd.setOnClickListener(v -> {
            // Aquí lanzas la actividad para añadir un producto
            Intent intent = new Intent(SectionActivity.this, AddProductActivity.class);
            intent.putExtra("sectionName", sectionName);
            startActivity(intent);
        });

        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder vh, RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder vh, int direction) {
                        int pos = vh.getAdapterPosition();
                        Producto toDelete = adapter.getProductAt(pos);
                        deleteProduct(toDelete.getId(), pos);
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rv);


        loadProducts();

    }

    private void loadProducts(){
        swipe.setRefreshing(true);
        API.getStoreAPI()
                .getProductosPorSeccion(sectionName)
                .enqueue(new Callback<List<Producto>>(){
                    @Override public void onResponse(Call<List<Producto>> c, Response<List<Producto>> r){
                        swipe.setRefreshing(false);
                        if(r.isSuccessful()&&r.body()!=null){
                            adapter.setData(r.body());
                        } else Log.e(TAG,"GET Prod err "+r.code());
                    }
                    @Override public void onFailure(Call<List<Producto>> c, Throwable t){
                        swipe.setRefreshing(false);
                        Toast.makeText(SectionActivity.this,"Error red",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteProduct(String idProducto, int position) {
        API.getStoreAPI()
                .eliminarProducto(idProducto)   // Solo pasamos el ID
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> c, Response<Void> r) {
                        if (r.isSuccessful()) {
                            adapter.remove(position);
                            Toast.makeText(SectionActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                            adapter.notifyItemChanged(position);
                            Toast.makeText(SectionActivity.this,
                                    "Error al eliminar ("+r.code()+")", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> c, Throwable t) {
                        adapter.notifyItemChanged(position);
                        Toast.makeText(SectionActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Producto");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputNombre = new EditText(this);
        inputNombre.setHint("Nombre del producto");
        layout.addView(inputNombre);

        final EditText inputPrecio = new EditText(this);
        inputPrecio.setHint("Precio");
        inputPrecio.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputPrecio);

        builder.setView(layout);

        builder.setPositiveButton("Añadir", (dialog, which) -> {
            String nombre = inputNombre.getText().toString();
            String precioStr = inputPrecio.getText().toString();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(precioStr);
            Producto nuevo = new Producto();
            nuevo.setNombre(nombre);
            nuevo.setPrecio(precio);

            API.getStoreAPI()
                    .addProducto(sectionName, nuevo)
                    .enqueue(new Callback<Void>() {
                        @Override public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(SectionActivity.this, "Producto añadido", Toast.LENGTH_SHORT).show();
                                loadProducts(); // recargar lista
                            } else {
                                Toast.makeText(SectionActivity.this, "Error al añadir ("+response.code()+")", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(SectionActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

}
