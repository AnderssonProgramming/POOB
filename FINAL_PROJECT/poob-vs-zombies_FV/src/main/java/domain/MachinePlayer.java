package domain;

import java.util.ArrayList;

/**
 * The MachinePlayer class represents an abstract player in the game who is controlled by the machine.
 * This class extends the Player class and provides predefined lists of plants and zombies that the machine can use.
 */
public abstract class MachinePlayer extends Player {

    /**
     * A list of plant types that the machine can use.
     */
    public static final ArrayList<String> MACHINE_PLANTS = new ArrayList<>();

    /**
     * A list of zombie types that the machine can use.
     */
    public static final ArrayList<String> MACHINE_ZOMBIES = new ArrayList<>();

    /**
     * A list of original zombie types available in the game.
     */
    public static final ArrayList<String> ORIGINAL_ZOMBIES = new ArrayList<>();

    /**
     * Static initializer block to populate the lists of plants and zombies.
     */
    static {
        ORIGINAL_ZOMBIES.add("Basic");
        ORIGINAL_ZOMBIES.add("Brainstein");
        ORIGINAL_ZOMBIES.add("BucketHead");
        ORIGINAL_ZOMBIES.add("Conehead");
        ORIGINAL_ZOMBIES.add("ECIZombie");
        
        MACHINE_PLANTS.add("Sunflower");
        MACHINE_PLANTS.add("Peashooter");
        MACHINE_PLANTS.add("Wallnut");
        
        MACHINE_ZOMBIES.add("Basic");
        MACHINE_ZOMBIES.add("Conehead");
        MACHINE_ZOMBIES.add("Buckhead");
    }
    
    /**
     * Constructs a new MachinePlayer with the specified name.
     *
     * @param name the name of the machine player
     */
    public MachinePlayer(String name) {
        super(name);
    }
}
