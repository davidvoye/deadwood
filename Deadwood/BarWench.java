package Deadwood;/*
 * BarWench class
 *
 * Performs board management and prompts players
 * at beginning of terms for input, and checks
 * for new day and game over after every player turn
 *
 */

import javafx.fxml.FXMLLoader;

import java.lang.Boolean;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.util.concurrent.TimeUnit;


public class BarWench {

    private int daysLeft;
    private int numPlayers;
    private ArrayList<Player> playerList;
    private Board board;
    private int turn;
    private dController controller;
    private Player currentPlayer;
    private String currentRoomString;
    private Room currentRoomObject;



    public BarWench(int numPlayers, dController controller){
        this.numPlayers = numPlayers;
        this.controller = controller;
    }

    //--------------------------------GETTERS-------------------------//


    public Board getBoard() {
        return board;
    }

    public String getCurrentRoom(){
        return this.currentRoomString;
    }

    public ArrayList<Player> getPlayers() {
        return playerList;
    }

    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    //-------------------------------------------------------
    // gameStart
    //
    // creates board object and list of player objects
    //-------------------------------------------------------
    public void gameStart(int numPlayers) {
        // set number of players
        this.numPlayers = numPlayers;

        // initialize gameboard object
        this.board = new Board();

        // helper to track which players turn it is
        this.turn = 0;

        // set appropriate number of days
        this.daysLeft = 3;
        if (numPlayers == 2 || numPlayers == 3) {
            this.daysLeft = 2;
        }

        // get list of all player objects
        this.playerList = createPlayerArrayList(numPlayers);

        // update board
        this.board.setPlayerList(playerList);

        dController.displayDaysOnBoard(this.daysLeft);

        // start gameplay
        playerTurn();
    }

    //-------------------------------------------------------
    // playerTurn
    //
    // starts playerTurn, retrieves and sets player object info
    //-------------------------------------------------------
    public void playerTurn() {
        // get current player
        int playerNumber = this.turn % this.numPlayers;

        //increment turn count so next time playerTurn is called it gets the next player
        this.turn++;

        // set current player to Controller
        controller.setCurrentPlayer(playerNumber+1);

        // set current player and their location to global variables
        this.currentPlayer = this.playerList.get(playerNumber);
        this.currentRoomString = this.currentPlayer.getLocation();
        this.currentRoomObject = this.board.getRoom(currentRoomString);


        // initiate player options based on their status
        playerOptions(this.currentPlayer);
    }

    //-------------------------------------------------------
    // playerOptions
    //
    // gives turn options to players based on their status
    //-------------------------------------------------------
    private void playerOptions(Player player) {
        boolean isWorking = player.isActing();
        boolean canRehearse = false;
        boolean canTakeRole = false;
        boolean canUpgrade = false;

        // player is not work, can only move
        if (!isWorking) {

            // if player was waiting in room moving from, reset player's waiting boolean and decrement rooms player waiting count

            this.currentPlayer.setWaiting(false);
            if(!this.currentPlayer.getJustWrapped()) {
                this.currentRoomObject.setNumPlayersWaiting(currentRoomObject.getNumPlayersWaiting() - 1);
            }else{
                this.currentPlayer.setJustWrapped(false);
            }

            // remove player from room they are moving from
            this.currentRoomObject.removePlayerFromRoom(this.currentPlayer);

            // get list of adjacent rooms player can move to
            ArrayList<String> adjRoomButtons = new ArrayList<>();
            try {
                adjRoomButtons = this.currentRoomObject.getAdjRooms();
            }
            catch(NullPointerException e){
                System.exit(0);
            }

            // pass list of buttons to controller
            controller.setButtonBorders(adjRoomButtons,  true);
        }

        // player is cardRooming
        if (isWorking) {
            int budget = currentRoomObject.getCurrentScene().getBudget();
            int rehearsalPoints = player.getRehersalPoints();

            // check if player is eligible to rehearse
            if (rehearsalPoints < budget - 1) {
                canRehearse = true;
            }

            int choice = controller.workOrRehearse(canRehearse);

            // player chose to act
            if (choice == 0) {
                work(budget);

            }
            else {
                currentPlayer.addRehearsal();
                String playerRehersalPoints = Integer.toString(player.getRehersalPoints());
                dController.showMessage("You now have " + playerRehersalPoints + " rehearsal points. Bravo!!!");
                maintenance();
            }
        }
    }

    //-------------------------------------------------------
    // work
    //
    // player chose to Act - engages Banker for playout
    //-------------------------------------------------------
    private void work(int budget) {
        int diceRoll = Banker.rollDie();
        int rehearsalPoints = currentPlayer.getRehersalPoints();

        // get budget
        if (currentRoomObject.getCurrentScene() != null){
            budget = currentRoomObject.getCurrentScene().getBudget();
        }

        // convert dice roll and rehearsal points to String for dialogue
        String diceRollString = Integer.toString(diceRoll);
        String rehersalPointsString = Integer.toString(rehearsalPoints);
        String num = Integer.toString(currentPlayer.getPlayerNumber());

        // roll + rehearsal points successful
        if (diceRoll + rehearsalPoints >= budget) {
            dController.showMessage("You rolled a " + diceRollString + " plus " + rehersalPointsString + " rehearsal points = ROLE SUCCESS!!");

            // pay player
            Banker.rollSuccess(currentPlayer);

            // remove shot counter
            int sceneShots = currentRoomObject.getRemainingShotCounters();
            controller.removeShotCounter(currentRoomString, sceneShots);
            currentRoomObject.decrementShotCounter();
            int shotCountersRemaining = sceneShots - 1;

            // if last shot counter removed, trigger its A Wrap
            if (shotCountersRemaining  == 0) {
                boolean playerOnCard = false;
                ArrayList<Player> players = currentRoomObject.getPlayers();

                // check if there was any player on a card to see if players get bonus roll
                for (Player player1 : players) {
                    if (player1.isOnCard() && playerOnCard == false) {
                        playerOnCard = true;
                    }
                }

                if (playerOnCard == true) {
                    dController.showMessage("It's a Wrap! There was a player on the card! Bonus roll time!");
                    Banker.bonusRollPayout(players, currentRoomObject.getCurrentScene());
                }
                else {
                    dController.showMessage("It's a Wrap! Only on-card roles get bonus rolls, keep at it!");
                }

                // update players so no longer acting, not on card, rehearsal points are removed, and clearpart
                for (Player player1 : players) {
                    player1.setJustWrapped(true);
                    player1.actingNo();
                    player1.cardNo();
                    player1.setCurrentPart(null);
                    player1.clearPart();
                    player1.resetRehearsal();
                }

                // remove  scene card
                currentRoomObject.removeSceneCard();
                reduceSceneCount();
                removeCard();
            }
        }

        else {
            Banker.rollFail(currentPlayer);
            dController.showMessage("You rolled a " + diceRollString + " plus " + rehersalPointsString + " rehearsal points. You failed at acting. Better luck next time!");

        }
        maintenance();
    }

    //-------------------------------------------------------
    // roomChoice
    //
    // response from controller on what room player chose
    //-------------------------------------------------------
    public void roomChoice(String room) {
        // will contain buttons of next choices after moving into room
        ArrayList<String> playerOptionButtons = new ArrayList<>();

        // update globals  player's location to new room choice
        this.currentPlayer.setLocation(room);
        this.currentRoomString = room;
        this.currentRoomObject = this.board.getRoom(this.currentRoomString);

        // update room's arraylist of players occupying the room
        this.currentRoomObject.addPlayerToRoom(this.currentPlayer);

        // player moved to trailer, can only wait
        if(currentRoomString.equals("trailer")) {
            String waitRoom = waitLogic("trailer_");
            playerOptionButtons.add(waitRoom);
            controller.setButtonBorders(playerOptionButtons, true);
        }

        // player moved to office, can only wait and triggers upgrade
        else if(currentRoomString.equals("office")) {
            Banker.playerUpgrade(this.currentPlayer);
            String waitRoom = waitLogic("office_");
            playerOptionButtons.add(waitRoom);
            controller.setButtonBorders(playerOptionButtons, true);

        //    maintenance();
        }



        // player moved to set room
        else{

            // no card on board, player can only wait
            if (this.currentRoomObject.getCurrentScene() == null) {
                String waitRoom = waitLogic(room + "_");
                playerOptionButtons.add(waitRoom);
                controller.setButtonBorders(playerOptionButtons,  true);
                maintenance();
            }
            // determine buttons player can move on
            else {
                ArrayList<Part> offCardParts = this.currentRoomObject.getParts();
                ArrayList<Part> onCardParts = this.currentRoomObject.getCurrentScene().getParts();
                int numCardParts = onCardParts.size();

                // add all on card roles player could take
                for (int i = 0; i < onCardParts.size(); i++) {
                    if(!onCardParts.get(i).isTaken() && onCardParts.get(i).getLevel() <= this.currentPlayer.getRank()) {
                        playerOptionButtons.add("#" + Integer.toString(numCardParts) + Integer.toString(i + 1));
                    }
                }

                // add all off-card roles player could take
                for (int i = 0; i < offCardParts.size(); i++) {
                    if(!offCardParts.get(i).isTaken() && offCardParts.get(i).getLevel() <= this.currentPlayer.getRank()) {
                        playerOptionButtons.add(offCardParts.get(i).getName());
                    }
                }

                // add waiting spot player could move too
                if (!currentPlayer.isWaiting() && currentRoomObject.getNumPlayersWaiting() != 8) {
                    String waitRoom = waitLogic(room + "_");
                    playerOptionButtons.add(waitRoom);
                }
                controller.setButtonBorders(playerOptionButtons,  true);
            }
        }
    }

    //-------------------------------------------------------
    // partChoice
    //
    // response from controller on which part player chose
    // part: either part name of role if off-card
    // otherwise part will be room name, plus index of which card role they chose
    //-------------------------------------------------------
    public void partChoice(String part) {

        // player has taken part, set isActing
        this.currentPlayer.actingYes();
        this.currentPlayer.setWaiting(false);
        boolean offCard = false;

        // get all off-card parts
        ArrayList<Part> offCardParts = currentRoomObject.getParts();

        // check if part name equals off card part
        for (int i = 0; i < offCardParts.size(); i++) {
            if(offCardParts.get(i).getName().equals(part)) {

                // set player to part
                currentPlayer.setCurrentPart(offCardParts.get(i));
                offCardParts.get(i).takePart();
                offCard = true;
            }
        }

        // otherwise part was on-card role
        if (this.currentRoomObject.getCurrentScene() != null && offCard == false) {
            ArrayList<Part> onCardParts = currentRoomObject.getCurrentScene().getParts();

            //convert index from string to int
            int partLastCharVal = part.charAt(part.length() - 1) - '0';
            int partIndex = partLastCharVal - 1;

            // set player to part
            currentPlayer.setCurrentPart(onCardParts.get(partIndex));
            onCardParts.get(partIndex).takePart();
            currentPlayer.cardYes();

        }
        maintenance();
    }

    //-------------------------------------------------------
    // waitChoice
    //
    // gives turn options to players based on their status
    //-------------------------------------------------------
    public void waitChoice(String waitButton) {
        currentPlayer.setWaiting(true);
        this.currentRoomObject.setNumPlayersWaiting(currentRoomObject.getNumPlayersWaiting() + 1);

            maintenance();

    }

    //-------------------------------------------------------
    // waitLogic
    //
    // calculates index of wait block button controller should activate
    //-------------------------------------------------------
    private String waitLogic(String room) {
        // will contain buttons of next choices after moving into room
        String playerOptionButtons = "";

        // get number of players already waiting in the room
        int waitingIndex = this.currentRoomObject.getNumPlayersWaiting() + 1;

        // tell controller which waiting buttons to highlight
        playerOptionButtons = room + Integer.toString(waitingIndex);

        return playerOptionButtons;
    }
    

    //-------------------------------------------------------
    // createPlayerArrayList
    //
    // creates player objects and sets to arraylist
    //-------------------------------------------------------
    private ArrayList<Player> createPlayerArrayList(int numPlayers) {
        ArrayList<Player> playerList = new ArrayList<Player>();
        Room trailer = this.board.getRoom("trailer");

        // since all players are waiting, update trailers numPlayersWaiting count
        trailer.setNumPlayersWaiting(numPlayers);

        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(numPlayers, i+1);

            // set players current location to trailer room
            player.setLocation("trailer");


            // add player to arraylist
            playerList.add(player);

            // add player to trailer room
            trailer.addPlayerToRoom(player);
            if (i == 0) {
                player.setColor("BLUE");
            }
            else if (i == 1) {
                player.setColor("RED");
            }
            else if(i == 2) {
                player.setColor("ORANGE");
            }
            else if(i == 3) {
                player.setColor("CYAN");
            }
            else if (i == 4) {
                player.setColor("PINK");
            }
            else if (i == 5) {
                player.setColor("VIOLET");
            }
            else if (i == 6) {
                player.setColor("YELLOW");
            }
            else{
                player.setColor("GREEN");
            }
        }
        return playerList;
    }


    //-------------------------------------------------------
    // maintenance
    //
    // End of day and game over check performed at end of every players turn
    //-------------------------------------------------------
    public void maintenance() {


        // check number of scenes
        int numScenes = board.getNumScenes();

        // game is over
        if(numScenes == 1 && daysLeft == 0) {
            Banker.tallyPointsEndOfGame(playerList);
        }

        // new day
        else if (numScenes == 1 && daysLeft > 0){
            controller.resetPlayersTrailer(numPlayers);
            controller.resetShotCounters();
            this.daysLeft--;
            dController.displayDaysOnBoard(this.daysLeft);
            board.newDay(playerList);

        }
        // game is not over, continue playing
        playerTurn();
    }


    //-------------------------------------------------------
    // removeCard
    //
    // removes card after Its a Wrap!
    //-------------------------------------------------------
    private void removeCard() {
        String roomName = currentRoomString;
        String cardImgID = "";
        switch (roomName) {
            case "MainStreet":
                cardImgID = "#MS_Card";
                break;
            case "Saloon":
                cardImgID = "#S_Card";
                break;
            case "Bank":
                cardImgID = "#B_Card";
                break;
            case "Hotel":
                cardImgID = "#H_Card";
                break;
            case "Church":
                cardImgID = "#C_Card";
                break;
            case "Ranch":
                cardImgID = "#R_Card";
                break;
            case "SecretHideout":
                cardImgID = "#SH_Card";
                break;
            case "GeneralStore":
                cardImgID = "#GS_Card";
                break;
            case "Jail":
                cardImgID = "#J_Card";
                break;
            case "TrainStation":
                cardImgID = "#TS_Card";
                break;
        }
        controller.removeCard(cardImgID);
    }


    //-------------------------------------------------------
    // reduceSceneCount
    //
    // decrements number of scenes on board
    //-------------------------------------------------------
    public void reduceSceneCount() {
        board.sceneRemoved();
    }
}