public class Tablero {
    private ArrayList<Flota> flotas;
    private static final int MIN_COORD = -100;
    private static final int MAX_COORD = 100;

    // Constructor
    public Tablero() {
        this.flotas = new ArrayList<>();
    }

    // Método para verificar que una posición esté dentro del rango
    public boolean posicionValida(int longitud, int latitud) {
        return longitud >= MIN_COORD && longitud <= MAX_COORD && latitud >= MIN_COORD && latitud <= MAX_COORD;
    }

    // Método para agregar una flota si todas sus posiciones son válidas
    public void agregarFlota(Flota flota) {
        // Verificar si todas las naves de la flota están dentro del rango válido de coordenadas
        for (PortaAviones p : flota.getPortaAviones()) {
            if (!posicionValida(p.getPosicion().getLongitud(), p.getPosicion().getLatitud())) {
                System.out.println("Posición fuera de rango");
                return;
            }
        }
        flotas.add(flota);
    }

    // Mostrar las flotas
    public void mostrarFlotas() {
        for (Flota flota : flotas) {
            System.out.println("Flota: " + flota.getNombre() + " (ID: " + flota.getIdFlota() + ")");
        }
    }
}
