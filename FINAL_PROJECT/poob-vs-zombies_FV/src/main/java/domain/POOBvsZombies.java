package domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.Timer;

import presentation.GardenMenu;

/**
 * The POOBvsZombies class represents the main game logic and state for the POOB vs Zombies game.
 * It manages game initialization, game modes, players, entities on the game board, and provides
 * methods for interacting with the game state.
 * @author: Andersson David Sánchez Méndez
 * @author: Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class POOBvsZombies {
    private String modality;
    private String winner; 
    private float roundTime;
    private float matchTime;
    private ArrayList<Player> players;
    private ArrayList<ArrayList<Object>> entities; 

    // Singleton instance
    private static POOBvsZombies instance;

    // Map to manage individual timers for each entity
    @SuppressWarnings("unused")
    private Map<Entity, Timer> entityTimers = new HashMap<>();

    // Reference to GardenMenu
    private GardenMenu gardenMenu;

    // Campos para restaurar estado tras cargar partida
    private int restoredIndex=0;
    private int restoredRemaining=0;
    private boolean restoredPaused=false;

    public int getRestoredIndex(){return restoredIndex;}
    public int getRestoredRemaining(){return restoredRemaining;}
    public boolean getRestoredPaused(){return restoredPaused;}

    // Constructors

    /**
     * Constructs a new POOBvsZombies game in Player vs Player mode.
     *
     * @param matchTimeInSeconds Duration of the match in seconds.
     * @param namePlayerOne Name of the first player.
     * @param plants List of plant types available to the first player.
     * @param sunAmount Initial amount of sun resources for the first player.
     * @param namePlayerTwo Name of the second player.
     * @param brainAmount Initial amount of brain resources for the second player.
     * @param zombies List of zombie types available to the second player.
     * @throws POOBvsZombiesException If the inputs are invalid.
     */
    public POOBvsZombies(float matchTimeInSeconds, String namePlayerOne, ArrayList<String> plants, int sunAmount, String namePlayerTwo, int brainAmount, ArrayList<String> zombies) throws POOBvsZombiesException {
        
        if(matchTimeInSeconds <= 0 || namePlayerOne == null || plants == null || sunAmount <= 0 || namePlayerTwo == null || zombies == null || brainAmount <= 0){ 
            throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
        }
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Object>>();

        this.players.add(new ZombiesStrategic(namePlayerOne, sunAmount, plants));
        this.players.add(new PlantsStrategic(namePlayerTwo, brainAmount, zombies));

        this.modality = "PlayerVsPlayer";
        this.winner = "";

        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();

        instance = this;
    }

    /**
     * Constructs a new POOBvsZombies game in Player vs Machine mode.
     *
     * @param matchTimeInSeconds Duration of the match in seconds.
     * @param hordersNumber Number of zombie hordes.
     * @param namePlayerOne Name of the player.
     * @param plants List of plant types available to the player.
     * @throws POOBvsZombiesException If the inputs are invalid.
     */
    public POOBvsZombies(float matchTimeInSeconds,int hordersNumber, String namePlayerOne, ArrayList<String> plants)throws POOBvsZombiesException {
        if(matchTimeInSeconds <= 0 || hordersNumber <= 0 || namePlayerOne == null || plants == null){ 
            throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
        }
        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Object>>();

        this.players.add(new PlantsStrategic(namePlayerOne, 2615, plants));
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds));

        this.modality = "PlayerVsMachine";
        this.winner = "";

        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();

        ((ZombiesOriginal) players.get(1)).startAutomaticHordes(this);

        instance = this;
    }

    
    
    /**
     * Constructs a new POOBvsZombies game instance Machine vs Machine mode.
     *
     * @param matchTimeInSeconds the duration of the match in seconds
     * @param hordersNumber the number of zombie hordes
     * @param suns the initial number of suns for the plants
     * @param brains the initial number of brains for the zombies
     * @throws POOBvsZombiesException if the inputs are invalid
     */
    public POOBvsZombies(float matchTimeInSeconds, int hordersNumber, int suns, int brains) throws POOBvsZombiesException{

        if(matchTimeInSeconds <= 0 || hordersNumber <= 0 || suns <= 0 || brains <= 0){ 
            throw new POOBvsZombiesException(POOBvsZombiesException.INVALID_INPUTS);
        }

        this.players = new ArrayList<Player>();
        this.entities = new ArrayList<ArrayList<Object>>();

        this.players.add(new PlantsIntelligent(suns));
        this.players.add(new ZombiesOriginal(hordersNumber, matchTimeInSeconds, MachinePlayer.ORIGINAL_ZOMBIES, brains));

        this.modality = "MachineVsMachine";
        this.winner = "";

        this.matchTime = setMatchTime(matchTimeInSeconds);
        this.roundTime = this.matchTime / 2;
        setUpEntities();


        ((PlantsIntelligent) players.get(0)).startAutomaticPlanting(this);
        ((ZombiesOriginal) players.get(1)).startAutomaticHordes(this);

        instance = this;
    }

    /**
     * Retrieves the singleton instance of POOBvsZombies.
     *
     * @return The singleton instance.
     */
    public static POOBvsZombies getInstance() {
        return instance;
    }
    
    /**
     * Creates a new Zombie instance based on the specified type.
     *
     * @param zombieType The type of zombie to create.
     * @return A new Zombie instance.
     * @throws IllegalArgumentException If the specified zombie type is invalid.
     */
    public Zombie createZombieInstance(String zombieType) {
        switch (zombieType) {
            case "Basic":
                return new Basic();
            case "Brainstein":
                return new Brainstein();
            case "BucketHead":
                return new Buckethead();
            case "Conehead":
                return new Conehead();
            case "ECIZombie":
                return new ECIZombie();
            default:
                throw new IllegalArgumentException("Tipo de zombie inválido: " + zombieType);
        }
    }

    /**
     * Creates a new Plant instance based on the specified type.
     *
     * @param plantType The type of plant to create.
     * @return A new Plant instance, or null if the type is invalid.
     */
    public Plant createPlantInstance(String plantType) {
        switch (plantType) {
            case "Sunflower":
                return new Sunflower();
            case "Peashooter":
                return new Peashooter();
            case "WallNut":
                return new WallNut();
            case "PotatoMine":
                return new PotatoMine();
            case "ECIPlant":
                return new ECIPlant();
            default:
                return null;
        }
    }

    /**
     * Spawns a zombie on the UI at the specified location.
     *
     * @param row The row index where the zombie will be placed.
     * @param col The column index where the zombie will be placed.
     * @param zombie The Zombie instance to spawn.
     */
    public void spawnZombieUI(int row, int col, Zombie zombie) {
        // Ensure gardenMenu is not null
        if (gardenMenu != null) {
            gardenMenu.spawnZombieAutomatically(row, col, zombie);
        }
    }
    
    /**
     * Spawns a plant on the UI at the specified location.
     *
     * @param row   The row index where the plant will be placed.
     * @param col   The column index where the plant will be placed.
     * @param plant The Plant instance to spawn.
     */
    public void spawnPlantUI(int row, int col, Plant plant) {
        // Ensure gardenMenu is not null
        if (gardenMenu != null) {
            gardenMenu.spawnPlantAutomatically(row, col, plant);
        }
    }

    /**
     * Calculates the current progress of the game.
     *
     * @return An integer representing the progress.
     */
    public int calculateProgress(){
        return 0;
    }


    /**
     * Converts the given time in seconds to minutes.
     *
     * @param seconds the time in seconds to be converted
     * @return the equivalent time in minutes
     */
    private float setMatchTime(float seconds) {
        return seconds * 60;
    }

    /**
     * Gets the round time for the game.
     *
     * @return The round time in seconds.
     */
    public float getRoundTime() {
        return roundTime;
    }
    
    /**
     * Gets the number of hordes in the game.
     *
     * @return The number of zombie hordes.
     */
    public int getHordersNumber() {
        if (players.size() > 1 && players.get(1) instanceof ZombiesOriginal) {
            ZombiesOriginal zombiesOriginal = (ZombiesOriginal) players.get(1);
            return zombiesOriginal.getHordersNumber();
        }
        return 0; 
    }

    /**
     * Gets the modality of the game.
     *
     * @return The game modality as a string.
     */
    public String getModality() {
        return modality;
    }

    /**
     * Gets the total match time.
     *
     * @return The match time in seconds.
     */
    public float getMatchTime() {
        return matchTime;
    }

    /**
     * Retrieves the first player (usually the plants player).
     *
     * @return The first Player instance.
     */
    public Player getPlayerOne(){
        return players.get(0);
    }

    /**
     * Retrieves the second player (usually the zombies player).
     *
     * @return The second Player instance.
     */
    public Player getPlayerTwo(){
        return players.get(1);
    }

    /**
     * Calculates the scores for both players based on current resources and entities.
     */
    public void calculateScores() {
        // Player 1 (Plants)
        Player plantsPlayer = players.get(0);
        int plantsResources = plantsPlayer.getTeam().getResourceCounterAmount(); // Current resources
        int plantsValue = calculateEntitiesValue(entities, true); // Sum value of plants on the board
        int plantsScore = (int) ((plantsResources + plantsValue) * 1.5); // Multiply by 1.5
        plantsPlayer.setScore(plantsScore);
    
        // Player 2 (Zombies)
        Player zombiesPlayer = players.get(1);
        int zombiesResources = zombiesPlayer.getTeam().getResourceCounterAmount(); // Current resources
        int zombiesValue = calculateEntitiesValue(entities, false); // Sum value of zombies on the board
        int zombiesScore = (zombiesResources + zombiesValue); // No multiplication
        zombiesPlayer.setScore(zombiesScore);
    }
    

    /**
     * Determines the winner of the game based on the players' scores.
     *
     * @return A message indicating the winner and the scores.
     */
    public String determineWinner() {
        Player plantsPlayer = players.get(0);
        Player zombiesPlayer = players.get(1);
    
        int plantsScore = plantsPlayer.getScore();
        int zombiesScore = zombiesPlayer.getScore();
    
        if (plantsScore > zombiesScore) {
            winner = plantsPlayer.getName();
            return "¡Las plantas han ganado! Puntaje: " + plantsScore + " vs " + zombiesScore;
        } else if (plantsScore < zombiesScore) {
            winner = zombiesPlayer.getName();
            return "¡Los zombies han ganado! Puntaje: " + zombiesScore + " vs " + plantsScore;
        } else {
            winner = "Empate";
            return "¡Es un empate! Ambos jugadores tienen puntaje: " + plantsScore;
        }
    }
    
    /**
     * Ends the game and displays the winner message.
     *
     * @param winnerMessage The message to display indicating the winner.
     */
    public void endGame(String winnerMessage) {
        calculateScores(); // Calculate final scores
        if (gardenMenu != null) {
            gardenMenu.showWinnerMessage(winnerMessage);
        }
        System.exit(0);
    }

    /**
     * Calculates the total value of entities (plants or zombies) on the board.
     *
     * @param entities The game board matrix of entities.
     * @param isPlant True to calculate the value of plants, false for zombies.
     * @return The total value of the specified entities.
     */
    private int calculateEntitiesValue(ArrayList<ArrayList<Object>> entities, boolean isPlant) {
        int totalValue = 0;
        for (ArrayList<Object> row : entities) {
            for (Object obj : row) {
                if (obj instanceof Plant && isPlant) {
                    totalValue += ((Plant) obj).getCost(); // Sum the cost of the plant
                } else if (obj instanceof Zombie && !isPlant) {
                    totalValue += ((Zombie) obj).getCost(); // Sum the cost of the zombie
                }
            }
        }
        return totalValue;
    }
    
    /**
     * Initializes the entities matrix representing the game board.
     */
    public void setUpEntities() {
        for (int i = 0; i < 5; i++) {
            ArrayList<Object> row = new ArrayList<Object>();
            for (int j = 0; j < 10; j++) {
                if (j == 9) {
                    row.add(new LinkedList<Zombie>()); // Cola de zombies
                } else {
                    row.add(null); // una sola entidad o null
                }
            }
            entities.add(row);
        }
    }

    /**
     * Adds an entity to the game board at the specified location.
     *
     * @param lane The row index where the entity will be placed.
     * @param yPos The column index where the entity will be placed.
     * @param entityType The type of entity to create and add.
     */
    public void addEntity(int lane, int yPos, String entityType) {
        Entity entity = createEntity(entityType);
        addEntity(lane, yPos, entity);
    }
    
    /**
     * Creates a new Entity instance based on the specified type.
     *
     * @param entityType The type of entity to create.
     * @return A new Entity instance.
     * @throws IllegalArgumentException If the entity type is invalid.
     */
    private Entity createEntity(String entityType) {
        switch (entityType) {
            case "Basic": return new Basic();
            case "Brainstein": return new Brainstein();
            case "BucketHead": return new Buckethead();
            case "Conehead": return new Conehead();
            case "ECIZombie": return new ECIZombie();
            case "Sunflower": return new Sunflower();
            case "Peashooter": return new Peashooter();
            case "WallNut": return new WallNut();
            case "PotatoMine": return new PotatoMine();
            case "ECIPlant": return new ECIPlant();
            case "LownMover": return new Lownmover();
            default:
                throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }
    }

    /**
     * Adds an existing entity to the game board at the specified location.
     *
     * @param lane The row index where the entity will be placed.
     * @param yPos The column index where the entity will be placed.
     * @param entity The Entity instance to add.
     * @throws IndexOutOfBoundsException If the specified location is invalid.
     */
    public void addEntity(int lane, int yPos, Entity entity) {
        if (lane < 0 || lane >= 5 || yPos < 0 || yPos >= 10) {
            throw new IndexOutOfBoundsException("Invalid lane or yPos");
        }
    
        if (yPos < 9) {
            entities.get(lane).set(yPos, entity);
            if (entity instanceof Plant) {
                Player plantsPlayer = players.get(0);
                plantsPlayer.setScore(plantsPlayer.getScore() + ((Plant) entity).getCost());
            } else if (entity instanceof Zombie) {
                Player zombiesPlayer = players.get(1);
                zombiesPlayer.setScore(zombiesPlayer.getScore() + ((Zombie) entity).getCost());
            }
        } else {
            // Última columna: agregamos cola de zombies
            if (entity instanceof Zombie) {
                Queue<Zombie> queue = getZombieQueue(lane, 9);
                queue.offer((Zombie) entity);
               // Sumar costo del zombie al puntaje de OZombies
                Player zombiesPlayer = players.get(1); // OZombies es siempre el segundo jugador
                zombiesPlayer.addToScore(((Zombie) entity).getCost());
                if (gardenMenu!=null) gardenMenu.updateScoreLabels(); // Actualizar los puntajes visuales
            } else {
                throw new IllegalArgumentException("Only zombies can be added to the last column");
            }
        }
    }
    

    /**
     * Deletes an entity from the game board at the specified location.
     *
     * @param xPos The row index of the entity to delete.
     * @param yPos The column index of the entity to delete.
     * @throws IndexOutOfBoundsException If the specified location is invalid.
     */
    public void deleteEntity(int xPos, int yPos) {
        if (xPos<0||xPos>=5||yPos<0||yPos>=10) throw new IndexOutOfBoundsException("Invalid xPos or yPos");
        if (yPos<9) {
            entities.get(xPos).set(yPos, null);
            
        } else {
            // Si borramos aqui, significaría limpiar cola
            Queue<Zombie> q = getZombieQueue(xPos,9);
            q.clear();
        }
    }

    /**
     * Retrieves the queue of zombies at the specified location.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The queue of zombies at the specified location.
     */
    @SuppressWarnings("unchecked")
    private Queue<Zombie> getZombieQueue(int row, int col) {
        return (Queue<Zombie>) entities.get(row).get(col);
    }

    /**
     * Gets the name of the entity at the specified location.
     *
     * @param xPos The row index.
     * @param yPos The column index.
     * @return The name of the entity, or null if none exists.
     */
    public String getEntity(int xPos, int yPos) {
        if (xPos<0||xPos>=5||yPos<0||yPos>=10) return null;
        if (yPos<9) {
            Object obj = entities.get(xPos).get(yPos);
            Entity entity = (Entity)obj;
            return entityName(entity);
        } else {
            // última columna
            Queue<Zombie> q = getZombieQueue(xPos,yPos);
            if (q.isEmpty()) return null;
            // Por si quisieramos el primer zombi del queue:
            Zombie z = q.peek();
            return entityName(z);
        }
    }

    /**
     * Returns the name of the given entity.
     *
     * @param entity The entity whose name is to be retrieved.
     * @return The name of the entity, or null if the entity is null.
     */
    private String entityName(Entity entity) {
        if (entity==null) return null;
        if (entity instanceof Basic) return "Basic";
        if (entity instanceof Brainstein) return "Brainstein";
        if (entity instanceof Buckethead) return "BucketHead";
        if (entity instanceof Conehead) return "Conehead";
        if (entity instanceof ECIZombie) return "ECIZombie";
        if (entity instanceof Sunflower) return "Sunflower";
        if (entity instanceof Peashooter) return "Peashooter";
        if (entity instanceof WallNut) return "WallNut";
        if (entity instanceof PotatoMine) return "PotatoMine";
        if (entity instanceof ECIPlant) return "ECIPlant";
        if (entity instanceof Lownmover) return "LownMover";
        return null;
    }

    /**
     * Finds the column index of the first plant in the specified row, starting from the right.
     *
     * @param row The row index to search.
     * @return The column index of the first plant, or -1 if none is found.
     */
    public int getFirstPlantInRow(int row) {
        for (int col = 8; col >= 0; col--) {
            Object obj = entities.get(row).get(col);
            if (obj instanceof Plant) {
                return col;
            }
        }
        return -1;
    }

    /**
     * Retrieves the Plant instance at the specified location.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The Plant instance, or null if none exists.
     */
    public Plant getPlantAt(int row, int col) {
        if (row<0||row>=5||col<0||col>=9) return null; // En la ultima col no hay plantas
        Object obj = entities.get(row).get(col);
        if (obj instanceof Plant) {
            return (Plant)obj;
        }
        return null;
    }
    
    /**
     * Removes the entity at the specified location.
     *
     * @param row The row index.
     * @param col The column index.
     */
    public void removeEntity(int row, int col) {
        if (row<0||row>=5||col<0||col>=10) return;
        entities.get(row).set(col,null);
    }

    /**
     * Retrieves the matrix of entities representing the game board.
     *
     * @return The entities matrix.
     */
    public ArrayList<ArrayList<Object>> getEntitiesMatrix() {
        return entities;
    }

    /**
     * Adds extra resources for the ECIPlant when the player runs out of suns.
     *
     * @param row   The row index where the resources will be spawned.
     * @param col   The column index where the resources will be spawned.
     * @param count The number of resources to add.
     * @param value The value of each resource.
     * @param type  The type of resource ("Sun" or other).
     */
    public void addPendingExtraResources(int row, int col, int count, int value, String type) {
        for(int i =0; i < count; i++) {
            Resource resource = new Resource(type, value);
            spawnSpecificResource(row, col, resource);
        }
    }

    /**
     * Spawns a specific resource at the specified location in the UI.
     *
     * @param row      The row index where the resource will be spawned.
     * @param col      The column index where the resource will be spawned.
     * @param resource The Resource instance to spawn.
     */
    public void spawnSpecificResource(int row, int col, Resource resource) {
        if (gardenMenu != null) {
            gardenMenu.spawnSpecificResource(row, col, resource);
        }
    }
    
    /**
     * Sets the GardenMenu reference for UI interactions.
     *
     * @param gardenMenu The GardenMenu instance.
     */
    public void setGardenMenu(GardenMenu gardenMenu) {
        this.gardenMenu = gardenMenu;
    }

    /**
     * Gets the GardenMenu reference.
     *
     * @return The GardenMenu instance.
     */
    public GardenMenu getGardenMenu() {
        return gardenMenu;
    }

    /**
     * Checks for a lawnmower in the specified row and removes it if present.
     *
     * @param row The row index to check.
     * @return True if a lawnmower was found and removed, false otherwise.
     * @throws IndexOutOfBoundsException If the specified row index is invalid.
     */
    public boolean getLawnmowerInRow(int row) {
        if (row < 0 || row >= entities.size()) {
            throw new IndexOutOfBoundsException("Invalid row index: " + row);
        }
        Object obj = entities.get(row).get(0);
        if (obj instanceof Lownmover) {
            entities.get(row).set(0, null);
            return true;
        }
        return false;
    }

    /**
     * Removes all zombies in the specified row.
     *
     * @param row The row index where zombies will be removed.
     * @throws IndexOutOfBoundsException If the specified row index is invalid.
     */
    public void removeZombiesInRow(int row) {
        if (row < 0 || row >= 5) {
            throw new IndexOutOfBoundsException("Invalid row index: " + row);
        }
        Queue<Zombie> queue = getZombieQueue(row, 9);
        while (!queue.isEmpty()) {
            queue.poll();
        }
    }

    /**
     * Sets the winner of the game.
     *
     * @param winner the name of the winner to be set
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Retrieves the winner of the game.
     *
     * @return A string representing the winner.
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Saves the current game state to a specified file.
     *
     * @param file The file to save the game state to.
     * @param currentIndex The current index of the timer task.
     * @param currentRemaining The remaining time of the current timer task.
     * @param isPaused The paused state of the game.
     * @throws IOException If an I/O error occurs.
     */
    public void saveGame(File file, int currentIndex, int currentRemaining, boolean isPaused) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("modality=" + getModality());
            pw.println("matchTime=" + getMatchTime());
            pw.println("roundTime=" + getRoundTime());

            Player p1 = getPlayerOne();
            pw.println("playerOneName=" + p1.getName());
            pw.println("playerOneScore=" + p1.getScore());
            pw.println("playerOneSuns=" + p1.getTeam().getResourceCounterAmount());

            Player p2 = getPlayerTwo();
            pw.println("playerTwoName=" + p2.getName());
            pw.println("playerTwoScore=" + p2.getScore());
            pw.println("playerTwoBrains=" + p2.getTeam().getResourceCounterAmount());

            pw.println("currentTimerTaskIndex=" + currentIndex);
            pw.println("currentTimerTaskRemainingTime=" + currentRemaining);
            pw.println("isPaused=" + isPaused);

            // Guardar la matriz de entidades
            for (int i = 0; i < 5; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 10; j++) {
                    Object obj = entities.get(i).get(j);
                    if (j > 0) sb.append(" ");
                    if (j < 9) {
                        if (obj == null) sb.append("-");
                        else sb.append(entityName((Entity)obj));
                    } else {
                        @SuppressWarnings("unchecked")
                        Queue<Zombie> q = (Queue<Zombie>) obj;
                        if (q.isEmpty()) sb.append("-");
                        else {
                            Zombie z = q.peek();
                            sb.append(entityName(z));
                        }
                    }
                }
                pw.println(sb.toString());
            }

            // Guardar las cartas seleccionadas por ambos jugadores
            // Plantas del jugador 1
            for (String plant: getPlayerOne().getTeam().getCharacters()) {
                pw.println("selectedPlant=" + plant);
            }

            // Zombies del jugador 2
            for (String zombie: getPlayerTwo().getTeam().getCharacters()) {
                pw.println("selectedZombie=" + zombie);
            }
        }
    }

    /**
     * Loads a POOBvsZombies game from a specified file.
     *
     * @param file The file from which to load the game.
     * @return A POOBvsZombies instance representing the loaded game.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws POOBvsZombiesException If an error specific to POOBvsZombies occurs.
     */
    public static POOBvsZombies loadGame(File file) throws IOException, POOBvsZombiesException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String modality="";
        float matchTime=0f, roundTime=0f;
        String playerOneName="", playerTwoName="";
        int playerOneScore=0, playerOneSuns=0;
        int playerTwoScore=0, playerTwoBrains=0;
        int currentTaskIndex=0, currentTaskRemaining=0;
        boolean isPaused=false;

        String line;
        ArrayList<String> headerLines = new ArrayList<>();
        while((line=br.readLine())!=null) {
            if (line.startsWith("Basic")||line.startsWith("Conehead")||line.startsWith("BucketHead")||line.startsWith("Brainstein")||line.startsWith("ECIZombie")||
                line.startsWith("Sunflower")||line.startsWith("Peashooter")||line.startsWith("WallNut")||line.startsWith("PotatoMine")||line.startsWith("ECIPlant")||line.startsWith("LownMover")||
                line.startsWith("-")) {
                // Esto significa que ya comenzamos a leer la matriz
                // Añadimos el último que no es header a la lista también
                headerLines.add(line);
                break;
            }
            headerLines.add(line);
            if (line.startsWith("isPaused=")) break;
        }

        for (String l: headerLines) {
            if (l.startsWith("modality=")) modality = l.split("=")[1];
            else if (l.startsWith("matchTime=")) matchTime = Float.parseFloat(l.split("=")[1]);
            else if (l.startsWith("roundTime=")) roundTime = Float.parseFloat(l.split("=")[1]);
            else if (l.startsWith("playerOneName=")) playerOneName = l.split("=")[1];
            else if (l.startsWith("playerOneScore=")) playerOneScore = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("playerOneSuns=")) playerOneSuns = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("playerTwoName=")) playerTwoName = l.split("=")[1];
            else if (l.startsWith("playerTwoScore=")) playerTwoScore = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("playerTwoBrains=")) playerTwoBrains = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("currentTimerTaskIndex=")) currentTaskIndex = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("currentTimerTaskRemainingTime=")) currentTaskRemaining = Integer.parseInt(l.split("=")[1]);
            else if (l.startsWith("isPaused=")) isPaused = Boolean.parseBoolean(l.split("=")[1]);
        }

        // Ahora leer la matriz
        ArrayList<String> matrixLines = new ArrayList<>();
        if (!headerLines.get(headerLines.size()-1).contains("isPaused=")) {
            // El ultimo leido no fue isPaused, sino que ya es parte de la matriz
            // La ultima linea leida fue la primera linea de la matriz
            matrixLines.add(headerLines.get(headerLines.size()-1));
        }

        // Leer las 4 lineas restantes de la matriz
        while(matrixLines.size()<5 && (line=br.readLine())!=null) {
            matrixLines.add(line);
        }

        ArrayList<ArrayList<Object>> newMatrix = new ArrayList<>();
        for (int i=0; i<5; i++) {
            String matLine = matrixLines.get(i);
            String[] parts = matLine.split(" ");
            if (parts.length<10) {
                br.close();
                throw new IOException("Línea de matriz inválida");
            }
            ArrayList<Object> row = new ArrayList<>();
            for (int j=0;j<10;j++) {
                String entName = parts[j];
                if (entName.equals("-")) {
                    if (j<9) row.add(null);
                    else {
                        row.add(new LinkedList<Zombie>());
                    }
                } else {
                    if (j<9) {
                        Entity e = createEntityStatic(entName);
                        row.add(e);
                    } else {
                        Queue<Zombie> q = new LinkedList<>();
                        Zombie z = (Zombie)createEntityStatic(entName);
                        q.offer(z);
                        row.add(q);
                    }
                }
            }
            newMatrix.add(row);
        }

        // Ahora leer las lineas restantes para las cartas seleccionadas
        ArrayList<String> selectedPlants = new ArrayList<>();
        ArrayList<String> selectedZombies = new ArrayList<>();
        while((line=br.readLine())!=null) {
            if (line.startsWith("selectedPlant=")) {
                selectedPlants.add(line.split("=")[1]);
            } else if (line.startsWith("selectedZombie=")) {
                selectedZombies.add(line.split("=")[1]);
            }
        }

        br.close();

        // Crear instancia POOBvsZombies según modalidad
        POOBvsZombies loadedGame;
        ArrayList<String> emptyList = new ArrayList<>();
        if (modality.equals("PlayerVsPlayer")) {
            loadedGame = new POOBvsZombies(1, "tempP1", emptyList, 50, "tempP2", 50, emptyList);
        } else if (modality.equals("PlayerVsMachine")) {
            loadedGame = new POOBvsZombies(1,1,"tempP1",emptyList);
        } else if (modality.equals("MachineVsMachine")) {
            loadedGame = new POOBvsZombies(1,1,50,50);
        } else {
            loadedGame = new POOBvsZombies(1,"tempP1",emptyList,50,"tempP2",50,emptyList);
        }

        loadedGame.modality = modality;
        loadedGame.matchTime = matchTime;
        loadedGame.roundTime = roundTime;

        loadedGame.getPlayerOne().setName(playerOneName);
        loadedGame.getPlayerOne().setScore(playerOneScore);
        loadedGame.getPlayerOne().getTeam().setResourceCounter(playerOneSuns);

        loadedGame.getPlayerTwo().setName(playerTwoName);
        loadedGame.getPlayerTwo().setScore(playerTwoScore);
        loadedGame.getPlayerTwo().getTeam().setResourceCounter(playerTwoBrains);

        loadedGame.entities = newMatrix;

        // Restaurar cartas seleccionadas
        loadedGame.getPlayerOne().getTeam().setCharacters(selectedPlants);
        loadedGame.getPlayerTwo().getTeam().setCharacters(selectedZombies);

        // Guardar info adicional para restaurar en GardenMenu
        loadedGame.restoredIndex = currentTaskIndex;
        loadedGame.restoredRemaining = currentTaskRemaining;
        loadedGame.restoredPaused = isPaused;

        return loadedGame;
    }

private static Entity createEntityStatic(String name) {
    switch (name) {
        case "Basic": return new Basic();
        case "Brainstein": return new Brainstein();
        case "BucketHead": return new Buckethead();
        case "Conehead": return new Conehead();
        case "ECIZombie": return new ECIZombie();
        case "Sunflower": return new Sunflower();
        case "Peashooter": return new Peashooter();
        case "WallNut": return new WallNut();
        case "PotatoMine": return new PotatoMine();
        case "ECIPlant": return new ECIPlant();
        case "LownMover": return new Lownmover();
        default: return null;
    }
}


}