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
                    onShip = false;
                    direction = 0;
                    return randomShot(prevShots);
                } else if (prevShots.get(prevPos) == ShotStatus.HIT && direction == 0){
                    direction = tempDir;
                    searchCount = 0;
                    return shootInDirection(prevShots, prevPos);
                } else if (prevShots.get(prevPos) == ShotStatus.MISS && direction != 0){ //if the end of the target ship is reached and it is not sunk yet
                    direction *= -1;
                    return shootInDirection(prevShots, startPos);
                }else if (direction == 0){
                    return searchShot(prevShots, startPos);
                } else { //Should never be called but if it is then its there
                    onShip = false;
                    direction = 0;
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

        }
        System.out.println("Error in ai");
        return null;
    }
    
    public Position shootInDirection(HashMap<Position, ShotStatus> prevShots, Position prevPos) throws InvalidPositionException{
        if(isVertical){
            return new Position(prevPos.getX(), prevPos.getY() + direction);
        } else {
            return new Position(prevPos.getX()  + direction, prevPos.getY());
        }
    }
    
    public Position randomShot(HashMap<Position, ShotStatus> prevShots) throws InvalidPositionException{
        Position newPos = new Position(rand.nextInt(10) + 1, rand.nextInt(10) + 1);
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
        onShip = false;
        direction = 0;
        return randomShot(prevShots);
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
