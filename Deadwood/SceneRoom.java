/*
 * SceneRoom.java
 *
 * Chad Schillinger
 * CS 345
 *
 * ----------------------------------------------------------------------------
 *
 * NOTES:
 */
package Deadwood;
import java.util.*;
import java.lang.*;


public class SceneRoom extends Room {


    // -------------------- CONSTRUCTOR ---------------------- //
    public SceneRoom(String name, ArrayList<String> adjRooms, ArrayList<Take> takeCounters,
                     ArrayList<Part> parts, boolean isSetRoom, int shots, int x, int y, int h, int w){
        super.roomName = name;
        super.isSetRoom = isSetRoom;
        super.players = new ArrayList<Player>();
        super.adjacentRooms = adjRooms;
        super.parts = parts;
        super.x = x;
        super.y = y;
        super.h = h;
        super.w = w;
        super.numTakeCounters = shots;
        //need to actually use array of take objects for assign 3/GUI
        super.numTakeCounters = takeCounters.size();
        super.takes = takeCounters.size();
    }

}

