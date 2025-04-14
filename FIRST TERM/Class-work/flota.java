import java.util.ArrayList;

public class Flota {
    private final String idFlota; // Identificador único que no puede ser modificado
    private String nombre;
    private ArrayList<PortaAviones> portAviones;
    private ArrayList<Avion> aviones;
    private ArrayList<Barco> barcos;
    private ArrayList<Marino> marinos;
    
    private int puntajeTotal; // Para rastrear el puntaje de la flota

    // Constructor
    public Flota(String idFlota, String nombre) {
        this.idFlota = idFlota;
        this.nombre = nombre;
        this.portAviones = new ArrayList<>();
        this.aviones = new ArrayList<>();
        this.barcos = new ArrayList<>();
        this.marinos = new ArrayList<>();
        this.puntajeTotal = 0; // Inicialmente, el puntaje es 0
    }

    // Métodos adicionales para gestionar la flota, puntajes y elementos de la guerra
    public void agregarPortaAvion(PortaAviones portaAvion) {
        portAviones.add(portaAvion);
    }

    public void agregarAvion(Avion avion) {
        aviones.add(avion);
    }

    public void agregarBarco(Barco barco) {
        barcos.add(barco);
    }

    public void agregarMarino(Marino marino) {
        marinos.add(marino);
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int puntaje) {
        this.puntajeTotal = puntaje;
    }

    public String getIdFlota() {
        return idFlota;
    }

    public String getNombre() {
        return nombre;
    }
}
