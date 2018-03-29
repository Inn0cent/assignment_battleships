import java.util.HashMap;
import java.util.ArrayList;
/**
 *
 * This type represents individual players in the game of battleships.
 * The methods provided will be used by the Game class as the game is played. 
 * The game will initially call choosePlacement once for each ship. 
 * Once all ships are placed then the game will repeatedly (in turn) call the chooseShot method. 
 * After each chooseShot method the game will inform the player of the result of the shot with the shotResult method.
 * The player will also be informed of opponent's shots with the opponentShot method. This could be used to update a local copy of the game board for display to the user.
 * 
 */
public class Player implements PlayerInterface
{
    
    private String name;
    private StringInput input;
    private HashMap<Position, ShotStatus> prevShots;
    private ArrayList<Position> oppShots;
    
    public Player(String name)
    {
        this.name = name;
        input = new StringInput();
        prevShots = new HashMap<Position, ShotStatus>();
        oppShots = new ArrayList<Position>();
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
        Position position = null;
        boolean isVertical = false;
        String[] inputString;
        String vertInput;
        int x;
        int y;
        boolean correct = false;
        boolean valid = false;
        while(!valid){
            valid = true;
            System.out.println("Place a ship of length " + ship.getSize());
            position = enterCoordinate();
            while(!correct){
                correct = true;
                System.out.print("Do you want to place horizontally or vertically (h/v): ");
                vertInput = input.enterData();
                System.out.println();
                if(vertInput.equals("h")){
                    isVertical = false;
                } else if (vertInput.equals("v")){
                    isVertical = true;
                } else if (vertInput.toLowerCase().equals("pause")){
                    throw new PauseException();
                } else {
                    System.out.println("Invalid input");
                    correct = false;
                }
            }
            try{
                board.placeShip(ship, position, isVertical);
            } catch (InvalidPositionException ex){
                System.out.println("Not all the squares in the ship are on the board");
                valid = false;
                correct = false;
            } catch (ShipOverlapException ex) {
                System.out.println("The ship overlaps another ship");
                valid = false;
                correct = false;
            }
        }
        return new Placement(position, isVertical);
    }

    /**
     * @return The shot chosen by the player
     * 
     * @throws PauseException At any stage the user can choose to enter "pause" (case insensitive) to make the game return to 
     * the main menu
     */
    public Position chooseShot() throws PauseException{
        return enterCoordinate();
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
    
    public Position enterCoordinate() throws PauseException{
        int x;
        int y;
        boolean correct = false;
        while(!correct){
            correct = true;
            System.out.print("Enter coordinates in the form x,y: ");
            String[] inputString = input.enterData().split(",");
            if(inputString[0].toLowerCase().equals("pause")){
                throw new PauseException();
            }
            try{
                x = Integer.parseInt(inputString[0]);
                y = Integer.parseInt(inputString[1]);
                return new Position(x,y);
            } catch (NumberFormatException ex){
                System.out.println("Please enter numbers and don't have any spaces");
                correct = false;
            } catch (InvalidPositionException ex){
                System.out.println("Please enter numbers that are between 1 and 10");
                correct = false;
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("Rememer to use a comma as a delimiter");
                correct = false;
            }
        }
        return null;
    }
    
    public String saveString(){
        String save = name + ";";
        for(Position pos : prevShots.keySet()){
            save += pos.toString() + ":" + prevShots.get(pos).name() + "/";
        }
        save += ";";
        for(Position pos : oppShots){
            save += pos.toString() + "/";
        }
        return save;
    }
    
    public Player loadPlayer(String input){
        Player newPlayer;
        String[] splitInput = input.split(";");
        String[] prevShotsString = splitInput[1].split("/");
        newPlayer = new Player(splitInput[0]);
        for(String str : prevShotsString){
            String[] posStatus = str.split(":");
            String[] pos = posStatus[0].split(",");
            newPlayer.shotResult(new Position(Integer.parseInt(pos[0]), Integer.parseInt(pos[1])), ShotStatus.valueOf(posStatus[1]));
        }
        return newPlayer;
    }
}
