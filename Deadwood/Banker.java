package Deadwood;/* Banker class
*
* performs bonus rolls when players are on-card when its-a-wrap
* calculates end of game score
* performs upgrade logic
*/

import java.util.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;



public class Banker {

  // ----------------- METHODS ------------------ //


  //-------------------------------------------------------
  // rollDie. Player rolls 1 dice method.  Returns number 1-6
  //-------------------------------------------------------
  public static int rollDie(){
    int diceRoll = 1 + (int)(Math.random() * ((6 - 1) + 1));
    return diceRoll;
  }

  // bonusRoll Player rolls n dice for bonus roll after scene wrap
  //-------------------------------------------------------
  public static List<Integer> bonusRoll(int diceNum){
    int i=0;
    List<Integer> bonusRoll = new ArrayList<Integer>();
    while(i <= (diceNum -1)){
      int randDice = rollDie();
      bonusRoll.add(randDice);
      i++;
    }
    // sort Bonus Roll array from highest to lowest
    Collections.sort(bonusRoll);
    Collections.reverse(bonusRoll);
    return bonusRoll;
  }

  //-------------------------------------------------------
  // rollSuccess
  //
  // Method that pays players for "acting/dice Roll" success either on card or off
  //-------------------------------------------------------
  public static void rollSuccess(Player player){
    // checks to see if player is on card
    if (player.isOnCard() == true){
      player.incrementCredits(2);
    }
    else {
      player.incrementCredits(1);
      player.incrementDollars(1);
    }
  }

  //-------------------------------------------------------
  // rollFail
  //
  // Method that pays extra for failed work
  //-------------------------------------------------------
  public static void rollFail(Player player){
    if (!player.isOnCard()){
      player.incrementDollars(1);
    }
  }

  //-------------------------------------------------------
  // playerUpgrade
  //
  // Notifies players of upgrades available
  // Updates player Rank, Credits & Dollars.
  //-------------------------------------------------------
  public static void playerUpgrade(Player player){

    Integer userChoice = null;
    String dollarsOrCredits = null;

    ArrayList <Integer> upgradeChoices = new ArrayList <Integer>();

    boolean upgradeOffered = false;
    if((player.getDollars() >= 40 || player.getCredits() >= 25) && player.getRank() < 6){
      upgradeOffered = true;
      upgradeChoices.add(6);
    }
    if((player.getDollars() >= 28 || player.getCredits() >= 20) && player.getRank() < 5){
      upgradeOffered = true;
      upgradeChoices.add(5);
    }
    if((player.getDollars() >= 18 || player.getCredits() >= 15) && player.getRank() < 4){
      upgradeOffered = true;
      upgradeChoices.add(4);
    }
    if((player.getDollars() >= 10 || player.getCredits() >=10) && player.getRank() < 3){
      upgradeOffered = true;
      upgradeChoices.add(3);
    }
    if ((player.getDollars() >= 4 || player.getCredits() >=5) && player.getRank() < 2){
      upgradeOffered = true;
      upgradeChoices.add(2);
    }

    //give player drop down of upgrade choices based on qualifications and store userchoice
    if (upgradeOffered == true) {
      userChoice = dController.choiceDialogue(upgradeChoices);
    }

    // rank 6 player is trying to upgrade
    if (upgradeOffered == false && player.getRank() == 6){
      dController.showMessage("You have already reached max rank! No more upgrades available.");
    }

    if (upgradeOffered == false && player.getRank() < 6){
      dController.showMessage("You are not eligible for any upgrades at this time.  Saddle up and get to work!");
    }

    // Player is eligible for upgrade. Ask if they want dollars or Credits if applicable
    if (upgradeOffered == true){
      // RANK 6 Procedure //
      if (userChoice == 6) {
        player.setRank(6);
        //if player's rank qualify's and they have sufficient dollars AND credits.
        if (player.getDollars() >= 40 && player.getCredits() >= 25) {
          dollarsOrCredits = dController.dollarsCreditsDiolog();
          if (dollarsOrCredits.equals("Dollars")) {
            player.decreaseDollars(40);
          } else {
            player.decreaseCredits(25);
          }
        }
        //if player has just enough Dollars for Rank 6 upgrade
        else if(player.getDollars() >= 40) {
          player.decreaseDollars(40);
        }
        //if player has enough Credits for Rank 6 upgrade
        else if (player.getCredits() >= 25) {
          player.decreaseCredits(25);
        }
        dController.upgradeImage(player.getPlayerNumber(), 6);
      } // end of rank 6 procedure

      // RANK 5 Procedure //
      if (userChoice == 5) {
        player.setRank(5);

        //if player's rank qualify's and they have suficcient dollars AND credits.
        if (player.getDollars() >= 28 && player.getCredits() >= 20) {
          dollarsOrCredits = dController.dollarsCreditsDiolog();
          if (dollarsOrCredits.equals("Dollars")) {
            player.decreaseDollars(28);
          } else {
            player.decreaseCredits(20);
          }
        }
        //if player has just enough Dollars for Rank 5 upgrade
        else if (player.getDollars() >= 28) {
          player.decreaseDollars(28);
        }
        //if player has enough Credits for Rank 5 upgrade
        else if (player.getCredits() >= 20) {
          player.decreaseCredits(20);
        }
        dController.upgradeImage(player.getPlayerNumber(), 5);
      } // End of Rank 5 procedure

      // RANK 4 Procedure //
      if (userChoice == 4) {
        player.setRank(4);

        //if player's rank qualify's and they have suficcient dollars AND credits.
        if (player.getDollars() >= 18 && player.getCredits() >= 15) {
          dollarsOrCredits = dController.dollarsCreditsDiolog();
          if (dollarsOrCredits.equals("Dollars")) {
            player.decreaseDollars(18);
          } else {
            player.decreaseCredits(15);
          }
        }
        //if player has just enough Dollars for Rank 4 upgrade
        else if (player.getDollars() >= 18) {
          player.decreaseDollars(18);
        }
        //if player has enough Credits for Rank 4 upgrade
        else if (player.getCredits() >= 15) {
          player.setRank(4);
          player.decreaseCredits(15);
        }
        dController.upgradeImage(player.getPlayerNumber(), 4);
      } // End of Rank 4 procedure

      // RANK 3 Procedure //
      if (userChoice == 3) {
        player.setRank(3);

        //if player's rank qualify's and they have suficcient dollars AND credits.
        if (player.getDollars() >= 10 && player.getCredits() >= 10) {
          dollarsOrCredits = dController.dollarsCreditsDiolog();
          if (dollarsOrCredits.equals("Dollars")) {
            player.decreaseDollars(10);
          } else {
            player.decreaseCredits(10);
          }
        }
        //if player has just enough Dollars for Rank 3 upgrade
        else if (player.getDollars() >= 10) {
          player.decreaseDollars(10);
        }
        //if player has enough Credits for Rank 3 upgrade
        else if (player.getCredits() >= 10) {
          player.decreaseCredits(10);
        }
        dController.upgradeImage(player.getPlayerNumber(), 3);
      } // End of Rank 3 procedure

      // RANK 2 Procedure //
      if (userChoice == 2) {
        player.setRank(2);

        //if player's rank qualify's and they have suficcient dollars AND credits.
        if (player.getDollars() >= 4 && player.getCredits() >= 5) {
          dollarsOrCredits = dController.dollarsCreditsDiolog();
          if (dollarsOrCredits.equals("Dollars")) {
            player.decreaseDollars(4);
          } else {
            player.decreaseCredits(5);
          }
        }
        //if player has just enough Dollars for Rank 2 upgrade
        else if (player.getDollars() >= 4) {
          player.decreaseDollars(4);
        }
        //if player has enough Credits for Rank 2 upgrade
        else if (player.getCredits() >= 5) {
          player.decreaseCredits(5);
        }
        dController.upgradeImage(player.getPlayerNumber(), 2);
      }
    }

  } // end of Upgrade Method


  //-------------------------------------------------------
  // bonusRollPayout
  //
  // AKA Its a Wrap!
  //-------------------------------------------------------
  public static void bonusRollPayout(ArrayList<Player> playersInRoom, Cards card){
    int budget = card.getBudget();

    // All the parts on the card, reversed to match order of bonus dice roll
    ArrayList<Part> partsOnCard = card.getParts();
    Collections.reverse(partsOnCard);

    // this array parallells the parts on card and will accumalate the totals
    // of the dice being distributed to it
    int numPartsOnCard = partsOnCard.size();
    int[] dieDistributionTotals = new int[numPartsOnCard];

    // Roll number of dice equal to # of scene budget
    // Distribute the die among parts, wrapping around to highest ranked scene
    List<Integer> playerBonusRoll = bonusRoll(budget);
    String bonusRoll = playerBonusRoll.toString();
    dController.showMessage("BONUS ROLL!! You rolled a " + bonusRoll + " All actors have been paid.");
    int indexOfDieDistArray = 0;  // tracks index of dieDistribution since number of die could be more than parts
    for (int i = 0; i < playerBonusRoll.size(); i++) {
      if (indexOfDieDistArray == numPartsOnCard) { // we have reached last part, need to wrap to beginning
        indexOfDieDistArray = 0;
      }
      dieDistributionTotals[indexOfDieDistArray] += playerBonusRoll.get(i);
      indexOfDieDistArray++;
    }


    // go through parts on card, if player has part on card then pay with
    // associated die payout from dieDistributionTotals[]
    for (int i = 0; i < partsOnCard.size(); i++) {
      for (int j = 0; j < playersInRoom.size(); j++) {
        Part part = playersInRoom.get(j).getCurrentPart();
        if(part != null && part == partsOnCard.get(i)) {
          playersInRoom.get(j).incrementDollars(dieDistributionTotals[i]);
        }
      }
    }
    //payout players NOT on card
    for(Player player: playersInRoom){
      Part part = player.getCurrentPart();
      if(part != null && !player.isOnCard()){
        player.incrementDollars(player.getPartLevel());
      }
    }
  }


  //-------------------------------------------------------
  // tallyPointsEndOfGame
  //
  // Method that calculates player's points at end of game and declares the winner or tied winners
  //-------------------------------------------------------
  public static void tallyPointsEndOfGame(ArrayList<Player> players){
    int highestPoint = -1;
    String playerWinnerColor = null;
    boolean tieGame = false;
    ArrayList<Player> tiedPlayers = new ArrayList<>();
    ArrayList<String> tiedPlayerColor = new ArrayList<>();
    for(Player player : players) {
      player.setPlayerPoints(player.getDollars() + player.getCredits() + (player.getRank() * 5));
      int playerPoints = player.getPlayerPoints();
      if(playerPoints > highestPoint) {
        highestPoint = playerPoints;
        playerWinnerColor = player.getColor();
      }
    }
    for(Player player : players){
      if(player.getPlayerPoints() == highestPoint){
        tiedPlayers.add(player);
      }
    }

    //if more than one player with high score, print tied message, otherwise winning message
    if (tiedPlayers.size() > 1) {
      for(Player player: tiedPlayers){
      tiedPlayerColor.add(player.getColor());
      }
      String tied = Arrays.toString(tiedPlayerColor.toArray());
      dController.showMessage("Tied game between " + tied + " players");
    }
    else {
    dController.showMessage("The winner of Deadwood is " + playerWinnerColor + "!");
    try {
    TimeUnit.SECONDS.sleep(2);
    System.exit(0);
}catch(Exception e){
    System.out.println("Error in thread sleep");
}
    }
  }



} // end of Banker class
