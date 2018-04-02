import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

/**
 * The test class AItest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class AItest
{
    
    private Position pos22;
    private Position pos51;
    private Position pos53;
    private Position pos55;
    private Position pos97;
    private Position pos87;
    private Position pos92;
    private Position pos23;
    private Position pos24;
    private HashMap<Position, ShotStatus> map;
    private AI ai;
    
    /**
     * Default constructor for test class AItest
     */
    public AItest()
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
        map = new HashMap();
        ai = new AI();
        try{
            pos22 = new Position(2,2);
            pos23 = new Position(2,3);
            pos24 = new Position(2,4);
            pos51 = new Position(5,1);
            pos53 = new Position(5,3);
            pos55 = new Position(5,5);
            pos97 = new Position(9,7);
            pos87 = new Position(8,7);
            pos92 = new Position(9,2);            
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
    public void aiCheck(){
        Position pos;
        Position prevPos = null;
        while(!mapContainsPos(map, pos22) || !mapContainsPos(map, pos23) || !mapContainsPos(map, pos24)){
            pos = ai.shoot(map, prevPos);
            prevPos = pos;
            System.out.println(pos);
            if(pos.equals(pos22) || pos.equals(pos23) || pos.equals(pos24)){
                map.put(pos, ShotStatus.HIT);
            } else {
                map.put(pos, ShotStatus.MISS);
            }
        }
        System.out.println(map);
    }
    
    @Test
    public void nextShotTest(){
        Position pos;
        Position prevPos = pos22;
        map.put(prevPos, ShotStatus.HIT);
        pos = ai.shoot(map, prevPos);
        prevPos = pos;
        System.out.println(pos);
        map.put(prevPos, ShotStatus.MISS);
        pos = ai.shoot(map, prevPos);
        prevPos = pos;
        System.out.println(pos);
    }
    
    @Test 
    public void randomTest() throws InvalidPositionException{
        Position pos;
        for(int i = 0; i < 100; i++){
            pos = ai.randomShot(map);
            map.put(pos, ShotStatus.MISS);
            System.out.println(pos);
        }
        System.out.println();
        System.out.println(map);
        System.out.println();
        for(int i = 1; i <= 10; i++){
            for(int j = 1; j <= 10 ; j++){
                if(!map.containsKey(new Position(i,j))){
                    System.out.println("" + i + j);
                }
            }
        }
    } 
    
    public boolean mapContainsPos(HashMap<Position, ShotStatus> prevShots, Position pos){ //map.contains() does not work in this situation
        for(Position key : prevShots.keySet()){
            if(key.equals(pos)){
                return true;
            }
        }
        return false;
    }   
}
