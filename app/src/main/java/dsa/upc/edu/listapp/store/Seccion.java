package dsa.upc.edu.listapp.store;

import java.io.Serializable;
import java.util.List;

public class Seccion {
    private String nombre;
    private List<Producto> productos;

    public Seccion() {}
    public Seccion(String nombre, List<Producto> productos) {
        this.nombre = nombre;
        this.productos = productos;
    }
    public String getNombre() { return nombre; }
    public List<Producto> getProductos() { return productos; }
}