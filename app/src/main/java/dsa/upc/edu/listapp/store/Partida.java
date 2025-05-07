package dsa.upc.edu.listapp.store;


import java.io.Serializable;
import java.util.List;

public class Partida implements Serializable{

    private String id_partida;
    private String id_usuario;
    private Integer vidas;
    private Integer monedas;
    private Integer puntuacion;
    private List<Objeto> inventario;

    public Partida() {
    }

    public Partida(String id_partida, String id_usuario, Integer vidas, Integer monedas, Integer puntuacion, List<Objeto> inventario) {
        this.id_partida = id_partida;
        this.id_usuario = id_usuario;
        this.vidas = vidas;
        this.monedas = monedas;
        this.puntuacion = puntuacion;
        this.inventario = inventario;
    }

    public String getId_partida() {
        return id_partida;
    }

    public void setId_partida(String id_partida) {
        this.id_partida = id_partida;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Integer getVidas() {
        return vidas;
    }

    public void setVidas(Integer vidas) {
        this.vidas = vidas;
    }

    public Integer getMonedas() {
        return monedas;
    }

    public void setMonedas(Integer monedas) {
        this.monedas = monedas;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<Objeto> getInventario() {
        return inventario;
    }

    public void setInventario(List<Objeto> inventario) {
        this.inventario = inventario;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id_partida='" + id_partida + '\'' +
                ", id_usuario='" + id_usuario + '\'' +
                ", vidas=" + vidas +
                ", monedas=" + monedas +
                ", puntuacion=" + puntuacion +
                ", inventario=" + inventario +
                '}';
    }
}