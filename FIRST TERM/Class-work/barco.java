public class Barco {
    private int numero;
    private int puntaje; // Puntaje que otorga el barco

    // Constructor
    public Barco(int numero, int puntaje) {
        this.numero = numero;
        this.puntaje = puntaje;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
