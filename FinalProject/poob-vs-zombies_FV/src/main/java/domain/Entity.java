
/**
 * The Entity class represents a generic entity with a position and a name.
 * It serves as a base class for other entities in the domain.
 */
package domain;

public abstract class Entity {
    /**
     * The row position of the entity.
     */
    private int row;

    /**
     * The column position of the entity.
     */
    private int col;

    /**
     * The name of the entity.
     */
    private String name;

    /**
     * Sets the position and name of the entity.
     *
     * @param row  the row position to set
     * @param col  the column position to set
     * @param name the name to set
     */
    public void setPosition(int row, int col, String name) {
        this.row = row;
        this.col = col;
        this.name = name;
    }

    /**
     * Gets the row position of the entity.
     *
     * @return the row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column position of the entity.
     *
     * @return the column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the name of the entity.
     *
     * @return the name of the entity
     */
    public String getName() {
        return name;
    }

    // Resto de la clase...
}