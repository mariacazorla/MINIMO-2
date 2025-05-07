package dsa.upc.edu.listapp.auth;

import java.util.List;

import dsa.upc.edu.listapp.store.Partida;

public class Usuario {
    private String nombreUsu;
    private String password;
    private List<Partida> partidas;

    public Usuario() {
        // Necesario para Retrofit
    }

    public Usuario(String nombreUsu, String password) {
        this.nombreUsu = nombreUsu;
        this.password = password;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }
}

