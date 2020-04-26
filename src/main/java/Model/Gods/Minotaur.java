package Model.Gods;

import Model.Board;
import Enumerations.Direction;
import Model.Exceptions.*;
import Model.Player;
import Model.Slot;
import Model.Worker;

/**
 * {@link Player}'s {@link Worker} may move into an opponent worker's {@link Slot} (using normal movements rules), if
 * the next slot in the same direction is unoccupied. Their worker is forced into that slot (regardless of it's level).
 */
public class Minotaur extends God {
    
    public Minotaur(Player player, String name) {
        super(player, name);
        MIN_MOVEMENTS = 1;
        MIN_BUILDINGS = 1;
        MAX_MOVEMENTS = 1;
        MAX_BUILDINGS = 1;
        canAlwaysBuildDome = false;
        canUseBothWorkers = false;
    }

    /**
     * This method allows a movement not only if the chosen slot is free but also if in
     * the chosen slot there is an enemy worker and behind him/her the slot is free
     * @param direction where the worker wants to move to.
     * @param worker the {@link Player}'s {@link Worker} to be moved.
     * @return true if the winning condition has been verified, false otherwise
     * @throws IndexOutOfBoundsException if the worker try to move in a direction that is out out the board
     * @throws InvalidDirectionException if there are some troubles of I/O.
     * @throws InvalidMoveException if the move is not permitted.
     */
    @Override
    public boolean move(Direction direction, Worker worker)
            throws IndexOutOfBoundsException, InvalidDirectionException, InvalidMoveException {

        int previousLevel = worker.getSlot().getLevel().ordinal();
        try {
            return worker.move(direction);
        } catch (SlotOccupiedException e) {
            Slot opponentSLot = Board.getNearbySlot(direction, worker.getSlot());
            // the slot in the same direction of the worker. If there is not a slot, the move is not available.
            Slot slotNearOpponentSlot;
            try {
                slotNearOpponentSlot = Board.getNearbySlot(direction, opponentSLot);
            } catch (IndexOutOfBoundsException er){
                // this exception advises the caller that the slot is occupied and the opponent worker cannot move.
                throw new InvalidMoveException("Slot occupied");
            }
            // the worker set in the destination slot
            Worker opponentWorker = opponentSLot.getWorker();
        
            // if the slot next to the opponent worker is free and the destination slot is actually occupied by an opponent worker
            if (opponentWorker!=null && opponentWorker.getColor()!=worker.getColor() && !slotNearOpponentSlot.getIsOccupied()) {
                // manually move player's worker in the destination slot
                opponentWorker.updatePosition(slotNearOpponentSlot);
                return worker.updatePosition(opponentSLot);
            }
            // if there is a dome or a player's worker, the slot is occupied for Apollo too
            else
                throw new InvalidMoveException("Slot occupied");
        }
    }

    /**
     * This method calls the standard build of a worker:
     * Minotaur doesn't modify the building rules.
     * @param direction specifies the slot where to build
     * @param worker one of the player's workers
     * @throws IndexOutOfBoundsException if the worker try to build in a direction that is out out the board
     * @throws InvalidBuildException if the build is not permitted.
     */
    @Override
    public void build(Direction direction, Worker worker)
            throws IndexOutOfBoundsException,InvalidBuildException  {
        
        if (player.getTurn().getNumberOfMovements() == 0) throw new InvalidBuildException("Order of movements not correct");

        try {
            worker.build(direction);
        } catch (SlotOccupiedException e) {
            throw new InvalidBuildException("Slot occupied");
        }
    }


    /**
     * It does nothing.
     */
    @Override
    public void resetParameters() {
        // nothing is necessary
    }

    /**
     * this methods does what checkIfCanMoveInNormalCondition does together with another verification,
     * it checks the availability of a slot by checking the slot behind the one where the move wants to go
     * if it is occupied by an enemy worker
     * @param worker {@link Player}'s {@link Worker} selected to be checked.
     * @return true if the worker can move, false otherwise
     */
    @Override
    protected boolean checkIfCanMove(Worker worker) {
        for (Direction direction : Direction.values()) {
            try {
                // If the direction is out of the board, jump to the catch
                worker.checkDirection(direction);
                Slot destinationSlot = Board.getBoard().getNearbySlot(direction, worker.getSlot());
                Slot slotNearOpponentSlot = Board.getBoard().getNearbySlot(direction, destinationSlot);
                // else, check if the worker can move to the destinationSlot
                if (!destinationSlot.getIsOccupied()){
                    // if the player can move up and the destinationSlot hasn't got too many levels, the player can move.
                    if (!player.cannotMoveUp() && destinationSlot.getLevel().ordinal() <= worker.getSlot().getLevel().ordinal()+1)
                        return true;
                        // if the player cannot move up but the destinationSlot is equal or less high than the current slot, the player can move.
                    else if (player.cannotMoveUp() && destinationSlot.getLevel().ordinal() <= worker.getSlot().getLevel().ordinal())
                        return true;
                }
                else if (slotNearOpponentSlot!=null && !slotNearOpponentSlot.getIsOccupied() && destinationSlot.getWorker()!=null && destinationSlot.getWorker().getColor()!=worker.getColor())
                    return true;
            }
            catch (IndexOutOfBoundsException e){
                // just let the for continue
            } catch (InvalidDirectionException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * This method directly calls the God's method checkIfCanBuildInNormalConditions,
     * as in this case there is nothing else to control.
     * @param worker {@link Player}'s {@link Worker} selected to be checked.
     * @return true if the worker can build, false otherwise.
     */
    @Override
    protected boolean checkIfCanBuild(Worker worker) {
        return checkIfCanBuildInNormalConditions(worker);
    }

    /**
     * This method checks if the worker is paralyzed or not.
     * @param worker the worker chosen to be checked.
     * @return true if the worker can go on, false otherwise.
     */
    @Override
    public boolean checkIfCanGoOn(Worker worker) {
        int numberOfMovements = player.getTurn().getNumberOfMovements();
        int numberOfBuildings = player.getTurn().getNumberOfBuildings();
        
        if (numberOfMovements==0 && numberOfBuildings==0)
            return checkIfCanMove(worker);
        if (numberOfMovements==1 && numberOfBuildings==0)
            return checkIfCanBuild(worker);
        
        return false;
    }

    /**
     * This method checks if the player has completed a turn or if he still have to do some actions.
     * @return true if he can end his turn, false otherwise.
     */
    @Override
    public boolean validateEndTurn() {
        int numberOfMovements = player.getTurn().getNumberOfMovements();
        int numberOfBuildings = player.getTurn().getNumberOfBuildings();
        
        return numberOfMovements==1 && numberOfBuildings==1 || player.isWinning();
    }
}
