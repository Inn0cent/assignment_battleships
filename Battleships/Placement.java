
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
    
    public static Placement loadPlacement(String input){
        Placement newPlacement = null;
        String[] splitInput = input.split(",");
        try{
            newPlacement = new Placement(new Position(Integer.parseInt(splitInput[0]), Integer.parseInt(splitInput[1])), Boolean.parseBoolean(splitInput[2]));
        } catch (InvalidPositionException ex){
            System.out.println("Loading error - save corrupted");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Incorrect file format");
        }
        return newPlacement;
    }
}
