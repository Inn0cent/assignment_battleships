import java.util.Random;
import java.util.HashMap;
/**
 * Write a description of class AI here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class AI
{

    private Random rand;
    private boolean onShip;
    private boolean isVertical;
    private int direction;
    private Position startPos;
    private int tempDir;
    private int searchCount;
    private boolean changed; //When this is true the shooting direction has changed so there are two ships next to each other

    public AI()
    {
        rand = new Random();
        onShip = false;
        direction = 0;
        tempDir = 0;
        searchCount = 0;
    }

    public Position shoot(HashMap<Position, ShotStatus> prevShots, Position prevPos){
        int x;
        int y;
        try{
            if(onShip){
                if (prevShots.get(prevPos) == ShotStatus.HIT && direction != 0){
                    return shootInDirection(prevShots, prevPos);
                }else if(prevShots.get(prevPos) == ShotStatus.SUNK){
                    return randomShot(prevShots);
                } else if (prevShots.get(prevPos) == ShotStatus.HIT && direction == 0){
                    direction = tempDir;                    
                    return shootInDirection(prevShots, prevPos);
                } else if (prevShots.get(prevPos) == ShotStatus.MISS && direction != 0){ //if the end of the target ship is reached and it is not sunk yet
                    return changeDirection(prevShots);
                }else if (direction == 0){
                    return searchShot(prevShots, startPos);
                } else { //Should never be called but if it is then its there
                    return randomShot(prevShots);
                }
            } else {
                if(prevShots.get(prevPos) == ShotStatus.HIT){
                    onShip = true;
                    startPos = prevPos;
                    return searchShot(prevShots, prevPos);
                } else {
                    return randomShot(prevShots);
                }
            }
        } catch (InvalidPositionException ex){
            System.out.println("InvalidPosException");
        }
        System.out.println("Error in ai");
        return null;
    }


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

    public boolean mapContainsPos(HashMap<Position, ShotStatus> prevShots, Position pos){ //map.contains() does not work in this situation
        for(Position key : prevShots.keySet()){
            if(key.equals(pos)){
                return true;
            }
        }
        return false;
    }
}
