// dsa/upc/edu/listapp/store/StoreAPI.java
package dsa.upc.edu.listapp.store;

import java.util.List;

import dsa.upc.edu.listapp.MonedasResponse;
import dsa.upc.edu.listapp.auth.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface StoreAPI {

    @GET("tienda/categorias")
    Call<List<Seccion>> getAllSecciones();

    @GET("tienda/categorias/{categoria}")
    Call<List<Producto>> getProductosPorSeccion(@Path("nombre") String nombreSeccion);

    @POST("tienda/productos/seccion/{seccion}")
    Call<Void> addProducto(@Path("seccion") String nombreSeccion, @Body Producto producto);

    @DELETE("tienda/productos/{id}")
    Call<Void> eliminarProducto(@Path("id") String idProducto);

    @GET("tienda/productos/buscar")
    Call<List<Producto>> searchProductos(@Query("nombre") String nombre);


    // Partidas

    @GET("partidas")
    Call<List<Partida>> getPartidas();

    @POST("partidas")
    Call<Partida> crearPartida();



    @DELETE("partidas/{id_partida}")
    Call<Void> deletePartida(@Path("id_partida") String idPartida);



    @GET("partidas/monedas/{id_partida}")
    Call<MonedasResponse> getMonedas(@Path("id_partida") String idPartida);



}


