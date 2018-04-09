import java.io.IOException;

public class Placement
{
    private Position position;
    private boolean isVertical;
    
    public Placement(Position position, boolean isVertical){
        this.position = position;
        this.isVertical = isVertical;
    }
    
    public Position getPosition(){
        return position;
    }
    
    public boolean isVertical(){
        return isVertical;
    }
    
    public String saveString(){
        return position.toString() + isVertical;
    }
    
    public static Placement loadPlacement(String input) throws IOException{
        Placement newPlacement = null;
        String[] splitInput = input.split(",");
        try{
            newPlacement = new Placement(new Position(Integer.parseInt(splitInput[0]), Integer.parseInt(splitInput[1])), Boolean.parseBoolean(splitInput[2]));
        } catch (InvalidPositionException ex){
            throw new IOException("Loading error - incorrect file format");
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IOException("Loading error - incorrect file format");
        } catch (IllegalArgumentException ex){
            throw new IOException("Loading error - incorrect file format");
        }
        return newPlacement;
    }
}
