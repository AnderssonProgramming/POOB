public class Posicion {
    private int longitud;
    private int latitud;

    // Constructor
    public Posicion(int longitud, int latitud) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public int getLatitud() {
        return latitud;
    }

    public void setLatitud(int latitud) {
        this.latitud = latitud;
    }
}
