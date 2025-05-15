package dsa.upc.edu.listapp.store;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Objeto implements Serializable {
    private String id_objeto;
    private String nombre;
    private int precio;
    private CategoriaObjeto categoria;

    @SerializedName("imagen")
    private String imageUrl;



    public String getId_objeto() {
        return id_objeto;
    }

    public void setId_objeto(String id_objeto) {
        this.id_objeto = id_objeto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public CategoriaObjeto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaObjeto categoria) {
        this.categoria = categoria;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Objeto{" +
                "id_objeto='" + id_objeto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoria=" + categoria +
                '}';
    }
}
