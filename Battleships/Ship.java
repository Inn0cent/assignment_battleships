import java.util.Arrays;
import java.io.IOException;
/**
 * The Ship class represents individual ships in the game of battleships.
 */
public class Ship implements ShipInterface
{

    private ShipStatus[] squares;
    private boolean sunk;

    /**
     * Constructor takes an integer (the size of the ship) as a parameter
     */
    public Ship(int size)
    {
        squares = new ShipStatus[size];
        Arrays.fill(squares, ShipStatus.INTACT);
        sunk = false;
    }

    public int getSize(){
        return squares.length;
    }

    public boolean isSunk(){
        return sunk;
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
        if(offset < 0 || offset >= squares.length) {
            throw new InvalidPositionException("Shot out of array length");
        }
        squares[offset] = ShipStatus.HIT;
        sunk = true;
        for(int i = 1; i < squares.length; i++){
            if(squares[i] != squares[0]){
                sunk = false;
                break;
            }
        }
        if(sunk){
            for(int i = 0; i < squares.length; i++){
                squares[i] = ShipStatus.SUNK;
            }
        }
    }

    /**
     * Find the status of a square of the ship
     * 
     * @param offset The offset from the top/left of the ship.
     * 
     * @return the status of that square: INTACT, HIT or SUNK
     * 
     * @throws InvalidPositionException When the parameter is less than zero or 
     * greater than/equal to the size of the ship 
     */
    public ShipStatus getStatus(int offset) throws InvalidPositionException{
        if(offset < 0 || offset >= squares.length) {
            throw new InvalidPositionException("Shot out of array length");
        }
        return squares[offset];
    }
    
    public String saveString(){
        String save = "";
        for(ShipStatus status : squares){
            save += status.name() + ",";
        }
        return save; 
    }
    
    public static Ship loadShip(String input) throws IOException{
        Ship newShip = null;
        String[] statuses = input.split(",");
        newShip = new Ship(statuses.length);
        try{
            for(int i = 0; i < statuses.length; i++){
                newShip.squares[i] = ShipStatus.valueOf(statuses[i]);
            }
        } catch (IllegalArgumentException ex){
            throw new IOException("Loading error - incorrect file format");
        }
        return newShip;
    }
}
