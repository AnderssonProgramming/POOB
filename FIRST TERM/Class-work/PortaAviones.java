public class PortaAviones {
    private int numero;
    private int capacidad;
    private int puntaje; // Puntaje que otorga el portaaviones

    // Constructor
    public PortaAviones(int numero, int capacidad, int puntaje) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.puntaje = puntaje;
    }

    // Métodos para acceder y modificar el puntaje
    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}
