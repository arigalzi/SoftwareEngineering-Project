package it.polimi.ingsw.PSP47.Model;

import it.polimi.ingsw.PSP47.Enumerations.Color;
import it.polimi.ingsw.PSP47.Enumerations.Direction;
import it.polimi.ingsw.PSP47.Enumerations.Gender;
import it.polimi.ingsw.PSP47.Enumerations.Level;
import it.polimi.ingsw.PSP47.Model.Exceptions.*;
import it.polimi.ingsw.PSP47.Model.Gods.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurnTest {
    Turn turn;
    Player player;
    God god;
    Slot[][] slots = new Slot[Board.ROWS_NUMBER][Board.COLUMNS_NUMBER];
    private Game game;
    
    @Before
    public void setUp() throws Exception {
        game = new Game(3);
        player = new Player("1", Color.BLUE);
        for (int i = 0; i<Board.ROWS_NUMBER; i++) {
            for (int j = 0; j<Board.COLUMNS_NUMBER; j++) {
                slots[i][j] = game.getBoard().getSlot(i,j);
            }
        }
    }
    
    @After
    public void tearDown() throws Exception {
        game.getBoard().clearBoard();
        player = null;
        turn = null;
        god = null;
        for (int i = 0; i<Board.ROWS_NUMBER; i++) {
            for (int j = 0; j<Board.COLUMNS_NUMBER; j++) {
                slots[i][j] = null;
            }
        }
    }

    // lo fa il controller non il model
   /* @Test
    public void deleteWorkersIfParalyzed_BothWorkersParalyzed_WorkersDeleted ()
            throws Exception {
        setUp_DeleteWorkersIfParalyzed_WorkersParalyzed(player, player.getWorker(Gender.MALE), player.getWorker(Gender.FEMALE));
        
        turn = new Turn(player, game.getBoard());    // the constructor calls implicitly deleteWorkersIfParalyzed
        
        assertTrue(player.isLoosing());
        assertNull(player.getWorker(Gender.MALE));
        assertNull(player.getWorker(Gender.FEMALE));
    }*/

    @Test
    public void deleteWorkersIfParalyzed_MaleWorkerParalyzed_WorkersNotDeleted ()
            throws Exception {
        setUp_DeleteWorkersIfParalyzed_WorkersParalyzed(player, player.getWorker(Gender.MALE), player.getWorker(Gender.FEMALE));
        // I am going to free the female worker (this is Prometheus, he can build)
        slots[3][3].setLevel(Level.LEVEL2);
        turn = new Turn(player, game.getBoard());
        
        assertFalse(player.isLoosing());
        assertEquals(player.getWorker(Gender.FEMALE), player.getWorker(Gender.FEMALE));
        assertEquals(player.getWorker(Gender.MALE), player.getWorker(Gender.MALE));
    }
    @Test
    public void deleteWorkersIfParalyzed_FemaleWorkerParalyzed_WorkersNotDeleted ()
            throws Exception {
        setUp_DeleteWorkersIfParalyzed_WorkersParalyzed(player, player.getWorker(Gender.MALE), player.getWorker(Gender.FEMALE));
        // I am going to free the female worker (this is Prometheus, he can build)
        slots[0][0].setLevel(Level.GROUND);
        turn = new Turn(player, game.getBoard());
        
        assertFalse(player.isLoosing());
        assertEquals(player.getWorker(Gender.FEMALE), player.getWorker(Gender.FEMALE));
        assertEquals(player.getWorker(Gender.MALE), player.getWorker(Gender.MALE));
    }

    // lo fa il controller non il model
    /*@Test
    public void deleteWorkersIfParalyzed_FemaleWorkerParalyzed_MaleWorkerNull_WorkersDeleted ()
            throws Exception {
        setUp_DeleteWorkersIfParalyzed_WorkersParalyzed(player, player.getWorker(Gender.MALE), player.getWorker(Gender.FEMALE));
        player.deleteWorker(player.getWorker(Gender.MALE));
        
        turn = new Turn(player,game.getBoard());    // the constructor calls implicitly deleteWorkersIfParalyzed
    
        assertTrue(player.isLoosing());
        assertNull(player.getWorker(Gender.MALE));
        assertNull(player.getWorker(Gender.FEMALE));
    }*/


    // lo fa il controller non il model
    /*@Test
    public void deleteWorkersIfParalyzed_MaleWorkerParalyzed_FemaleWorkerNull_WorkersDeleted () throws Exception {
        setUp_DeleteWorkersIfParalyzed_WorkersParalyzed(player, player.getWorker(Gender.MALE), player.getWorker(Gender.FEMALE));
        player.deleteWorker(player.getWorker(Gender.FEMALE));
        
        turn = new Turn(player, game.getBoard());    // the constructor calls implicitly deleteWorkersIfParalyzed
        
        assertTrue(player.isLoosing());
        assertNull(player.getWorker(Gender.MALE));
        assertNull(player.getWorker(Gender.FEMALE));
    }*/

    private void setUp_DeleteWorkersIfParalyzed_WorkersParalyzed(Player player, Worker maleWorker, Worker femaleWorker)
            throws Exception {
        god = new Prometheus(player, "Prometheus");   // Prometheus has normal conditions checking

        // I am going to paralyze the workers.
        maleWorker.setSlot(slots[1][1]);
        femaleWorker.setSlot(slots[2][2]);
        slots[0][0].setLevel(Level.DOME);
        slots[1][0].setLevel(Level.DOME);
        slots[2][0].setLevel(Level.DOME);
        slots[2][1].setLevel(Level.DOME);
        slots[3][1].setLevel(Level.DOME);
        slots[3][2].setLevel(Level.DOME);
        slots[3][3].setLevel(Level.DOME);
        slots[2][3].setLevel(Level.DOME);
        slots[1][3].setLevel(Level.DOME);
        slots[1][2].setLevel(Level.DOME);
        slots[0][2].setLevel(Level.DOME);
        slots[0][1].setLevel(Level.DOME);
    }

    //non serve pi?? perch?? ?? stato tolto il canusebothworkers

    /*@Test (expected = InvalidMoveException.class)
    public void setWorkerGender_SetWorkerTwoTimes_CannotUseBothWorkers_ThrowsWrongBuildOrMoveException() throws Exception {
        player = new Player("test", Color.BLUE);
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(1, 1));
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(0, 1));

        god = new Apollo(player, "Apollo test");
        turn = new Turn(player, game.getBoard());
        
        assertNull(turn.getWorkerGender());
        turn.setWorkerGender(Gender.MALE);
        assertEquals(turn.getWorkerGender(), Gender.MALE);
    
        turn.setWorkerGender(Gender.FEMALE);
    
    }*/
    @Test
    public void setWorkerGender_SetWorkerTwoTimes_CanUseBothWorkers_NormallyChangesWorkerGender() throws Exception {
        player = new Player("test", Color.BLUE);
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(1, 1));
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(0, 1));

        god = new Apollo(player, "Prometheus test");
        turn = new Turn(player, game.getBoard());
        
        assertNull(turn.getWorkerGender());
        turn.setWorkerGender(Gender.MALE);
        assertEquals(turn.getWorkerGender(), Gender.MALE);
        turn.setWorkerGender(Gender.FEMALE);
        assertEquals(turn.getWorkerGender(), Gender.FEMALE);
    }
    
    @Test
    public void setWantsToBuildDome_GodIsAtlas_WantsToBuildDomeSetTrue () throws Exception {
        player = new Player("test", Color.BLUE);
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(1, 1));
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(0, 1));

        god = new Atlas(player, "atlas test");
        turn = new Turn(player, game.getBoard());
        
        turn.setWantsToBuildDome(true);
        assertTrue(turn.wantsToBuildDome());
    }
    @Test (expected = InvalidBuildException.class)
    public void setWantsToBuildDome_GodIsNotAtlas_ThrowsWrongBuildOrMoveException () throws Exception {
        player = new Player("test", Color.BLUE);
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(1, 1));
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(0, 1));

        god = new Prometheus(player, "atlas test");
        turn = new Turn(player, game.getBoard());
        
        
        turn.setWantsToBuildDome(true);
    }

    // lo fa il controller non il model

    /*@Test (expected = InvalidMoveException.class)
    public void executeMove_ThirdLevelReached_PlayerIsWinning_ThenTryToMoveAnotherTime_ThrowsNoAvailableMovementsException() throws Exception{
        player = new Player("test", Color.BLUE);
        god = new Prometheus(player, "atlas test");
        turn = new Turn(player);
        turn.setWorkerGender(Gender.MALE);
        
        assertEquals(0, turn.getNumberOfMovements());
        
        slots[1][0].setLevel(Level.LEVEL3);
        slots[1][1].setLevel(Level.LEVEL2);
        player.getWorker(Gender.MALE).setSlot(slots[1][1]);
        turn.executeMove(Direction.LEFT);
        
        assertEquals(slots[1][0].getWorker(), player.getWorker(Gender.MALE));
        assertEquals(1, turn.getNumberOfMovements());
        assertTrue(player.isWinning());
        
        turn.executeMove(Direction.RIGHT);
        
    }*/
    @Test
    public void executeMove_ThirdLevelNotReached_PlayerIsNotWinning() throws Exception{
        player = new Player("test", Color.BLUE);
        player.getWorker(Gender.MALE).setSlot(game.getBoard().getSlot(1, 1));
        player.getWorker(Gender.FEMALE).setSlot(game.getBoard().getSlot(0, 1));

        god = new Prometheus(player, "atlas test");
        turn = new Turn(player, game.getBoard());
        
        assertEquals(0, turn.getNumberOfMovements());
        
        player.getWorker(Gender.MALE).setSlot(slots[0][1]);
        turn.setWorkerGender(Gender.MALE);
        turn.executeMove(Direction.LEFT);
        
        assertEquals(slots[0][0].getWorker(), player.getWorker(Gender.MALE));
        assertEquals(1, turn.getNumberOfMovements());
        assertFalse(player.isWinning());
    }


    // lo fa il controller non il model
    /*@Test (expected = InvalidBuildException.class)
    public void executeBuild_BuildTwoTimes_ThrowsNoAvailableBuildingsException () throws Exception{
        player = new Player("test", Color.BLUE);
        god = new Athena(player, "atlas test");
        turn = new Turn(player);
        
        player.getWorker(Gender.MALE).setSlot(slots[0][1]);
        turn.setWorkerGender(Gender.MALE);
        turn.executeMove(Direction.DOWN);
        assertEquals(slots[1][1].getWorker(), player.getWorker(Gender.MALE));
        assertEquals(turn.getNumberOfMovements(), 1);
        
        turn.executeBuild(Direction.LEFT);
        assertEquals(slots[1][0].getLevel(), Level.LEVEL1);
        assertEquals(turn.getNumberOfBuildings(), 1);
        
        turn.executeBuild(Direction.DOWN);
    }*/


}