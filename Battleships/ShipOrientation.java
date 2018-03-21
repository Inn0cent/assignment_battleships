
/**
 * Write a description of class ShipOrientation here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ShipOrientation
{
    
    ShipInterface ship;
    boolean isVertical;

    /**
     * Constructor for objects of class ShipOrientation
     */
    public ShipOrientation(ShipInterface ship, boolean isVertical)
    {
        this.ship = ship;
        this.isVertical = isVertical;
    }
    
    public ShipInterface getShip(){
        return ship;
    }
    
    /**
     * returns true if the orientation is vertical
     */
    public boolean getOrientation(){
        return isVertical;
    }
}
