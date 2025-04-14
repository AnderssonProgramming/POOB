import java.util.TreeMap;

/** 
 * Calculadora Relacional.
 * Permite la manipulación de relaciones (tablas) con operaciones como insertar, eliminar, proyectar, 
 * seleccionar, multiplicar, unir e intersectar relaciones.
 * 
 * @author Pedraza-Sánchez
 * @version 2024
 */
public class RelationalCalculator {
    
    private TreeMap<String, Relation> variables; // Almacena las relaciones con sus nombres
    private boolean lastOperationSuccessful; // Indica si la última operación fue exitosa

    /**
     * Crea una nueva instancia de la calculadora relacional.
     */
    public RelationalCalculator() {
        this.variables = new TreeMap<>();
        this.lastOperationSuccessful = false;
    }

    /**
     * Asigna una relación vacía con los atributos especificados a una variable.
     * 
     * @param name El nombre de la variable.
     * @param attributes Los atributos de la nueva relación.
     */
    public void assign(String name, String[] attributes) {
        if (attributes == null || attributes.length == 0) {
            // Si los atributos son nulos o vacíos, la operación falla
            lastOperationSuccessful = false;
            return;
        }
        variables.put(name, new Relation(attributes)); // Asigna la nueva relación
        lastOperationSuccessful = true;
    }

    /**
     * Realiza una operación unaria sobre una relación (insertar o eliminar una tupla).
     * 
     * @param a El nombre de la variable sobre la que se opera.
     * @param unary El operador ('i' para insertar, 'd' para eliminar).
     * @param tuple La tupla a insertar o eliminar.
     */
    public void update(String a, char unary, String[] tuple) {
        Relation relation = variables.get(a);
        if (relation == null || tuple == null || tuple.length != relation.columns()) {
            lastOperationSuccessful = false;
            return;
        }
        switch (unary) {
            case 'i':
                lastOperationSuccessful = relation.insert(tuple);
                break;
            case 'd':
                lastOperationSuccessful = relation.delete(tuple);
                break;
            default:
                lastOperationSuccessful = false;
        }
    }

    /**
     * Realiza una operación binaria sobre dos relaciones.
     * 
     * @param a El nombre de la variable destino.
     * @param b El nombre de la primera relación.
     * @param operator El operador ('p' para proyectar, 's' para seleccionar, 'm' para multiplicar).
     * @param c El nombre de la segunda relación.
     */
    public void assign(String a, String b, char operator, String c) {
        Relation relB = variables.get(b);
        Relation relC = variables.get(c);
        
        if (relB == null || relC == null) {
            lastOperationSuccessful = false;
            return;
        }
        
        Relation result = null;
        
        switch (operator) {
            case 'p':
                result = relB.project(relC.attributes());
                break;
            case 's':
                result = relB.select(relC.attributes());
                break;
            case 'm':
                result = relB.multiply(relC);
                break;
            default:
                lastOperationSuccessful = false;
                return;
        }
        
        if (result != null) {
            variables.put(a, result); // Asigna el resultado
            lastOperationSuccessful = true;
        } else {
            lastOperationSuccessful = false;
        }
    }

    /**
     * Retorna una lista con los nombres de las variables asignadas.
     * 
     * @return Un arreglo de nombres de variables.
     */
    public String[] variables() {
        return variables.keySet().toArray(new String[0]);
    }

    /**
     * Devuelve una representación en texto de la relación.
     * 
     * @param variable El nombre de la variable.
     * @return Una cadena con la representación de la relación.
     */
    public String toString(String variable) {
        Relation relation = variables.get(variable);
        if (relation == null) {
            return "Relation not found";
        }
        return relation.toString();
    }

    /**
     * Indica si la última operación fue exitosa.
     * 
     * @return true si la última operación fue exitosa, false en caso contrario.
     */
    public boolean ok() {
        return lastOperationSuccessful;
    }

    /**
     * Asigna la unión de dos relaciones a una variable.
     * 
     * @param a El nombre de la variable destino.
     * @param b El nombre de la primera relación.
     * @param c El nombre de la segunda relación.
     */
    public void assignUnion(String a, String b, String c) {
        Relation relB = variables.get(b);
        Relation relC = variables.get(c);

        if (relB == null || relC == null) {
            lastOperationSuccessful = false;
            return;
        }

        Relation result = relB.union(relC);
        if (result != null) {
            variables.put(a, result);
            lastOperationSuccessful = true;
        } else {
            lastOperationSuccessful = false;
        }
    }

    /**
     * Asigna la intersección de dos relaciones a una variable.
     * 
     * @param a El nombre de la variable destino.
     * @param b El nombre de la primera relación.
     * @param c El nombre de la segunda relación.
     */
    public void assignIntersection(String a, String b, String c) {
        Relation relB = variables.get(b);
        Relation relC = variables.get(c);

        if (relB == null || relC == null) {
            lastOperationSuccessful = false;
            return;
        }

        Relation result = relB.intersection(relC);
        if (result != null) {
            variables.put(a, result);
            lastOperationSuccessful = true;
        } else {
            lastOperationSuccessful = false;
        }
    }

    /**
     * Retorna la relación asignada a una variable.
     * 
     * @param varName El nombre de la variable.
     * @return La relación correspondiente, o null si no existe.
     */
    public Relation get(String varName) {
        return variables.get(varName);
    }
}
