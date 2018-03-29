import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
/**
 * GameInterface represents the game state including the boards and the players
 *
 * Requires a constructor with two parameters: the two player objects. 
 *
 * Also requires a main method which allows the user to choose the player names and types and start the game. 
 * The main method menu should allow users to: create the players (human or computer); load a game; continue a game; save the game; start a new game; exit the program.
 *
 * If providing a GUI then the same options need to be available through the GUI.
 **/
public class Game implements GameInterface
{

    private Player player1;
    private Player player2;
    private static PlayerInterface winner;
    private Board board1;
    private Board board2;
    private static StringInput stringInput;
    private static Game game;

    public Game(Player player1, Player player2)
    {
        this.player1 = player1;
        this.player2 = player2;
        board1 = new Board();
        board2 = new Board();
        stringInput = new StringInput();
    }

    public static void main(String[] args){
        String choice;
        boolean flag = false;
        String filename;
        winner = null;
        while(!flag){
            System.out.println("1: New Game");
            System.out.println("2: Continue Game");
            System.out.println("3: Load Game");
            System.out.println("4: Save Game");
            System.out.println("5: Exit");
            choice = stringInput.enterData();
            switch(choice){
                case "1": 
                newGame();
                break;
                case "2":
                if(game != null){
                    winner = game.play();
                } else {
                    System.out.println("There is no on going game");
                }
                break;
                case "3":
                System.out.println("Enter filename");
                filename = stringInput.enterData();
                try{
                    game.loadGame(filename);
                    winner = game.play();
                } catch (IOException ex) {
                    System.out.println("File failed to load");
                }
                break;
                case "4":
                if(game != null){
                    System.out.println("Enter filename");
                    filename = stringInput.enterData();
                    try{
                        game.saveGame(filename);
                    } catch (IOException ex){
                        System.out.println("File failed to save");
                    }
                } else {
                    System.out.println("There is no on going game");
                }
                break;
                case "5":
                flag = true;
                break;
                default:
                System.out.println("Please enter a number between 1 and 4");
                break;
            }
            if(winner != null){
                System.out.println("The winner is: " + winner.toString());
            }
        }    
    }

    public static void newGame(){
        String name;
        Player player1;
        Player player2;
        System.out.println("Enter player1's name");
        name = stringInput.enterData();
        player1 = new Player(name);
        System.out.println("Enter player2's name");
        name = stringInput.enterData();
        player2 = new Player(name);
        game = new Game(player1, player2);
        winner = game.play();
    }

    /**
     * Play the game until completion or pause. Should work either for a new game or the continuation of a paused game. 
     * This method should get ask players for ship placements. The board that is passed to the players when choosing placements should be a clone of the game board so that they can try out moves without affecting the state of the game. Once ships are all placed the players should be asked one after another for their choice of shot via their getShot method. 
     * When a shot has been accepted by the game the player should be informed of the result of the shot. 
     * 
     * At any stage during game play a player can choose to pause a game (with a PauseException), which should cause the play method to return
     * 
     * @return the winning player if there is one, or null if not (the game has been paused by a player). If a player tries to take an illegal shot or place a ship illegally then they forfeit the game and the other player immediately wins.
     *
     **/
    public PlayerInterface play(){
        Placement placement;
        Position shot;
        ShipStatus status;
        try{
            ShipInterface[] ships1 = {new Ship(5)};//, new Ship(4), new Ship(3), new Ship(3), new Ship(2)};
            ShipInterface[] ships2 = {new Ship(5)};//, new Ship(4), new Ship(3), new Ship(3), new Ship(2)};
            System.out.println(player1.toString() + " place your ships");
            for(int i = board1.numShipsPlaced(); i < ships1.length; i++){
                placement = player1.choosePlacement(ships1[i], board1.clone());
                try{
                    board1.placeShip(ships1[i], placement.getPosition(), placement.isVertical());
                } catch (InvalidPositionException ex){
                    System.out.println("Player " + player1.toString() + " has forefitted");
                    return player2;
                } catch (ShipOverlapException ex){
                    System.out.println("Player " + player1.toString() + " has forefitted");
                    return player2;
                }
                System.out.println(board1.toString());
                System.out.println();
            }
            
            System.out.println(player2.toString() + " place your ships");
            for(int i = board2.numShipsPlaced(); i < ships2.length; i++){
                placement = player2.choosePlacement(ships2[i], board2.clone());
                try{
                    board2.placeShip(ships2[i], placement.getPosition(), placement.isVertical());
                } catch (InvalidPositionException ex){
                    System.out.println("Player " + player2.toString() + " has forefitted");
                    return player1;
                } catch (ShipOverlapException ex){
                    System.out.println("Player " + player2.toString() + " has forefitted");
                    return player1;
                } 
                System.out.println(board2.toString());
                System.out.println();
            }
            
            System.out.println(board1.toString());
            System.out.println();
            System.out.println(board2.toString());
            System.out.println();
            while(true){
                try{
                    System.out.println(player1.toString() + " take a shot");
                    shot = player1.chooseShot();
                    board2.shoot(shot);
                    player2.opponentShot(shot);
                    status = board2.getStatus(shot);
                    if(status == ShipStatus.NONE){
                        player1.shotResult(shot, ShotStatus.MISS);
                    } else if (status == ShipStatus.HIT){
                        player1.shotResult(shot, ShotStatus.HIT);
                    } else if (status == ShipStatus.SUNK){
                        player1.shotResult(shot, ShotStatus.SUNK);
                    }
                } catch (InvalidPositionException ex){ //This should never get called as all the potential errors are caught before
                    System.out.println("Player " + player1.toString() + " has forefitted");
                    return player2;
                }
                if(board2.allSunk()){
                    return player1;
                }
                try{
                    System.out.println(player2.toString() + " take a shot");
                    shot = player2.chooseShot();
                    board1.shoot(shot);
                    player1.opponentShot(shot);
                    status = board1.getStatus(shot);
                    if(status == ShipStatus.NONE){
                        player2.shotResult(shot, ShotStatus.MISS);
                    } else if (status == ShipStatus.HIT){
                        player2.shotResult(shot, ShotStatus.HIT);
                    } else if (status == ShipStatus.SUNK){
                        player2.shotResult(shot, ShotStatus.SUNK);
                    }
                } catch (InvalidPositionException ex){ //This should never get called as all the potential errors are caught before
                    System.out.println("Player " + player2.toString() + " has forefitted");
                    return player1;
                }
                if(board1.allSunk()){
                    return player2;
                }
                System.out.println(board1.toString());
                System.out.println();
                System.out.println(board2.toString());
                System.out.println();
            }
        } catch (PauseException ex){
            return null;
        }
    }

    /**
     * Save the current state of the game (including the boards and players) into a file so it can be re-loaded and game play continued. You choose what the format of the file is. Java object serialization is not the preferred solution.
     *
     * @param filename the name of the file in which to save the game state
     *
     * @throws IOException when an I/O problem occurs while saving
     **/
    public void saveGame(String filename) throws IOException{
        String p1 = player1.saveString();
        String p2 = player2.saveString();
        String b1 = board1.saveString();
        String b2 = board2.saveString();
        PrintWriter out = new PrintWriter(new FileWriter(filename, true));
        out.println(p1);
        out.println(p2);
        out.println(b1);
        out.println(b2);
        out.close(); 
    }

    /**
     * Load the game state from the given file
     *
     * @param filename  the name of the file from which to load the game state
     *
     * @throws IOException when an I/O problem occurs or the file is not in the correct format (as used by saveGame())
     **/
    public void loadGame(String filename) throws IOException{
        String line = null;
        BufferedReader in = new BufferedReader(new FileReader(filename));
        ArrayList<String> input = new ArrayList<String>();
        try {
            while((line = in.readLine()) != null) {
                input.add(line);
            }            
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");                
        } finally {
            if(in != null){
                in.close();
            }
        }
        
        
    }
}
