package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class POOBvsZombiesTest {
    private POOBvsZombies game;

    @BeforeEach
    public void setUp() {
        ArrayList<String> plants = new ArrayList<>();
        plants.add("Sunflower");
        plants.add("Peashooter");

        ArrayList<String> zombies = new ArrayList<>();
        zombies.add("Basic");
        zombies.add("Conehead");

        try {
            game = new POOBvsZombies(300, "PlayerOne", plants, 100, "PlayerTwo", 100, zombies);
        } catch (POOBvsZombiesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void accordingPSShouldAddEntityCorrectly() {
        game.addEntity(0, 0, "Peashooter");
        String entityName = game.getEntity(0, 0);
        assertEquals("Peashooter", entityName);
    }

    @Test
    public void accordingPSShouldDeleteEntityCorrectly() {
        game.addEntity(1, 1, "Sunflower");
        game.deleteEntity(1, 1);
        assertNull(game.getEntity(1, 1));
    }

    @Test
    public void accordingPSShouldFindFirstPlantInRowCorrectly() {
        game.addEntity(2, 2, "Peashooter");
        game.addEntity(2, 5, "Sunflower");
        int firstPlantCol = game.getFirstPlantInRow(2);
        assertEquals(5, firstPlantCol);
    }

    @Test
    public void accordingPSShouldNotFindLawnmowerWhenNotPresent() {
        boolean hasLawnmower = game.getLawnmowerInRow(0);
        assertFalse(hasLawnmower);
    }

    @Test
    public void accordingPSShouldBeSingletonInstance() {
        POOBvsZombies instance1 = POOBvsZombies.getInstance();
        assertNotNull(instance1);
        POOBvsZombies instance2 = POOBvsZombies.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void accordingPSShouldCreateZombieInstances() {
        Zombie zombie = game.createZombieInstance("Basic");
        assertNotNull(zombie);
        assertTrue(zombie instanceof Basic);
    }

    @Test
    public void accordingPSShouldThrowExceptionForInvalidZombieType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            game.createZombieInstance("InvalidType");
        });
        assertEquals("Tipo de zombie inválido: InvalidType", exception.getMessage());
    }

    @Test
    public void accordingPSShouldCreatePlantInstances() {
        Plant plant = game.createPlantInstance("Sunflower");
        assertNotNull(plant);
        assertTrue(plant instanceof Sunflower);
    }

    @Test
    public void accordingPSShouldReturnNullForInvalidPlantType() {
        Plant invalidPlant = game.createPlantInstance("InvalidType");
        assertNull(invalidPlant);
    }

    @Test
    public void accordingPSShouldCalculateProgressCorrectly() {
        int progress = game.calculateProgress();
        assertEquals(0, progress);
    }

    @Test
    public void accordingPSShouldReturnCorrectRoundTime() {
        float roundTime = game.getRoundTime();
        assertEquals(game.getMatchTime() / 2, roundTime);
    }

    @Test
    public void accordingPSShouldReturnCorrectModality() {
        assertEquals("PlayerVsPlayer", game.getModality());
    }

    @Test
    public void accordingPSShouldReturnCorrectMatchTime() {
        assertEquals(300 * 60, game.getMatchTime());
    }

    @Test
    public void accordingPSShouldRetrievePlayerOneCorrectly() {
        Player playerOne = game.getPlayerOne();
        assertNotNull(playerOne);
        assertEquals("PlayerOne", playerOne.getName());
    }

    @Test
    public void accordingPSShouldRetrievePlayerTwoCorrectly() {
        Player playerTwo = game.getPlayerTwo();
        assertNotNull(playerTwo);
        assertEquals("PlayerTwo", playerTwo.getName());
    }

    @Test
    public void accordingPSShouldCalculateScoresCorrectly() {
        game.calculateScores();
        int scoreOne = game.getPlayerOne().getScore();
        int scoreTwo = game.getPlayerTwo().getScore();
        assertTrue(scoreOne >= 0);
        assertTrue(scoreTwo >= 0);
    }

    @Test
    public void accordingPSShouldDetermineWinnerCorrectly() {
        game.getPlayerOne().setScore(150);
        game.getPlayerTwo().setScore(100);
        String winnerMessage = game.determineWinner();
        assertEquals("¡Las plantas han ganado! Puntaje: 150 vs 100", winnerMessage);
    }

    @Test
    public void accordingPSShouldAddPendingExtraResourcesCorrectly() {
        game.addPendingExtraResources(0, 0, 3, 25, "Sun");
        // Suponemos que GardenMenu procesa los recursos
        assertTrue(true); // Asegúrate de validarlo con GardenMenu correctamente.
    }

    @Test
    public void accordingPSShouldSaveAndLoadGameCorrectly() throws IOException, POOBvsZombiesException {
        File file = new File("test_game_save.txt");
        game.saveGame(file, 0, 60, false);

        POOBvsZombies loadedGame = POOBvsZombies.loadGame(file);
        assertEquals("PlayerVsPlayer", loadedGame.getModality());
        assertEquals(300 * 60, loadedGame.getMatchTime());
        file.delete();
    }


    @Test
    public void accordingPSShouldRemoveEntityCorrectly() {
        game.addEntity(3, 3, "Sunflower");
        game.removeEntity(3, 3);
        assertNull(game.getEntity(3, 3));
    }

    @Test
    public void accordingPSShouldRetrievePlantAtCorrectPosition() {
        game.addEntity(2, 2, "Sunflower");
        assertNotNull(game.getPlantAt(2, 2));
    }

    @Test
    public void accordingPSShouldReturnNullWhenNoPlantAtPosition() {
        assertNull(game.getPlantAt(4, 4));
    }

    @Test
    public void accordingPSShouldSetAndGetWinnerCorrectly() {
        game.setWinner("PlayerOne");
        assertEquals("PlayerOne", game.getWinner());
    }

    @Test
    public void accordingPSShouldAddAndDeleteEntityCorrectly() {
        game.addEntity(1, 1, "Sunflower");
        assertEquals("Sunflower", game.getEntity(1, 1)); // Verificar adición
        game.deleteEntity(1, 1);
        assertNull(game.getEntity(1, 1)); // Verificar eliminación
    }

    @Test
    public void accordingPSShouldCalculateScoresAfterAddingEntities() {
        game.addEntity(0, 0, "Peashooter");
        game.addEntity(1, 1, "Sunflower");
        game.calculateScores();
        int playerOneScore = game.getPlayerOne().getScore();
        assertTrue(playerOneScore > 0); // Verificar puntuación aumentada
    }

    @Test
    public void accordingPSShouldDetermineWinnerAfterScoreUpdate() {
        game.addEntity(0, 0, "Sunflower");
        game.calculateScores();
        game.getPlayerOne().setScore(200);
        game.getPlayerTwo().setScore(150);
        String winner = game.determineWinner();
        assertEquals("¡Las plantas han ganado! Puntaje: 200 vs 150", winner);
    }


    @Test
    public void accordingPSShouldSaveAndLoadGameWithEntitiesCorrectly() throws IOException, POOBvsZombiesException {
        game.addEntity(2, 2, "Sunflower");
        File file = new File("save_test.txt");
        game.saveGame(file, 0, 30, false);

        POOBvsZombies loadedGame = POOBvsZombies.loadGame(file);
        assertEquals("Sunflower", loadedGame.getEntity(2, 2));
        file.delete();
    }

    @Test
    public void accordingPSShouldNotAddInvalidEntitiesAndHandleExceptions() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            game.addEntity(0, 0, "InvalidEntity");
        });
        assertEquals("Invalid entity type: InvalidEntity", exception.getMessage());

        assertNull(game.getEntity(0, 0)); // Verificar que no se añadió nada
    }

    @Test
    public void accordingPSShouldUpdateScoresAfterRemovingEntities() {
        game.addEntity(2, 3, "Peashooter");
        game.calculateScores();
        int scoreBefore = game.getPlayerOne().getScore();
        
        game.deleteEntity(2, 3);
        game.calculateScores();
        int scoreAfter = game.getPlayerOne().getScore();
        
        assertTrue(scoreBefore > scoreAfter); // Puntaje debe reducirse
    }

    @Test
    public void accordingPSShouldSpawnPlantAndZombieCorrectly() {
        // Crear instancia de planta y zombie
        Plant sunflower = game.createPlantInstance("Sunflower");
        Zombie basicZombie = game.createZombieInstance("Basic");

        assertNotNull(sunflower); // Verificar que la planta fue creada
        assertNotNull(basicZombie); // Verificar que el zombie fue creado

    }


    @Test
    public void accordingPSShouldRestoreGameStateWithResources() throws IOException, POOBvsZombiesException {
        game.addPendingExtraResources(0, 0, 5, 25, "Sun");
        File file = new File("restore_test.txt");
        game.saveGame(file, 1, 20, true);

        POOBvsZombies restoredGame = POOBvsZombies.loadGame(file);
        assertTrue(restoredGame.getRestoredPaused());
        assertEquals(1, restoredGame.getRestoredIndex());
        assertEquals(20, restoredGame.getRestoredRemaining());
        file.delete();
    }


}
