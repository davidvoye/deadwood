/*
 * Room.java
 *
 * Chad Schillinger
 * CS 345
 *
 * ----------------------------------------------------------------------------
 *
 * NOTES: test
 */
package Deadwood;
import java.util.ArrayList;


public class Room {

     String roomName;
     boolean isSetRoom;
     int x;
     int y;
     int h;
     int w;
     ArrayList<String> adjacentRooms;
     ArrayList<Player> players;
     ArrayList<Part> parts;
     Cards currentScene;
     ArrayList<Take> takeCounters;
     ArrayList<Part> part;
     int numTakeCounters;
     int takes;
     int numPlayersWaiting;


    //-----------------------CONSTRUCTOR------------------------------//
     public Room(String name, ArrayList<String> adjRooms, boolean isSetRoom,  int x, int y, int h, int w){
          this.roomName = name;
          this.players = new ArrayList<Player>();
          this.adjacentRooms = adjRooms;
          this.x = x;
          this.y = y;
          this.h = h;
          this.w = w;
          this.isSetRoom = isSetRoom;
          this.currentScene = null;
          this.numPlayersWaiting = 0;
     }

     public Room(){
         //default constructor
     }


     //-----------------------GETTERS------------------------------//
     public int getNumPlayersWaiting() {return this.numPlayersWaiting;}

     public ArrayList<Player> getPlayers(){
         return this.players;
     }

     public int getRemainingShotCounters(){return numTakeCounters;}

     public void addPlayerToRoom(Player player){ players.add(player); }

     public void resetRoomPlayers(){ players.clear(); }

     public ArrayList<String> getAdjRooms(){ return this.adjacentRooms; }

     public String getRoomName(){ return this.roomName; }

     public ArrayList<Part> getParts(){ return this.parts; }

     public boolean isSetRoom(){return isSetRoom;}

    public int getOrigShotCount() { return this.takes; }

     public Cards getCurrentScene() {
         if(isSetRoom){
             return this.currentScene;
         }
         return null;
     }


     //---------------------SETTERS------------------------------
     public void setNumPlayersWaiting(int num) {
         this.numPlayersWaiting = num;
     }

     public void decrementShotCounter(){this.numTakeCounters--;}

     public void setCurrentScene(Cards scene){this.currentScene = scene;}

     public void removeSceneCard(){this.currentScene = null;}

     public void resetShotCounters(){this.numTakeCounters = takes;}

     public void removePlayerFromRoom(Player player){players.remove(player);}

     public void removeAllPlayers() {players.clear();};
}
