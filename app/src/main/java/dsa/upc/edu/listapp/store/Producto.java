package dsa.upc.edu.listapp.store;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class Producto implements Serializable {

    /** Gson usará el valor de "id_objeto" del JSON para poblar este campo */
    @SerializedName("id_objeto")
    private String id;

    private String nombre;
    private double precio;

    @SerializedName("imagen")
    private String imageUrl;

    public Producto() {}

    public Producto(String id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public double getPrecio() {
        return precio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
