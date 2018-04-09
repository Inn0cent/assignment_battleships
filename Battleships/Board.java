import java.util.HashMap;
import java.io.IOException;
/**
 * Write a description of class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Board implements BoardInterface
{

    private java.util.HashMap<Ship, Placement> ships;

    public Board()
    {
        ships = new java.util.HashMap<Ship, Placement>();
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
        Ship newShip = (Ship) ship; //so it can be stored as type ship
        if((isVertical && position.getY() + newShip.getSize() - 1 > 10) || (!isVertical && position.getX() + newShip.getSize() - 1 > 10)){ //do not have to check if less than 0 as Position already does that
            throw new InvalidPositionException("Not all the squares fit onto the board");
        } 
        
        for(ShipInterface placedShip : ships.keySet()){
            lookPosition.setX(position.getX());
            lookPosition.setY(position.getY());
            for(int i = 0; i < newShip.getSize(); i++){
                if(hitX(placedShip, lookPosition) || hitY(placedShip, lookPosition)){
                    throw new ShipOverlapException("Ships have overlapped at (" + lookPosition + ")");
                }
                if(isVertical){                    
                    lookPosition.setY(lookPosition.getY() + 1);
                } else {
                    lookPosition.setX(lookPosition.getX() + 1);
                }
            }
        }
        ships.put(newShip, new Placement(position, isVertical));
    }

    /**
     * Update the board state by shooting at the given position
     * 
     * @throws InvalidPositionException if the shot position is not on the board
     */
    public void shoot(Position position) throws InvalidPositionException{ // Any position that is passed must be on the board
        boolean hits = false;
        for(ShipInterface ship : ships.keySet()){
            if(hitY(ship, position)){
                try{ // this ensures that the shot hits the ship
                    ship.shoot(position.getY() - ships.get(ship).getPosition().getY());
                    System.out.println("hit");
                } catch (InvalidPositionException ex){
                    System.out.println("miss");
                }
                hits = true;
                break;
            } else if(hitX(ship, position)){
                try{ // this ensures that the shot hits the ship
                    ship.shoot(position.getX() - ships.get(ship).getPosition().getX());
                    System.out.println("hit");
                } catch (InvalidPositionException ex){
                    System.out.println("miss");
                }
                hits = true;
                break;
            } 
        }
        if(!hits){
            System.out.println("miss");
            System.out.println();
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
        for(ShipInterface ship : ships.keySet()){
            if(hitX(ship, position)){
                return ship.getStatus(position.getX() - ships.get(ship).getPosition().getX());
            } else if(hitY(ship, position)){
                return ship.getStatus(position.getY() - ships.get(ship).getPosition().getY());
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
        for(ShipInterface ship : ships.keySet()){
            try{
                if(ship.getStatus(0) != ShipStatus.SUNK){
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
        for(ShipInterface ship : ships.keySet()){
            if(ships.get(ship).isVertical()){
                try {
                    if(ship.getStatus(0) == ShipStatus.HIT || ship.getStatus(0) == ShipStatus.SUNK){
                        verticalSymbols.put(ships.get(ship).getPosition(), "XX");
                    } else {
                        verticalSymbols.put(ships.get(ship).getPosition(), "/\\");
                    }
                    for(int i = 1; i < ship.getSize() - 1; i++){
                        if(ship.getStatus(i) == ShipStatus.HIT || ship.getStatus(i) == ShipStatus.SUNK){
                            verticalSymbols.put(new Position(ships.get(ship).getPosition().getX(), ships.get(ship).getPosition().getY()+i), "XX");
                        } else {
                            verticalSymbols.put(new Position(ships.get(ship).getPosition().getX(), ships.get(ship).getPosition().getY()+i), "$$");
                        }
                    }
                    if(ship.getStatus(ship.getSize()-1) == ShipStatus.HIT || ship.getStatus(ship.getSize()-1) == ShipStatus.SUNK){
                        verticalSymbols.put(new Position(ships.get(ship).getPosition().getX(), ships.get(ship).getPosition().getY()+ship.getSize()-1), "XX");
                    } else {
                        verticalSymbols.put(new Position(ships.get(ship).getPosition().getX(), ships.get(ship).getPosition().getY()+ship.getSize()-1), "\\/");
                    }
                } catch (InvalidPositionException ex){
                    
                }
            }
        }
        boardRepresentation = "  | 1| 2| 3| 4| 5| 6| 7| 8| 9|10|\n";
        for(int y = 1; y <= 10; y++){
            if(y < 10){
                boardRepresentation += " ";
            }
            boardRepresentation += y + "|";
            for(int x = 1; x <= 10; x++){
                flag = true;
                for(ShipInterface ship : ships.keySet()){
                    if(ships.get(ship).getPosition().getX() == x && ships.get(ship).getPosition().getY() == y && !ships.get(ship).isVertical()){
                        try{
                            if(ship.getStatus(0) == ShipStatus.HIT || ship.getStatus(0) == ShipStatus.SUNK){
                                boardRepresentation += "XX|";
                            } else {
                                boardRepresentation += "<=|";
                            }
                            for(int i = 1; i < ship.getSize()-1; i++){
                                if(ship.getStatus(i) == ShipStatus.HIT || ship.getStatus(i) == ShipStatus.SUNK){
                                    boardRepresentation += "XX|";
                                } else {
                                    boardRepresentation += "==|";
                                }
                            }
                            if(ship.getStatus(ship.getSize()-1) == ShipStatus.HIT || ship.getStatus(ship.getSize()-1) == ShipStatus.SUNK){
                                boardRepresentation += "XX|";
                            } else {
                                boardRepresentation += "=>|";
                            }
                        } catch (InvalidPositionException ex) {
                            
                        }
                        x += ship.getSize()-1;
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
        for(ShipInterface ship : ships.keySet()){
            try{
                boardClone.placeShip(ship, ships.get(ship).getPosition(), ships.get(ship).isVertical());
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
    public boolean hitY(ShipInterface key, Position target){ 
        if(ships.get(key).isVertical() && ships.get(key).getPosition().getY() <= target.getY() && ships.get(key).getPosition().getY() + key.getSize() > target.getY() && target.getX() == ships.get(key).getPosition().getX()){
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
    public boolean hitX(ShipInterface key, Position target){
        if(!ships.get(key).isVertical() && ships.get(key).getPosition().getX() <= target.getX() && ships.get(key).getPosition().getX() + key.getSize() > target.getX() && target.getY() == ships.get(key).getPosition().getY()){
            return true;
        }
        return false;
    }
    
    public int numShipsPlaced(){
        return ships.size();
    }
    
    public String saveString(){
        String save = "";
        for(Ship ship : ships.keySet()){
            save += ship.saveString() + ":" + ships.get(ship).saveString() + ";";
        }
        return save;
    }
    
    public static Board loadBoard(String input) throws IOException{
        Board newBoard = new Board();
        String[] splitInput = input.split(";");
        for(String str : splitInput){
            String[] shipPlacement = str.split(":");
            try{
                Placement newPlace = Placement.loadPlacement(shipPlacement[1]);
                newBoard.placeShip(Ship.loadShip(shipPlacement[0]), newPlace.getPosition(), newPlace.isVertical());
            } catch (InvalidPositionException ex){
                throw new IOException("Loading error - incorrect file format");
            } catch (ShipOverlapException ex){
                throw new IOException("Loading error - incorrect file format");
            }
        }
        return newBoard;
    }
}
