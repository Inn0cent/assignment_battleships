
/**
 * Write a description of class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Board implements BoardInterface
{
    
    
    
    public Board()
    {
        
    }

    /**
     * Add a ship to the board with the top/left position specified
     * 
     * @param ship The ship to add to the board
     * 
     * @param position The top/left of the ship position
     * 
     * @param isVertical True if ship is to be placed vertically, otherwise the ship should
     * be placed horizontally
     * 
     * @throws InvalidPositionException if the not all of the ship squares fit onto the board
     * 
     * @throws ShipOverlapException if the any of the squares of the ship to be added would overlap
     * with an already placed ship
     * 
     */
    public void placeShip(ShipInterface ship, Position position, boolean isVertical) throws InvalidPositionException, ShipOverlapException{
        
    }

    /**
     * Update the board state by shooting at the given position
     * 
     * @throws InvalidPositionException if the shot position is not on the board
     */
    public void shoot(Position position) throws InvalidPositionException{
        
    }
    
    /**
     * Find the status at the given position
     * 
     * @param position the position to find out about
     * 
     * @return The status at the given board position
     * 
     * @throws InvalidPositionException if the position is not on the board
     */
    public ShipStatus getStatus(Position position) throws InvalidPositionException{
        return null;
    }
    
    /**
     * Find if all the ships on the board have been sunk
     * 
     * @return True If and only if all the ships have been sunk
     */
    public boolean allSunk(){
        return false;
    }
    
    /**
     * 
     * A string representation of the board, suitable for printing to the screen
     * 
     */
    public String toString(){
        return null;
    }
    
    /**
     * Make a deep clone of the current object. Only clones of boards are to be passed to a
     * player by a game, so that the player does not affect the game state if shots are tried out
     *
     * @return the cloned object
     */
    public BoardInterface clone(){
        return null;
    }
}
