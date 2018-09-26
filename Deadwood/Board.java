/*
 * Board.java
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
import java.io.*;
import java.lang.*;



public class Board {

    private Stack<Cards> cards;
    private ArrayList<Room> rooms;
    private int numScenes;
    private ArrayList<String> playerNames;
    private ArrayList<Player> players;


    //----------------------CONSTRUCTOR-------------------//
    public Board(){

        cards = new Stack<>();
        this.cards = XMLParser.createSceneStack();
        Collections.shuffle(cards);
        this.rooms = XMLParser.createRoomList();
        this.numScenes = 10;
        this.players = players;
        dealSceneCards();
        dController.addCards(rooms);
    }

    //---------------------GETTERS--------------------------//
    public int getNumScenes(){
        return this.numScenes;
    }

    public ArrayList<Room> getRooms(){return  this.rooms;}

    public Room getRoom(String roomName){
        for(int i = 0; i < rooms.size(); i++){
            if(rooms.get(i).getRoomName().equals(roomName)) {
              return rooms.get(i);
            }
        }
        return null;
    }


    //----------------------SETTERS------------------------//
    public void setPlayerList(ArrayList<Player> players){this.players = players;}

    public void setNumScenes(int num){this.numScenes = num;}

    public void sceneRemoved() {
        this.numScenes--;
    }

    public void dealSceneCards(){
        for(int i = 0; i < rooms.size(); i++){
            if(rooms.get(i).isSetRoom()){
                rooms.get(i).setCurrentScene(cards.pop());
            }
        }
    }


    //----------------------METHODS------------------------//
    public void newDay(ArrayList<Player> players) {

        //reset all rooms to empty
        for(Room room : rooms) {
            room.resetRoomPlayers();
            room.resetShotCounters();
            room.removeAllPlayers();
            room.setNumPlayersWaiting(0);
            if(room.isSetRoom()) {
                ArrayList<Part> parts = room.getParts();
                for (Part part : parts) {
                    part.freePart();
                }
            }
        }
        setNumScenes(10);
        Room trailer = getRoom("trailer");
        //reset player location attribute
        for(int i = 0; i < players.size(); i++){
            players.get(i).setLocation("trailer");
            players.get(i).resetRehearsal();
            players.get(i).cardNo();
            players.get(i).actingNo();
            players.get(i).setWaiting(true);
            players.get(i).setCurrentPart(null);
            players.get(i).setJustWrapped(false);
            trailer.addPlayerToRoom(players.get(i));
        }
        trailer.setNumPlayersWaiting(players.size());


        //reset visibility off all on-card part to false
        dController.resetCardVisiblity(this.rooms);
        //new scenes for each room
        dealSceneCards();
        //add card images to UI
        dController.addCards(rooms);


    }
}
