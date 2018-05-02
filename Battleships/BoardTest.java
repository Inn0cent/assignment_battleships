
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class BoardTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class BoardTest
{

    private Board board;
    private Ship ship5;
    private Ship ship4;
    private Ship ship3;
    private Ship ship32;
    private Ship ship2;
    private Position pos22;
    private Position pos51;
    private Position pos53;
    private Position pos55;
    private Position pos97;
    private Position pos87;
    private Position pos92;
    private Position pos23;
    private Position pos62;
    private Position pos72;

    /**
     * Default constructor for test class BoardTest
     */
    public BoardTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        board = new Board();
        ship5 = new Ship(5);
        ship4 = new Ship(4);
        ship3 = new Ship(3);
        ship32 = new Ship(3);
        ship2 = new Ship(2);
        try{
            pos22 = new Position(2,2);
            pos51 = new Position(5,1);
            pos53 = new Position(5,3);
            pos55 = new Position(5,5);
            pos97 = new Position(9,7);
            pos87 = new Position(8,7);
            pos92 = new Position(9,2);
            pos23 = new Position(2,3);
            pos62 = new Position(6,2);
            pos72 = new Position(7,2);
        } catch (InvalidPositionException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    @Test
    public void noPlaceErrors() throws InvalidPositionException, ShipOverlapException{
        board.placeShip(ship5, pos22, false);
        board.placeShip(ship3, pos23, true);
        board.placeShip(ship2, pos87, false);
        //System.out.println(board.toString());
        //board.shoot(pos62);
        //board.shoot(pos72);
        board.getStatus(pos72);
    }

    @Test (expected = ShipOverlapException.class)
    public void overlapPlaceErrors() throws InvalidPositionException, ShipOverlapException {
        board.placeShip(ship5, pos22, false);
        board.placeShip(ship3, pos53, true);
        board.placeShip(ship2, pos55, false);
    }

    @Test (expected = InvalidPositionException.class)
    public void offEdgePlaceErrors() throws InvalidPositionException, ShipOverlapException {
        board.placeShip(ship5, pos22, false);
        board.placeShip(ship3, pos53, true);
        board.placeShip(ship2, pos97, false);
    }
    
    @Test
    public void noShootErrors() throws InvalidPositionException, ShipOverlapException{
        board.placeShip(ship5, pos22, false);
        board.placeShip(ship3, pos53, true);
        board.placeShip(ship2, pos87, false);
        
        board.shoot(new Position(4,2));
        assertEquals(ship5.getStatus(2), ShipStatus.HIT);
        board.toString();
        board.shoot(new Position(1,1));
        board.shoot(pos22);
        board.shoot(new Position(3,2));
        board.shoot(new Position(5,2));
        board.shoot(new Position(6,2));
        assertEquals(ship5.getStatus(0), ShipStatus.SUNK);
        board.toString();
        board.shoot(pos53);
        assertEquals(ship3.getStatus(0), ShipStatus.HIT);
        board.shoot(new Position(5,4));
        board.shoot(new Position(5,5));
        board.shoot(pos87);
        board.shoot(new Position(9,7));
        assertEquals(board.allSunk(), true);
        board.toString();
    }
    
    @Test //(expected = ShipOverlapException.class)
    public void shipOnTop() throws InvalidPositionException, ShipOverlapException{
        BoardInterface clone;
        board.clone().placeShip(ship5, new Position(6,8), false);
        board.placeShip(ship5, new Position(6,8), false);
        //System.out.print("5");
        board.clone().placeShip(ship4, new Position(5,1), true);
        board.placeShip(ship4, new Position(5,1), true);
        //System.out.print("4");
        board.clone().placeShip(ship3, new Position(3,6), true);
        board.placeShip(ship3, new Position(3,6), true);
        //System.out.print("3");
        board.clone().placeShip(ship32, new Position(2,5), false);
        board.placeShip(ship32, new Position(2,5), false);
        //System.out.print("3!");
        System.out.print("placed");
        clone = board.clone();
        clone.placeShip(ship2, new Position(10,8), true);
        //board.placeShip(ship3, pos53, false);
        System.out.println("Fail");
    }
}
