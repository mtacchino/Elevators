# Elevators
Algorithm for controlling 3 elevators in a 30 story building
---------------------------------------------------------------
When a button is pushed (pressButton method) from outside an elevator, do the following algorithm:
1) Request the closest "enroute" elevator. This is the closest elevator which will be passing the requested floor
2) If there is no "enroute" elevator, request the closest "idle" elevator. This is the closest elevator with no current requests
3) If there is no "idle" elevator, request the closest "in-use" elevator. This is the closest elevator regardless of requests

The go method processes the requests while elevators are on.

Notes:
A priority queue sorts elevator requests ascending when direction is down, and descending when direction is up
From 8am to 10am, any idle elevator is sent to the first floor to pick up the morning rush
From 4pm to 6pm, the elevators are sent to floor 30
During the weekend and from 10pm to 6am, only one elevator is in use. The other two are set to inactive 
