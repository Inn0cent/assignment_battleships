

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
    private Ship ship3;
    private Ship ship2;
    private Position pos22;
    private Position pos51;
    private Position pos53;
    private Position pos55;
    private Position pos97;
    private Position pos87;
    private Position pos92;
    
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
        ship3 = new Ship(3);
        ship2 = new Ship(2);
        try{
            pos22 = new Position(2,2);
            pos51 = new Position(5,1);
            pos53 = new Position(5,3);
            pos55 = new Position(5,5);
            pos97 = new Position(9,7);
            pos87 = new Position(8,7);
            pos92 = new Position(9,2);
        } catch (InvalidPositionException ex){
            ex.getMessage();
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
        board.placeShip(ship3, pos53, true);
        board.placeShip(ship2, pos87, false);
        board.toString();
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
}