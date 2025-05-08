// dsa/upc/edu/listapp/store/StoreAPI.java
package dsa.upc.edu.listapp.store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface StoreAPI {

    @GET("tienda/categorias")
    Call<List<String>> getAllSecciones();

    @GET("tienda/categorias/{categoria}")
    Call<List<Producto>> getProductosPorCategoria(@Path("categoria") String categoria);




    @POST("tienda/productos/seccion/{seccion}")
    Call<Void> addProducto(@Path("seccion") String nombreSeccion, @Body Producto producto);

    @DELETE("tienda/productos/{id}")
    Call<Void> eliminarProducto(@Path("id") String idProducto);

    @GET("tienda/productos/buscar")
    Call<List<Producto>> searchProductos(@Query("nombre") String nombre);

    @GET("tienda/productoAleatorio")
    Call<Objeto> getProductoAleatorio();


    @POST("carrito/comprarAleatorio/{id_partida}")
    Call<Objeto> comprarProductoAleatorio(@Path("id_partida") String idPartida);



    // Partidas

    @GET("partidas")
    Call<List<Partida>> getPartidas();

    @POST("partidas")
    Call<Partida> crearPartida();



    @DELETE("partidas/{id_partida}")
    Call<Void> deletePartida(@Path("id_partida") String idPartida);



    @GET("partidas/monedas/{id_partida}")
    Call<MonedasResponse> getMonedas(@Path("id_partida") String idPartida);

    // Carrito

    @GET("carrito")
    Call<List<Producto>> getProductosDelCarrito();

    @POST("carrito/{id_producto}")
    Call<Void> agregarProductoAlCarrito(@Path("id_producto") String idProducto);

    @DELETE("carrito/{id_producto}")
    Call<Void> eliminarProductoDelCarrito(@Path("id_producto") String idProducto);

    @POST("carrito/comprar/{id_partida}")
    Call<Void> realizarCompra(@Path("id_partida") String idPartida);

    @DELETE("carrito")
    Call<Void> vaciarCarrito();

    //Inventario

    @GET("partidas/{id_partida}")
    Call<Partida> getPartidaDetalle(@Path("id_partida") String idPartida);




}


