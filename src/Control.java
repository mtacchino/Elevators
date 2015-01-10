import java.util.ArrayList;
import java.util.Calendar;

/*
 * Control class
 * 
 * Author: Matt Tacchino
 * 
 * Controls a list of elevators.
 * When a button is pushed (pressButton method) from outside an elevator, do the following algorithm:
 * 	1) Request the closest "enroute" elevator. This is the closest elevator which will be passing the requested floor
 *  2) If there is no "enroute" elevator, request the closest "idle" elevator. This is the closest elevator with no current requests
 *  3) If there is no "idle" elevator, request the closest "in-use" elevator. This is the closest elevator regardless of requests
 *  
 * The go method processes the requests while elevators are on.
 * From 8am to 10am, any idle elevator is sent to the first floor to pick up the morning rush
 * From 4pm to 6pm, the elevators are sent to floor 30
 * During the weekend and from 10pm to 6am, only one elevator is in use. The other two are set to inactive 
 */
public class Control {
	Elevator elevator1 = new Elevator();
	Elevator elevator2 = new Elevator();
	Elevator elevator3 = new Elevator();
	Elevator elevatorList[] = {elevator1,elevator2,elevator3};
	
	void go(){
		Calendar date = Calendar.getInstance(); //get the current date before start
		//make 2 elevators inactive from 10pm to 6am and on the weekend, but only once the elevator has zero requests, 
		//since we don't want to trap people in an inactive elevator..
		//Enable for the rest of the time.
		if (date.get(Calendar.DAY_OF_WEEK) == 1 || date.get(Calendar.DAY_OF_WEEK) == 7 || date.get(Calendar.HOUR_OF_DAY) < 6 || date.get(Calendar.HOUR_OF_DAY) >= 22){
			if (elevator1.getRequests().isEmpty())
				elevator1.setDirection(Direction.INACTIVE);
			if (elevator2.getRequests().isEmpty())
				elevator2.setDirection(Direction.INACTIVE);
		}
		else {
			if (elevator1.getDirection() == Direction.INACTIVE)
				elevator1.setDirection(Direction.IDLE);
			if (elevator2.getDirection() == Direction.INACTIVE)
				elevator2.setDirection(Direction.IDLE);
		}
		
		for (Elevator e : getActiveElevators()){
			if (e.isIdle()){
				//if current time is between 8am and 10am and elevator is idle, send the elevator to the first floor
				if (date.get(Calendar.HOUR_OF_DAY) >= 8 && date.get(Calendar.HOUR_OF_DAY) < 10)
					e.addRequest(1);
				//if current time is between 4pm and 6pm and elevator is idle, send the elevator to the 30th floor
				else if (date.get(Calendar.HOUR_OF_DAY) >= 16 && date.get(Calendar.HOUR_OF_DAY) < 18)
					e.addRequest(30);
			}
			if (!e.getRequests().isEmpty()){ //check that the elevator has at least one request.
				if (e.getRequests().peek() == e.getFloor())
					e.getPassengers(); //the elevator is on a requested floor. Get the passengers
				else
					e.move();
			}
		}
	}

	/*
	 * method to be run when an "up" or "down" button is pressed on a floor OUTSIDE the elevator
	 */
	void pressButton(int floor, Direction direction) throws Exception{
		//First, try to get the closest enroute elevator
		Elevator closestElevator = getClosestEnrouteElevator(floor,direction);
		if (closestElevator != null){
			closestElevator.addRequest(floor);
			return;
		}
		//There are no enroute elevators, try to get the closest idle elevator
		closestElevator = getClosestIdleElevator(floor);
		if (closestElevator != null) {
			closestElevator.addRequest(floor);
			return;
		}
		//There are no idle or enroute elevators. Get closest in-use elevator
		closestElevator = getClosestInUseElevator(floor);
		if (closestElevator != null) {
			closestElevator.addRequest(floor);
			return;
		}
		throw new Exception("No active elevators could be found."); //this will only happen if all elevators are inactive
	}

	/*
	 * helper method for pressButton to get the closest idle elevator
	 */
	private Elevator getClosestIdleElevator(int floor){
		Elevator closestElevator = null;
		int shortestDistance = Elevator.NUM_OF_STORIES;
		for (Elevator e : getActiveElevators()){
			if (e.isIdle()){
				if (Math.abs(e.getFloor() - floor) < shortestDistance){
					closestElevator = e;
					shortestDistance = Math.abs(e.getFloor() - floor);
				}
			}
		}
		return closestElevator;
	}
	
	/*
	 * helper method for pressButton to get the closest enroute elevator
	 */
	private Elevator getClosestEnrouteElevator(int floor, Direction direction){
		Elevator closestElevator = null;
		int shortestDistance = Elevator.NUM_OF_STORIES;
		for (Elevator e : getActiveElevators()){
			if (e.getDirection() == Direction.UP){
				if (floor >= e.getFloor() && floor <= e.getRequests().peek()){
					if (Math.abs(e.getFloor() - floor) < shortestDistance){
						closestElevator = e;
						shortestDistance = Math.abs(e.getFloor() - floor);
					}
				}
			}
			else if (e.getDirection() == Direction.DOWN){
				if (floor <= e.getFloor() && floor >= e.getRequests().peek()){
					if (Math.abs(e.getFloor() - floor) < shortestDistance){
						closestElevator = e;
						shortestDistance = Math.abs(e.getFloor() - floor);
					}
				}
			}
		}
		return closestElevator;
	}
	
	/*
	 * helper method for pressButton to get the closest elevator (idle, enroute, or in-use)
	 */
	private Elevator getClosestInUseElevator(int floor){
		Elevator closestElevator = null;
		int shortestDistance = Elevator.NUM_OF_STORIES;
		for (Elevator e : getActiveElevators()){
			if (Math.abs(e.getFloor() - floor) < shortestDistance){
				closestElevator = e;
				shortestDistance = Math.abs(e.getFloor() - floor);
			}
		}
		return closestElevator;
	}
	
	/*
	 * return an array list of the elevators that are active
	 */
	public ArrayList<Elevator> getActiveElevators(){
		ArrayList<Elevator> activeElevators = new ArrayList<Elevator>();
		for (Elevator e : elevatorList){
			if (e.getDirection() != Direction.INACTIVE)
				activeElevators.add(e);
		}
		return activeElevators;
	}
}