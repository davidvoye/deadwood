package Deadwood;/* Player class
 *
 * player objects with getters and setters
 */

import java.util.*;

public class Player {


  // -------------------- ATTRIBUTES ---------------------- //
  private int rank;
  private String location;
  private int dollars;
  private int credits;
  private int rehersalPoints;
  private int endOfGamePointTally;
  private boolean acting;
  private boolean onCard;
  private String rollName;
  private List<Integer> bonusRoll = new ArrayList<Integer>();
  private int playerPoints;
  static int numPlayers;
  private Part currentPart;
  private int numPartsPlayed;
  private int playerNumber;
  private boolean waiting;
  private String color;
  private boolean justWrapped;


  // -------------------- CONSTRUCTOR ---------------------- //

  public Player (int numPlayers, int playerNumber){
    this.acting = false;
    this.numPartsPlayed = 0;
    this.rank = 1;
    this.dollars = 0;
    this.credits = 0;
    this.rehersalPoints = 0;
    this.currentPart = null;
    this.acting = false;
    this.numPlayers = numPlayers;
    this.onCard = false;
    this.endOfGamePointTally = 0;
    this.playerNumber = playerNumber;
    this.waiting = true;
    this.justWrapped = false;

    if (numPlayers == 5) {
      this.credits = 2;
    }
    if (numPlayers == 6){
      this.credits = 4;
    }
    if (numPlayers >= 7) {
      this.rank = 2;
      dController.upgradeImage(playerNumber, rank);
    }
  }

  // -------------------- GETTERS ---------------------- //

  public String getColor() {return this.color; }

  public boolean isWaiting() {
    return this.waiting;
  }

  public int getRank() { return this.rank; }

  public int getRehersalPoints() {
    return this.rehersalPoints;
  }

  public int getCredits(){ return this.credits; }

  public int getDollars(){ return this.dollars; }

  public String getLocation(){ return this.location; }

  public boolean isOnCard(){ return this.onCard; }

  public boolean isActing(){ return this.acting; }

  public Part getCurrentPart(){ return this.currentPart; }

  public int getPartLevel(){ return this.currentPart.getLevel(); }

  public int getPlayerPoints(){ return this.endOfGamePointTally; }

  public int getPlayerNumber(){return this.playerNumber;}

  public boolean getJustWrapped(){return  this.justWrapped;}


  // -------------------- SETTERS ---------------------- //

  public void setColor(String color) {this.color = color;}

  public void setWaiting(boolean bool) {
    this.waiting = bool;
  }

  public void incrementDollars(int n){ this.dollars = this.dollars + n; }

  public void incrementCredits(int n){
    this.credits = this.credits + n;
  }

  public void setRank(int n){
    this.rank = n;
  }

  public void addRehearsal(){ rehersalPoints++; }

  public void resetRehearsal(){
    this.rehersalPoints = 0;
  }

  public void actingYes(){
    this.acting = true;
  }

  public void actingNo(){ this.acting = false; }

  public void cardYes(){
    this.onCard = true;
  }

  public void cardNo(){ this.onCard = false; }

  public void setLocation(String roomName){ this.location = roomName; }

  public void incrementNumPartsPlayed(){this.numPartsPlayed++;}

  public void setCurrentPart(Part currentPart){
    this.currentPart = currentPart;
  }

  public void setJustWrapped(boolean val){this.justWrapped = val;}

  public void setPlayerPoints(int num){ this.endOfGamePointTally = num; }

  public void clearPart(){
    this.currentPart = null;
  }

  public void decreaseDollars (int n) {
    int playerDollars = this.getDollars();
    this.dollars = (playerDollars - n);
  }

  public void decreaseCredits(int n) {
    int playerCredits = this.getCredits();
    this.credits = (playerCredits - n);
  }

}