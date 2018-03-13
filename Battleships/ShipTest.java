
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShipTest
{
    private ShipInterface ship;

    public ShipTest()
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
        ship = new Ship(3);
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
    public void shipBasics() throws InvalidPositionException{
        assertEquals(ship.getSize(), 3);
        assertFalse(ship.isSunk());
        assertEquals(ship.getStatus(0), ShipStatus.INTACT);
        ship.shoot(0);
        assertEquals(ship.getStatus(0), ShipStatus.HIT);
        assertFalse(ship.isSunk());
        ship.shoot(1);
        ship.shoot(2);
        assertEquals(ship.getStatus(0), ShipStatus.SUNK);
        assertTrue(ship.isSunk());
    }

    @Test(expected = InvalidPositionException.class)
    public void shipException() throws InvalidPositionException{
        ship.getStatus(4);
    }
}
