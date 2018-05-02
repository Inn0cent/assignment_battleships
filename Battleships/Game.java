import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Random;
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

    private HumanConsolePlayer player1;
    private HumanConsolePlayer player2;
    private static PlayerInterface winner;
    private Board board1;
    private Board board2;
    private static StringInput stringInput;
    private static Game game;
    private static boolean humanPlayer1;
    private static boolean humanPlayer2;
    private Random rand = new Random();

    public Game(HumanConsolePlayer player1, HumanConsolePlayer player2)
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
        stringInput = new StringInput();
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
                    game = new Game(null, null);
                    game.loadGame(filename);
                    winner = game.play();
                } catch (IOException ex) {
                    ex.getMessage();
                    System.out.println("File failed to load");
                } catch (NullPointerException ex){
                    System.out.println(ex.getMessage());
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
                System.out.println("Please enter a number between 1 and 5");
                break;
            }
            if(winner != null && flag == false){
                System.out.println("The winner is: " + winner.toString());
                System.out.println();
                game = null;
                winner = null;
            }
        }    
    }

    public static void newGame(){
        String input;
        HumanConsolePlayer player1;
        HumanConsolePlayer player2;
        System.out.println("Enter player1's name");
        input = stringInput.enterData();
        player1 = new HumanConsolePlayer(input);
        System.out.println("Is " + input + " human? (y/n)");
        input = stringInput.enterData();
        while(!input.equals("y") && !input.equals("n")){
            System.out.println("Please only enter y or n");
            input = stringInput.enterData();
        }
        if(input.equals("y")){
            humanPlayer1 = true;
        } else {
            humanPlayer1 = false;
        }
        System.out.println("Enter player2's name");
        input = stringInput.enterData();
        player2 = new HumanConsolePlayer(input);
        System.out.println("Is " + input + " human? (y/n)");
        input = stringInput.enterData();
        while(!input.equals("y") && !input.equals("n")){
            System.out.println("Please only enter y or n");
            input = stringInput.enterData();
        }
        if(input.equals("y")){
            humanPlayer2 = true;
        } else {
            humanPlayer2 = false;
        }
        game = new Game(player1, player2);
        System.out.println();
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
        try{
            ShipInterface[] ships1 = {new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2)};
            ShipInterface[] ships2 = {new Ship(5), new Ship(4), new Ship(3), new Ship(3), new Ship(2)};
            if(humanPlayer1){
                try{
                    board1 = playerPlaceShips(player1, board1, ships1);
                } catch (InvalidPositionException ex){
                    System.out.println("Player " + player1.toString() + " has forefitted");
                    return player2;
                } catch (ShipOverlapException ex){
                    System.out.println("Player " + player1.toString() + " has forefitted");
                    return player2;
                }
            } else {
                try{
                    board1 = aiPlaceShips(board1, ships1, player1);
                } catch (InvalidPositionException ex){
                    System.out.println("Player " + player1.toString() + " has forefitted");
                    return player2;
                } catch (ShipOverlapException ex){
                    System.out.println("Player " + player1.toString() + " has forefitted");
                    return player2;
                }
            }

            if(humanPlayer2){
                try{
                    board2 = playerPlaceShips(player2, board2, ships2);
                } catch (InvalidPositionException ex){
                    System.out.println("Player " + player2.toString() + " has forefitted");
                    return player1;
                } catch (ShipOverlapException ex){
                    System.out.println("Player " + player2.toString() + " has forefitted");
                    return player1;
                }
            } else {
                try{
                    board2 = aiPlaceShips(board2, ships2, player2);
                } catch (InvalidPositionException ex){
                    System.out.println("Player " + player2.toString() + " has forefitted");
                    return player1;
                } catch (ShipOverlapException ex){
                    System.out.println("Player " + player2.toString() + " has forefitted");
                    return player1;
                }
            }         

            while(true){
                try{
                    shoot(player1, board1, player2, board2, humanPlayer1);
                } catch (InvalidPositionException ex){ //This should never get called as all the potential errors are caught before
                    System.out.println("Player " + player1.toString() + " has forefitted.");
                    return player2;
                }
                if(board2.allSunk()){
                    return player1;
                }
                System.out.println();
                try{
                    shoot(player2, board2, player1, board1, humanPlayer2);
                } catch (InvalidPositionException ex){ //This should never get called as all the potential errors are caught before
                    System.out.println("Player " + player2.toString() + " has forefitted.");  
                    return player1;
                }
                if(board1.allSunk()){
                    return player2;
                }
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
        PrintWriter out = new PrintWriter(new FileWriter(filename, false));
        out.println(p1);
        out.println(p2);
        out.println(b1);
        out.println(b2);
        out.println(humanPlayer1);
        out.println(humanPlayer2);
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
        try{
            player1 = HumanConsolePlayer.loadPlayer(input.get(0));
            player2 = HumanConsolePlayer.loadPlayer(input.get(1));
            board1 = Board.loadBoard(input.get(2));
            board2 = Board.loadBoard(input.get(3));
            humanPlayer1 = Boolean.parseBoolean(input.get(4));
            humanPlayer2 = Boolean.parseBoolean(input.get(5));
        } catch (IndexOutOfBoundsException ex){
            //System.out.println("Loading error - incorrect file format");
            throw new IOException("Loading error - incorrect file format");
        }
    }

    public Board playerPlaceShips(HumanConsolePlayer player, Board board, ShipInterface[] ships) throws InvalidPositionException, ShipOverlapException, PauseException {
        Placement placement;
        System.out.println(player.toString() + " place your ships");
        System.out.println(board.toString());
        for(int i = board.numShipsPlaced(); i < ships.length; i++){
            placement = player.choosePlacement(ships[i], board.clone());            
            board.placeShip(ships[i], placement.getPosition(), placement.isVertical());
            System.out.println(board.toString());
            System.out.println();
        }
        return board;
    }

    public Board aiPlaceShips(Board board, ShipInterface[] ships, HumanConsolePlayer player) throws InvalidPositionException, ShipOverlapException, PauseException{
        Placement place;
        for(int i = board.numShipsPlaced(); i < ships.length; i++){
            place = player.aiPlacement(board.clone(), ships[i]);
            board.placeShip(ships[i], place.getPosition(), place.isVertical());
        }
        return board;
    }

    public void shoot(HumanConsolePlayer player, Board board, HumanConsolePlayer oppPlayer, Board oppBoard, boolean human) throws InvalidPositionException, PauseException{
        Position shot = null;
        ShipStatus status;
        if(human){
            System.out.println(player.toString() + "'s board");
            System.out.println(board.toString());
            System.out.println();
            System.out.println(player.toString() + " take a shot");
            shot = player.chooseShot();
        } else {
            shot = player.aiShoot();
            System.out.println("Computer " + player.toString() + " shot: " + shot);
        }
        oppBoard.shoot(shot);
        oppPlayer.opponentShot(shot);
        status = oppBoard.getStatus(shot);
        if(status == ShipStatus.NONE){
            player.shotResult(shot, ShotStatus.MISS);
            System.out.println("miss");
        } else if (status == ShipStatus.HIT){
            player.shotResult(shot, ShotStatus.HIT);
            System.out.println("hit");
        } else if (status == ShipStatus.SUNK){
            player.shotResult(shot, ShotStatus.SUNK);
            System.out.println("sunk");
        }
    }
}
