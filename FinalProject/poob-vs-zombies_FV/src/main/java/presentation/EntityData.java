package presentation;

import java.awt.Image;

/**
 * The EntityData class represents an entity with a type, name, and image.
 * It is used to store and retrieve information about different entities.
 */
public class EntityData {

    /**
     * The type of the entity.
     */
    private String type;

    /**
     * The name of the entity.
     */
    private String name;

    /**
     * The image associated with the entity.
     */
    private Image image;

    /**
     * Constructs a new EntityData object with the specified type, name, and image.
     *
     * @param type  the type of the entity
     * @param name  the name of the entity
     * @param image the image associated with the entity
     */
    public EntityData(String type, String name, Image image) {
        this.type = type;
        this.name = name;
        this.image = image;
    }

    /**
     * Returns the type of the entity.
     *
     * @return the type of the entity
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the name of the entity.
     *
     * @return the name of the entity
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the image associated with the entity.
     *
     * @return the image associated with the entity
     */
    public Image getImage() {
        return image;
    }
}

