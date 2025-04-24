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

    @GET("tienda/secciones")
    Call<List<Seccion>> getAllSecciones();

    @GET("tienda/productos/seccion/{nombre}")
    Call<List<Producto>> getProductosPorSeccion(@Path("nombre") String nombreSeccion);

    @POST("tienda/productos/seccion/{seccion}")
    Call<Void> addProducto(@Path("seccion") String nombreSeccion, @Body Producto producto);

    @DELETE("tienda/productos/{id}")
    Call<Void> eliminarProducto(@Path("id") String idProducto);

    @GET("tienda/productos/buscar")
    Call<List<Producto>> searchProductos(@Query("nombre") String nombre);
}


