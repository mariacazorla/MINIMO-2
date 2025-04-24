package dsa.upc.edu.listapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import dsa.upc.edu.listapp.store.API;
import dsa.upc.edu.listapp.store.Producto;
import dsa.upc.edu.listapp.store.Seccion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {
    private RecyclerView rv;
    private SwipeRefreshLayout swipe;
    private SectionAdapter adapter;
    private ProductAdapter productAdapter;
    private SearchView searchView;


    private static final String TAG="MainActivity";

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        Log.d(TAG, "onCreate de MainActivity arrancado");

        setContentView(R.layout.activity_main);
        rv=findViewById(R.id.rvSections);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SectionAdapter(sec-> {
            Intent i=new Intent(StoreActivity.this, SectionActivity.class);
            i.putExtra("sectionName", sec.getNombre());
            startActivity(i);
        });
        rv.setAdapter(adapter);
/*
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);  // Realizar la búsqueda cuando se envíe la consulta
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadSections();  // Si el texto está vacío, mostrar todos los productos
                } else {
                    searchProducts(newText);  // Realizar la búsqueda mientras el usuario escribe
                }
                return true;
            }
        });

 */


        swipe=findViewById(R.id.swipeRefreshLayout);
        swipe.setOnRefreshListener(this::loadSections);

        loadSections();




    }

    private void loadSections(){
        Log.d(TAG, "Lanzando petición a /secciones");

        swipe.setRefreshing(true);
        API.getStoreAPI().getAllSecciones().enqueue(new Callback<List<Seccion>>(){
            @Override public void onResponse(Call<List<Seccion>> c, Response<List<Seccion>> r){
                swipe.setRefreshing(false);
                Log.d(TAG, "onResponse con código " + r.code());

                if(r.isSuccessful() && r.body()!=null){
                    adapter.setData(r.body());
                } else Log.e(TAG,"GET Sec err "+r.code());
            }
            @Override public void onFailure(Call<List<Seccion>> c, Throwable t){
                swipe.setRefreshing(false);
                Log.e(TAG, "Error al cargar secciones", t);
                Toast.makeText(StoreActivity.this,"Error red",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProducts(String query) {
        swipe.setRefreshing(true);
        API.getStoreAPI()
                .searchProductos(query)  // Usamos el método de búsqueda en la API
                .enqueue(new Callback<List<Producto>>() {
                    @Override
                    public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                        swipe.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            productAdapter.setData(response.body());
                        } else {
                            Toast.makeText(StoreActivity.this, "No se encontraron productos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Producto>> call, Throwable t) {
                        swipe.setRefreshing(false);
                        Toast.makeText(StoreActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
