public class Estudiante {
    private String nombre;
    private String apellidos;
    private int edad;
    private int id;
    private int promedio;
    private String carrera;
    private int semestre;

    // Constructor
    public Estudiante(String nombre, String apellidos, int edad, int id, int promedio, String carrera, int semestre) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.id = id;
        this.promedio = promedio;
        this.carrera = carrera;
        this.semestre = semestre;
    }

    // Getter para nombre
    public String getNombre() {
        return nombre;
    }

    // Setter para id
    public boolean setId(int id) {
        boolean res = false;
        if (id >= 0) {
            this.id = id;
            res = true;
        }
        return res;
    }

    // Getter para id
    public int getId() {
        return id;
    }

    // Método hablar
    public void hablar() {
        System.out.println("Hola, me llamo " + nombre + ", tengo " + edad + " años, estudio " + carrera + " y tengo un promedio de " + promedio + ".");
    }
}

public class Main {
    public static void main(String[] args) {
        Estudiante estudiante = new Estudiante("Juan", "Pérez", 20, 12345, 85, "Ingeniería de Sistemas", 4);
        estudiante.hablar();
    }
}
