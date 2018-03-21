import java.util.HashMap;
/**
 * Write a description of class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Board implements BoardInterface
{

    private java.util.HashMap<Position, ShipOrientation> ships;

    public Board()
    {
        ships = new java.util.HashMap<Position, ShipOrientation>();
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
        Position lookPosition = new Position(position.getX(), position.getY()); //so position is not overwritten        
        
        if((isVertical && position.getY() + ship.getSize() > 10) || (!isVertical && position.getX() + ship.getSize() > 10)){ //do not have to check if less than 0 as Position already does that
            throw new InvalidPositionException("Not all the squares fit onto the board");
        } 
        
        for(Position pos : ships.keySet()){
            lookPosition.setX(position.getX());
            lookPosition.setY(position.getY());
            for(int i = 0; i < ship.getSize(); i++){
                if(hitX(pos, lookPosition) || hitY(pos, lookPosition)){
                    throw new ShipOverlapException("Ships have overlapped at (" + lookPosition + ")");
                }
                if(isVertical){                    
                    lookPosition.setY(lookPosition.getY() + 1);
                } else {
                    lookPosition.setX(lookPosition.getX() + 1);
                }
            }
        }
        ships.put(position, new ShipOrientation(ship, isVertical));
    }

    /**
     * Update the board state by shooting at the given position
     * 
     * @throws InvalidPositionException if the shot position is not on the board
     */
    public void shoot(Position position) throws InvalidPositionException{ // Any position that is passed must be on the board
        boolean hits = false;
        for(Position pos : ships.keySet()){
            if(hitY(pos, position)){
                try{ // this ensures that the shot hits the ship
                    ships.get(pos).getShip().shoot(position.getY() - pos.getY());
                } catch (InvalidPositionException ex){
                    System.out.println("miss");
                }
                hits = true;
                break;
            } else if(hitX(pos, position)){
                try{ // this ensures that the shot hits the ship
                    ships.get(pos).getShip().shoot(position.getX() - pos.getX());
                } catch (InvalidPositionException ex){
                    System.out.println("miss");
                }
                hits = true;
                break;
            } 
        }
        if(!hits){
            System.out.println("miss");
        }
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
        for(Position pos : ships.keySet()){
            if(hitX(pos, position) || hitY(pos, position)){
                return ships.get(pos).getShip().getStatus(0);
            }
        }
        return ShipStatus.NONE;
    }

    /**
     * Find if all the ships on the board have been sunk
     * 
     * @return True If and only if all the ships have been sunk
     */
    public boolean allSunk(){
        boolean sunk = true;
        for(ShipOrientation value : ships.values()){
            try{
                if(value.getShip().getStatus(0) != ShipStatus.SUNK){
                    sunk = false;
                }
            } catch (InvalidPositionException ex){

            }
        }
        return sunk;
    }

    /**
     * 
     * A string representation of the board, suitable for printing to the screen
     * 
     */
    public String toString(){ // final letter is j
        String boardRepresentation = "";
        HashMap<Position, String> verticalSymbols = new HashMap<Position, String>();
        boolean flag = true;
        for(Position pos : ships.keySet()){
            if(ships.get(pos).getOrientation()){
                verticalSymbols.put(pos, "^^");
                for(int i = 1; i < ships.get(pos).getShip().getSize() - 1; i++){
                    try{
                        verticalSymbols.put(new Position(pos.getX(), pos.getY()+i), "$$");
                    } catch (InvalidPositionException ex) {
                        
                    }
                }
                try{
                    verticalSymbols.put(new Position(pos.getX(), pos.getY()+ships.get(pos).getShip().getSize()-1), "vv");
                } catch (InvalidPositionException ex){
                    
                }
            }
        }
        System.out.println(verticalSymbols);
        boardRepresentation = "  | 1| 2| 3| 4| 5| 6| 7| 8| 9|10|\n";
        for(int y = 1; y <= 10; y++){
            if(y < 10){
                boardRepresentation += " ";
            }
            boardRepresentation += y + "|";
            for(int x = 1; x <= 10; x++){
                flag = true;
                for(Position pos : ships.keySet()){
                    if(pos.getX() == x && pos.getY() == y && !ships.get(pos).getOrientation()){
                        boardRepresentation += "<=|";
                        for(int i = 1; i < ships.get(pos).getShip().getSize()-1; i++){
                            boardRepresentation += "==|";
                        }
                        boardRepresentation += "=>|";
                        x += ships.get(pos).getShip().getSize()-1;
                        flag = false;
                    }          
                }
                for(Position pos : verticalSymbols.keySet()){
                    if(x == pos.getX() && y == pos.getY()){
                        boardRepresentation += verticalSymbols.get(pos) + "|";
                        verticalSymbols.remove(pos);
                        flag = false;
                        break;
                    } 
                }  
                if(flag){
                    boardRepresentation += "  |";
                }
            }
            boardRepresentation += "\n";
        }
        System.out.println(boardRepresentation);
        return boardRepresentation;
    }
    
    public void emptyBoard(){
        String boardRepresentation = "";
        boardRepresentation = "  |A|B|C|D|E|F|G|H|I|J|\n";
        for(int i = 1; i <= 9; i++){
            boardRepresentation += " " + i + "| | | | | | | | | | |\n"; 
        }
        boardRepresentation += 10 + "| | | | | | | | | | |\n";
        System.out.println(boardRepresentation);
    }

    /**
     * Make a deep clone of the current object. Only clones of boards are to be passed to a
     * player by a game, so that the player does not affect the game state if shots are tried out
     *
     * @return the cloned object
     */
    public BoardInterface clone(){
        BoardInterface boardClone = new Board();
        for(Position pos : ships.keySet()){
            try{
                boardClone.placeShip(ships.get(pos).getShip(), pos, ships.get(pos).getOrientation());
            } catch (InvalidPositionException ex){

            } catch (ShipOverlapException ex){

            }
        }
        return boardClone;
    }

    /**
     * Checks if the ship is verticle,
     * if the target is between the start of the ship and the end of the ship in the y axis
     * and if the x co-ordinates of the target and the start of the ship were the same
     * 
     * @param the start position of a ship which is also the key of the hash map
     * 
     * @param the target position
     */
    public boolean hitY(Position key, Position target){ 
        if(ships.get(key).getOrientation() && key.getY() <= target.getY() && key.getY() + ships.get(key).getShip().getSize() >= target.getY() && target.getX() == key.getX()){
            return true;
        }
        return false;
    }

    /**
     * Checks if the ship is horizontal,
     * if the target is between the start of the ship and the end of the ship in the x axis
     * and if the y co-ordinates of the target and the start of the ship were the same
     * 
     * @param the start position of a ship which is also the key of the hash map
     * 
     * @param the target position
     */
    public boolean hitX(Position key, Position target){
        if(!ships.get(key).getOrientation() && key.getX() <= target.getX() && key.getX() + ships.get(key).getShip().getSize() >= target.getX() && target.getY() == key.getY()){
            return true;
        }
        return false;
    }
}
