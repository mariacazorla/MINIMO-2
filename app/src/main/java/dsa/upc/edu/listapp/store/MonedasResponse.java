package dsa.upc.edu.listapp.store;

public class MonedasResponse {
    private int monedas;

    // Gson necesita un constructor vacío
    public MonedasResponse() {}

    // Getter para que CarritoActivity pueda usar resp.body().getMonedas()
    public int getMonedas() {
        return monedas;
    }

    // (Opcional) Setter si alguna vez necesitas serializar de vuelta
    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }
}
