import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;

/**
 * Representa una tabla relacional con atributos y tuplas.
 * Los atributos se definen como columnas y las tuplas como filas de la tabla.
 * Esta clase permite realizar operaciones sobre la relación, como insertar, eliminar, proyectar, seleccionar, 
 * unir, e intersectar tuplas.
 * 
 * @author Pedraza-Sánchez
 * @version 2024
 */
public class Relation {
    private String[] attributes; // Atributos de la relación (columnas)
    private Set<String[]> tuples; // Conjunto de tuplas (filas) almacenadas en la relación

    /**
     * Crea una nueva relación con los atributos especificados.
     * 
     * @param attributes Los atributos que definen las columnas de la relación.
     *                   No deben tener duplicados.
     */
    public Relation(String[] attributes) {
        // Verifica que los atributos no contengan duplicados
        Set<String> attributeSet = new HashSet<>(Arrays.asList(attributes));
        if (attributeSet.size() != attributes.length) {
            this.attributes = new String[0]; // Si hay duplicados, crea una relación vacía
        } else {
            this.attributes = attributes;
        }
        this.tuples = new HashSet<>();
    }

    /**
     * Inserta una tupla en la relación.
     * La tupla no se insertará si ya existe una tupla igual ignorando mayúsculas.
     * 
     * @param tuple La tupla a insertar.
     * @return true si la tupla se insertó correctamente, false si ya existía.
     */
    public boolean insert(String[] tuple) {
        for (String[] existingTuple : tuples) {
            if (Arrays.equals(existingTuple, tuple)) {
                return false; // Si la tupla ya existe, no se inserta
            }
        }
        return this.tuples.add(tuple); // Añade la nueva tupla
    }

    /**
     * Elimina una tupla de la relación.
     * 
     * @param tuple La tupla a eliminar.
     * @return true si la tupla fue eliminada, false si no se encontró.
     */
    public boolean delete(String[] tuple) {
        if (tuple.length != this.attributes.length) {
            return false; // La tupla es inválida si no coincide con el número de atributos
        }
        return this.tuples.remove(tuple); // Elimina la tupla
    }

    /**
     * Retorna el número de columnas (atributos) de la relación.
     * 
     * @return El número de columnas de la relación.
     */
    public int columns() {
        return this.attributes.length;
    }

    /**
     * Retorna el número de tuplas en la relación.
     * 
     * @return El número de tuplas en la relación.
     */
    public int tuples() {
        return this.tuples.size();
    }

    /**
     * Retorna los atributos (columnas) de la relación.
     * 
     * @return Un arreglo de los nombres de los atributos de la relación.
     */
    public String[] attributes() {
        return this.attributes;
    }

    /**
     * Verifica si una tupla está presente en la relación.
     * 
     * @param tuple La tupla a verificar.
     * @return true si la tupla está presente, false en caso contrario.
     */
    public boolean in(String[] tuple) {
        if (tuple.length != this.attributes.length) {
            return false; // La tupla es inválida si no coincide con el número de atributos
        }
        return this.tuples.contains(tuple);
    }

    /**
     * Compara si dos relaciones son iguales en términos de atributos y tuplas.
     * 
     * @param other Otra relación a comparar.
     * @return true si ambas relaciones son iguales, false en caso contrario.
     */
    public boolean equals(Relation other) {
        if (other == null) return false;
        return Arrays.equals(this.attributes, other.attributes) &&
               this.tuples.equals(other.tuples);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return equals((Relation) other);
    }

    /**
     * Retorna una representación en formato de texto de la relación.
     * 
     * @return Una cadena que representa la relación con atributos y tuplas.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(", ", attributes)).append("\n");
        for (String[] tuple : tuples) {
            sb.append(String.join(", ", tuple)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Proyecta la relación sobre los atributos especificados.
     * 
     * @param newAttributes Los atributos sobre los que se proyectará la relación.
     * @return Una nueva relación con los atributos proyectados, o null si la proyección es inválida.
     */
    public Relation project(String[] newAttributes) {
        Set<String> currentAttributes = new HashSet<>(Arrays.asList(this.attributes));
        if (!currentAttributes.containsAll(Arrays.asList(newAttributes))) {
            return null; // Proyección inválida si los nuevos atributos no son un subconjunto
        }
        
        Relation projected = new Relation(newAttributes);
        for (String[] tuple : this.tuples) {
            String[] projectedTuple = new String[newAttributes.length];
            for (int i = 0; i < newAttributes.length; i++) {
                int index = Arrays.asList(this.attributes).indexOf(newAttributes[i]);
                projectedTuple[i] = tuple[index];
            }
            projected.insert(projectedTuple);
        }
        return projected;
    }

    /**
     * Selecciona las tuplas basadas en una condición derivada de una tupla.
     * 
     * @param conditionTuple La tupla que representa la condición de selección.
     * @return Una nueva relación con las tuplas seleccionadas.
     */
    public Relation select(String[] conditionTuple) {
        if (conditionTuple.length != this.attributes.length) {
            return null; // Condición inválida
        }

        Relation selected = new Relation(this.attributes);
        for (String[] tuple : this.tuples) {
            boolean matches = true;
            for (int i = 0; i < conditionTuple.length; i++) {
                if (!conditionTuple[i].equals("*") && !conditionTuple[i].equals(tuple[i])) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                selected.insert(tuple);
            }
        }
        return selected;
    }

    /**
     * Realiza la multiplicación (producto cartesiano) de esta relación con otra.
     * 
     * @param other La relación con la que se multiplicará esta relación.
     * @return Una nueva relación que contiene el producto cartesiano.
     */
    public Relation multiply(Relation other) {
        Set<String> allAttributes = new HashSet<>(Arrays.asList(this.attributes));
        allAttributes.addAll(Arrays.asList(other.attributes));
        String[] newAttributes = allAttributes.toArray(new String[0]);

        Relation result = new Relation(newAttributes);

        for (String[] tuple1 : this.tuples) {
            for (String[] tuple2 : other.tuples) {
                boolean match = true;
                for (int i = 0; i < this.attributes.length; i++) {
                    int index = Arrays.asList(newAttributes).indexOf(this.attributes[i]);
                    if (!tuple1[i].equals(tuple2[index])) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    String[] newTuple = new String[newAttributes.length];
                    for (int i = 0; i < this.attributes.length; i++) {
                        newTuple[Arrays.asList(newAttributes).indexOf(this.attributes[i])] = tuple1[i];
                    }
                    for (int i = 0; i < other.attributes.length; i++) {
                        newTuple[Arrays.asList(newAttributes).indexOf(other.attributes[i])] = tuple2[i];
                    }
                    result.insert(newTuple);
                }
            }
        }
        return result;
    }

    /**
     * Realiza la unión de esta relación con otra.
     * 
     * @param other La relación con la que se unirá esta relación.
     * @return Una nueva relación que contiene todas las tuplas de ambas relaciones.
     */
    public Relation union(Relation other) {
        if (!Arrays.equals(this.attributes, other.attributes)) {
            return null; // No se pueden unir relaciones con diferentes atributos
        }

        Relation result = new Relation(this.attributes);
        for (String[] tuple : this.tuples) {
            result.insert(tuple);
        }
        for (String[] tuple : other.tuples) {
            result.insert(tuple); // El método insert evitará duplicados
        }

        return result;
    }

    /**
     * Realiza la intersección de esta relación con otra.
     * 
     * @param other La relación con la que se intersectará esta relación.
     * @return Una nueva relación que contiene solo las tuplas comunes.
     */
    public Relation intersection(Relation other) {
        if (!Arrays.equals(this.attributes, other.attributes)) {
            return null; // No se pueden intersectar relaciones con diferentes atributos
        }

        Relation result = new Relation(this.attributes);
        for (String[] tuple : this.tuples) {
            if (other.in(tuple)) {
                result.insert(tuple);
            }
        }

        return result;
    }
}
