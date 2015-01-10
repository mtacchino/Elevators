import java.util.Comparator;
import java.util.PriorityQueue;
 
/*
 * Elevator class
 * 
 * Author: Matt Tacchino
 * 
 * Class representing an elevator.
 * Also contains static int NUM_OF_STORIES representing the number of stories in the building
 * An elevator has a direction, floor, and a priority queue representing the floor requests
 * The priority queue sorts ascending when direction is down, and descending when direction is up
 */
public class Elevator {
	   private Direction direction = Direction.IDLE;
       private int floor = 1; //all elevators start on the first floor
       public static final int NUM_OF_STORIES = 30;
       private PriorityQueue<Integer> requests = new PriorityQueue<Integer>(NUM_OF_STORIES, new Comparator<Integer>(){
              public int compare(Integer a,Integer b){
                     if (direction == Direction.DOWN){
                           if (a < b)
                                  return 1;
                           if (a > b)
                                  return -1;
                     }
                     else if (direction == Direction.UP){
                           if (a < b)
                                  return -1;
                           if (a > b)
                                  return 1;
                     }
                     return 0;
              }
       });
      
       //A button is pressed INSIDE the elevator to request a floor. 
       public void addRequest(int floor){
    	   if (!requests.contains(floor)){ //only continue if the requested floor is not already in requests
    		   if (requests.isEmpty()){ //if this is the only request, set the direction based on where the request is going
	    		   if (this.floor < floor)
	    			   setDirection(Direction.UP);
	    		   else if (this.floor > floor)
	    			   setDirection(Direction.DOWN);
	    		   else //request is same as current floor
	    			   setDirection(Direction.IDLE);
	    	   }
    	   requests.add(floor);
    	   }
       }
       
       public void removeRequest(int floor){
    	   if (requests.contains(floor))
    		   requests.remove(); // the head of the priority queue is removed
       }
      
       //move the elevator up or down one floor
       public void move(){
    	   if (direction == Direction.UP && floor < NUM_OF_STORIES)
               floor += 1;
    	   else if (direction == Direction.DOWN && floor > 1)
               floor -= 1;
       }
       
       public void getPassengers(){
    	   //open the elevator door and collect passengers
       }
       
       //SETTERS
       public void setDirection(Direction direction){
    	   this.direction = direction;
       }
      
       //GETTERS
       public boolean isIdle(){
              return direction == Direction.IDLE;
       }
      
       public int getFloor(){
              return floor;
       }
       
       public PriorityQueue<Integer> getRequests(){
    	   return requests;
       }
       
       public Direction getDirection(){
    	   return direction;
       }
       
       //toString
       public String toString(){
    	   return "Direction: " + direction + "\nFloor: " + floor + "\nRequested floors: " + requests;
       }
}