
/**
 * Write a description of class Ship here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ship implements ShipInterface
{
    
    
    
    /**
     * Constructor takes an integer (the size of the ship) as a parameter
     */
    public Ship(int size)
    {
        
    }

    public int getSize(){
        return -1;
    }
    
    public boolean isSunk(){
        return false;
    }
    
    /**
     * Update the status of the ship by firing at the offset specified. 
     * After the method is called then the status at the offset will either be HIT (if at least one square remains INTACT) 
     * or SUNK (if all the other squares were also HIT before the method was called). If a square was already HIT before
     * this method was called, it remains HIT
     * 
     * @param offset The offset from the top/left of the ship. 
     * 
     * @throws InvalidPositionException When the parameter is less than zero or 
     * greater than/equal to the size of the ship 
     */
    public void shoot(int offset) throws InvalidPositionException{
        
    }
    
    public ShipStatus getStatus(int offset) throws InvalidPositionException{
        return null;
    }
}
