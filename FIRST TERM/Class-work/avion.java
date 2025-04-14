public class Avion {
    private String placa;
    private boolean enMision;
    private int puntaje; // Puntaje que otorga el avión

    // Constructor
    public Avion(String placa, boolean enMision, int puntaje) {
        this.placa = placa;
        this.enMision = enMision;
        this.puntaje = puntaje;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
