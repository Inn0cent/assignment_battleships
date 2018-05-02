import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
/**
 * Write a description of class AI here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ComputerPlayer implements PlayerInterface
{

    private HashMap<Position, ShotStatus> prevShots;
    private Position prevPos;
    private ArrayList<Position> oppShots;
    private Random rand;
    private String name;
    private boolean onShip;
    private boolean isVertical;
    private int direction;
    private Position startPos;
    private int tempDir;
    private int searchCount;
    private boolean changed; //When this is true the shooting direction has changed so there are two ships next to each other

    public ComputerPlayer(String name)
    {
        prevShots = new HashMap<Position, ShotStatus>();
        oppShots = new ArrayList<Position>();
        rand = new Random();
        onShip = false;
        direction = 0;
        tempDir = 0;
        searchCount = 0;
        this.name = name;
    }

    /**
     * @param ship The ship to be placed
     * 
     * @param board (A clone of) the current board state to which the ship will be added
     * 
     * @return The placement (position and orientation) of the ship specified by the player
     * 
     * @throws PauseException At any stage the user can choose to enter "pause" (case insensitive) to make the game return to 
     * the main menu
     * 
     */
    public Placement choosePlacement(ShipInterface ship, BoardInterface board) throws PauseException{
        boolean valid = false;
        Placement newPlace = null;
        Position newPos = null;
        while(!valid){
            valid = true;
            int x = rand.nextInt(10) + 1;
            int y = rand.nextInt(10) + 1;
            boolean isVertical = rand.nextBoolean();
            try{
                newPos = new Position(x,y);
                board.placeShip(ship, newPos, isVertical);
                newPlace = new Placement(newPos, isVertical);
            } catch (InvalidPositionException ex) {
                valid = false;
            } catch (ShipOverlapException ex){
                valid = false;
            }   
        }
        return newPlace;
    }

    /**
     * If the previous shot it a hit then it will shoot in all the directions around the previous shot util it hits again
     * Once the direction is found it will shoot in that direcition until it misses, in which case it shoots in the other direction, or the ship is sunk, where it contiues to shoot randomly.  
     */ 
    public Position chooseShot(){
        int x;
        int y;
        try{
            if(onShip){
                if (prevShots.get(prevPos) == ShotStatus.HIT && direction != 0){
                    prevPos = shootInDirection(prevShots, prevPos);
                    return prevPos;
                }else if(prevShots.get(prevPos) == ShotStatus.SUNK){
                    prevPos = randomShot(prevShots);
                    return prevPos;
                } else if (prevShots.get(prevPos) == ShotStatus.HIT && direction == 0){
                    direction = tempDir;                    
                    prevPos = shootInDirection(prevShots, prevPos);
                    return prevPos;
                } else if (prevShots.get(prevPos) == ShotStatus.MISS && direction != 0){ //if the end of the target ship is reached and it is not sunk yet
                    prevPos = changeDirection(prevShots);
                    return prevPos;
                }else if (direction == 0){
                    prevPos = searchShot(prevShots, startPos);
                    return prevPos;
                } else { //Should never be called but if it is then its there
                    prevPos = randomShot(prevShots);
                    return prevPos;
                }
            } else {
                if(prevShots.get(prevPos) == ShotStatus.HIT){
                    onShip = true;
                    startPos = prevPos;
                    prevPos = searchShot(prevShots, prevPos);
                    return prevPos;
                } else {
                    prevPos = randomShot(prevShots);
                    return prevPos;
                }
            }
        } catch (InvalidPositionException ex){
            System.out.println("InvalidPosException");
        }
        System.out.println("Error in ai");
        return null;
    }
    
    /**
     * After the game calls the chooseShot method, it then calls this method with the result of the shot.
     * The player may choose to keep track of the results of previous shots in its state
     * 
     * @param position The position of the shot
     * 
     * @param status The result of the shot
     */
    public void shotResult(Position position, ShotStatus status){
        prevShots.put(position, status);
    }
    
    /**
     * After the game has received the opponent's shot this method is called. It does not 
     * affect the progress of the game, but may be of interest to the player (particularly a human player).
     * @param position The position that the opponent chose for their shot.
     */
    public void opponentShot(Position position){
        oppShots.add(position);
    }
    
    /**
     * @return A string representation of the player i.e. the display name provided as 
     * a parameter to the constructor.
     */
    public String toString(){
        return name;
    }

    /**
     * Will shoot in the same direction that it is allocated unless it hits a wall in which case it will change direction.
     */
    public Position shootInDirection(HashMap<Position, ShotStatus> prevShots, Position prevPos) throws InvalidPositionException{
        if(isVertical){
            if(prevPos.getY() + direction <= 10 && prevPos.getY() + direction >= 1){
                return new Position(prevPos.getX(), prevPos.getY() + direction);
            } else {
                return changeDirection(prevShots);
            }
        } else {
            if(prevPos.getX() + direction <= 10 && prevPos.getX() + direction >= 1){
                return new Position(prevPos.getX()  + direction, prevPos.getY());
            } else {
                return changeDirection(prevShots);
            }
        }
    }

    /**
     * It only shoots at every other position on the board randomly i.e. it only shoots the white squares of a chess board
     * This is because no ship can be 1 square wide so the algorithm only needs to shoot at every other square
     */
    public Position randomShot(HashMap<Position, ShotStatus> prevShots) throws InvalidPositionException{ 
        onShip = false;
        direction = 0;
        searchCount = 0;
        changed = false; //Resets all the values that change when a ship has been hit

        int x = rand.nextInt(10) + 1;
        int y = rand.nextInt(10) + 1;
        if(x%2 == 0){
            while(y%2 == 0){
                y = rand.nextInt(10) + 1;
            }
        } else {
            while(y%2 == 1){
                y = rand.nextInt(10) + 1;
            }
        }
        Position newPos = new Position(x, rand.nextInt(10) + 1);
        while(mapContainsPos(prevShots, newPos)){
            newPos = new Position(rand.nextInt(10) + 1, rand.nextInt(10) + 1);
        }
        return newPos;
    }

    /**
     * The method will shoot around the intial hit point until it hits in another position.
     */
    public Position searchShot(HashMap<Position, ShotStatus> prevShots, Position prevPos) throws InvalidPositionException{       
        if(prevPos.getX() < 10 && searchCount == 0 && !mapContainsPos(prevShots, new Position(prevPos.getX() + 1, prevPos.getY()))){
            isVertical = false;
            tempDir = 1;
            searchCount = 1;
            return new Position(prevPos.getX() + 1, prevPos.getY());
        } else if (searchCount == 0){
            searchCount = 1;
        }
        if(prevPos.getX() > 1 && searchCount == 1 && !mapContainsPos(prevShots, new Position(prevPos.getX() - 1, prevPos.getY()))){
            isVertical = false;
            tempDir = -1;
            searchCount = 2;
            return new Position(prevPos.getX() - 1, prevPos.getY());
        } else if (searchCount == 1){
            searchCount = 2;
        }
        if(prevPos.getY() < 10 && searchCount == 2 && !mapContainsPos(prevShots, new Position(prevPos.getX(), prevPos.getY() + 1))){
            isVertical = true;
            tempDir = 1;
            searchCount = 3;
            return new Position(prevPos.getX(), prevPos.getY() + 1);
        } else if (searchCount == 2){
            searchCount = 3;
        }
        if(prevPos.getY() > 1 && !mapContainsPos(prevShots, new Position(prevPos.getX(), prevPos.getY() - 1))){
            isVertical = true;
            tempDir = -1;
            return new Position(prevPos.getX(), prevPos.getY() - 1);
        }
        return randomShot(prevShots);
    }

    public Position changeDirection(HashMap<Position, ShotStatus> prevShots) throws InvalidPositionException {
        direction *= -1;
        if(changed == false){
            changed = true;
            return shootInDirection(prevShots, startPos);
        } else {
            direction = 0;
            return searchShot(prevShots, startPos);
        }
    }

    public boolean mapContainsPos(HashMap<Position, ShotStatus> prevShots, Position pos){
        for(Position key : prevShots.keySet()){
            if(key.equals(pos)){
                return true;
            }
        }
        return false;
    }
}
