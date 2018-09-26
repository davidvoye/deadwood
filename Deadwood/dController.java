package Deadwood;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;


public class dController {

    BarWench barwench;

    public void setBarWench(BarWench barwench) {
        this.barwench = barwench;
    }

    @FXML
    public  ImageView player1, player2, player3, player4, player5, player6, player7, player8;
    private ImageView currentPlayer;
    private ImageView previousPlayer;
    private ArrayList<String> deactivateNodes;


    //-------------------------------------------------------
    // handleRoomButton
    //
    //
    //-------------------------------------------------------
    @FXML
    public void handleRoomButton(Event event){
        Button butt = (Button)event.getSource();
        setButtonBorders(deactivateNodes, false);
        if (barwench.getBoard().getRoom(butt.getId()) != null) {
            flipCard(barwench.getBoard().getRoom(butt.getId()));
        }

        if (butt.getId() == null) {
            if(barwench.getCurrentRoom().equals("Church")) {
                barwench.roomChoice("Church");
            }
        }
        else {
            barwench.roomChoice(butt.getId());
        }
    }


    //-------------------------------------------------------
    // handlepartButton
    //
    //
    //-------------------------------------------------------
    @FXML
    public void handlepartButton(Event event){
        Button butt = (Button)event.getSource();
        Bounds boundsInScene = butt.localToScene(butt.getBoundsInLocal());
        currentPlayer.setFitHeight(45);
        currentPlayer.setFitWidth(44);
        currentPlayer.setLayoutX(boundsInScene.getMinX());
        currentPlayer.setLayoutY(boundsInScene.getMinY());
        setButtonBorders(deactivateNodes, false);
        barwench.partChoice(butt.getId());
    }


    //-------------------------------------------------------
    // handleWaitButton
    //
    //
    //-------------------------------------------------------
    @FXML
    public void handleWaitButton(Event event){
        Button butt = (Button)event.getSource();
        Bounds boundsInScene = butt.localToScene(butt.getBoundsInLocal());
        currentPlayer.setFitHeight(26);
        currentPlayer.setFitWidth(27);
        currentPlayer.setLayoutX(boundsInScene.getMinX());
        currentPlayer.setLayoutY(boundsInScene.getMinY());
        setButtonBorders(deactivateNodes, false);
        barwench.waitChoice(butt.getId());
    }


    //-------------------------------------------------------
    // displayPlayerStats
    //
    //
    //-------------------------------------------------------
    @FXML
    public void displayPlayerStats(){
        ArrayList<Player> players = barwench.getPlayers();
        String color;
        int dollars;
        int credits;
        int rehearsalPoints;

        String stats = "PLAYER STATS\n\n";
        for (int i = 0; i < players.size(); i++) {
            color = players.get(i).getColor();
            Integer.toString(dollars = players.get(i).getDollars());
            Integer.toString(credits = players.get(i).getCredits());
            Integer.toString(rehearsalPoints = players.get(i).getRehersalPoints());

            stats = stats + "PLAYER " + color + "\nDollars:\t" + dollars + "\ncredits:\t" + credits + "\nrehearsal points: " + rehearsalPoints + "\n\n";
        }
        showMessage(stats);
    }

    //-------------------------------------------------------
    // setCurrentPlayer
    //
    //
    //-------------------------------------------------------
    public void setCurrentPlayer(int playerID){
    if (currentPlayer != null) {
      this.previousPlayer = currentPlayer;
      }
        switch (playerID) {
            case 1:
                this.currentPlayer = player1;
                break;
            case 2:
                this.currentPlayer = player2;
                break;
            case 3:
                this.currentPlayer = player3;
                break;
            case 4:
                this.currentPlayer = player4;
                break;
            case 5:
                this.currentPlayer = player5;
                break;
            case 6:
                this.currentPlayer = player6;
                break;
            case 7:
                this.currentPlayer = player7;
                break;
            case 8:
                this.currentPlayer = player8;
                break;
        }
        highlightCurrentPlayer();
    }


    //-------------------------------------------------------
    // displayDaysOnBoard
    //
    //
    //-------------------------------------------------------
    @FXML
    public static void displayDaysOnBoard(int daysLeft) {
        Scene scene = Deadwood.scene;
        Label dayLabel = (Label) scene.lookup("#T_DayLabel");
        dayLabel.setText(daysLeft + " days left");
    }


    //-------------------------------------------------------
    // highlightCurrentPlayer
    //
    //
    //-------------------------------------------------------
    @FXML
    public void highlightCurrentPlayer(){
      DropShadow borderGlow = new DropShadow();
      borderGlow.setColor(Color.valueOf("#00ff00"));
      borderGlow.setOffsetX(0f);
      borderGlow.setOffsetY(0f);
      borderGlow.setHeight(150);
      borderGlow.setWidth(150);
      currentPlayer.setEffect(borderGlow);
      if(previousPlayer != null) {
          previousPlayer.setEffect(null);
      }
    }


    //-------------------------------------------------------
    // removeShotCounter
    //
    //
    //-------------------------------------------------------
    public static void removeShotCounter(String roomName, int shotNum){
        Scene scene = Deadwood.scene;
        ImageView shotCounter = (ImageView)scene.lookup("#" + roomName + "_C" + shotNum);
        shotCounter.setVisible(false);
    }


    //-------------------------------------------------------
    // resetShotCounters
    //
    //
    //-------------------------------------------------------
    public void resetShotCounters(){
        ArrayList<Room> rooms = barwench.getBoard().getRooms();
        Scene scene = Deadwood.scene;
        for(Room room : rooms){
            if(!room.getRoomName().equals("office") && !room.getRoomName().equals("trailer")) {
                int numShots = room.getOrigShotCount();
                for (int i = 0; i < numShots; i++) {
                    ImageView shotCounter = (ImageView) scene.lookup("#" + room.getRoomName() + "_C" + Integer.toString(i + 1));
                    shotCounter.setVisible(true);
                }
            }
        }

    }


    //-------------------------------------------------------
    // choiceDialogue
    //
    // Offer choices of upgrade ranks available to user, return int to banker
    //-------------------------------------------------------
    public static int choiceDialogue(ArrayList<Integer> choices){
      ChoiceDialog<Integer> dialog = new ChoiceDialog("", choices);
      dialog.setHeaderText("Enter Upgrade Rank");
      int desiredRank = dialog.showAndWait().get();
      return desiredRank;
    }


    //-------------------------------------------------------
    // dollarsCreditsDiolog
    //
    //
    //-------------------------------------------------------
    public static String dollarsCreditsDiolog(){
      ArrayList <String> currencyType = new ArrayList<String>();
      currencyType.add("Dollars");
      currencyType.add("Credits");
      ChoiceDialog<String> dialog = new ChoiceDialog("Dollars",currencyType);
      dialog.setHeaderText("Would you like to use your dollars or credits for rank upgrade?");
      String dollarsOrCredits = dialog.showAndWait().get();
      return dollarsOrCredits;
    }


    //-------------------------------------------------------
    // showMessage
    //
    //
    //-------------------------------------------------------
    public static void showMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    //-------------------------------------------------------
    // removeCard
    //
    //
    //-------------------------------------------------------
    public void removeCard(String cardID){
        Scene scene = Deadwood.scene;
        Node tempButt = scene.lookup(cardID);
        tempButt.setVisible(false);
    }


    //-------------------------------------------------------
    // workOrRehearse
    //
    // Get choice of work or rehearse from user
    //-------------------------------------------------------
    public int workOrRehearse(boolean canRehearse){

        ArrayList<String> choices = new ArrayList<>();
        choices.add("Act");
        if(canRehearse) {
            choices.add("Rehearse");
        }
        ChoiceDialog<String> dialog = new ChoiceDialog("Act", choices);
        dialog.setHeaderText("Choose what you want to do next:");

        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
        String input = dialog.showAndWait().get();
        if(input.equals("Act")){
            return 0;
        }else{
            return 1;
        }
    }


    //-------------------------------------------------------
    // flipCard
    //
    //
    //-------------------------------------------------------
    public static void flipCard(Room room){
        if(room.getCurrentScene() != null) {
            Scene scene = Deadwood.scene;
            String roomname = room.getRoomName();
            String cardImgID = null;
            if (!roomname.equals("office") && !roomname.equals("trailer")) {
                String imgStr = "/Deadwood/cards/" + room.getCurrentScene().getImage();
                switch (roomname) {
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
                Image tmpImage = new Image(imgStr);
                ImageView imageView = (ImageView) scene.lookup(cardImgID);
                imageView.setImage(tmpImage);

                //enable current pane, disable other two
                Pane oneRole;
                Pane twoRole;
                Pane threeRole;
                int numParts = room.getCurrentScene().getParts().size();
                String roomLetters = roomname.substring(0,2);
                switch (numParts) {
                    case 1:
                        oneRole = (Pane) scene.lookup("#" + roomLetters + "_OneRole");
                        oneRole.setVisible(true);
                        oneRole.toFront();
                        twoRole = (Pane) scene.lookup("#" + roomLetters + "_TwoRole");
                        twoRole.setVisible(false);
                        threeRole = (Pane) scene.lookup("#" + roomLetters + "_ThreeRole");
                        threeRole.setVisible(false);
                        break;
                    case 2:
                        oneRole = (Pane) scene.lookup("#" + roomLetters + "_OneRole");
                        oneRole.setVisible(false);
                        twoRole = (Pane) scene.lookup("#" + roomLetters + "_TwoRole");
                        twoRole.setVisible(true);
                        twoRole.toFront();
                        threeRole = (Pane) scene.lookup("#" + roomLetters + "_ThreeRole");
                        threeRole.setVisible(false);
                        break;
                    case 3:
                        oneRole = (Pane) scene.lookup("#" + roomLetters + "_OneRole");
                        oneRole.setVisible(false);
                        twoRole = (Pane) scene.lookup("#" + roomLetters + "_TwoRole");
                        twoRole.setVisible(false);
                        threeRole = (Pane) scene.lookup("#" + roomLetters + "_ThreeRole");
                        threeRole.setVisible(true);
                        threeRole.toFront();
                        break;
                }
            }
        }
    }


    //-------------------------------------------------------
    // addCards
    //
    // Populate all rooms with new cards, used at beginning of new day
    //-------------------------------------------------------
    public static void addCards(ArrayList<Room> rooms){
        Scene scene = Deadwood.scene;
        Image tmpImage = new Image("/Deadwood/cards/back.jpg");
        String cardImgID = null;
        for(Room room : rooms) {
            String roomname = room.getRoomName();
            if (!roomname.equals("office") && !roomname.equals("trailer")) {
                switch (roomname) {
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
                ImageView imageView = (ImageView) scene.lookup(cardImgID);
                imageView.setImage(tmpImage);


            }
        }
    }

    //-------------------------------------------------------
    // resetCardVisiblity
    //
    //
    //-------------------------------------------------------
    public static void resetCardVisiblity(ArrayList<Room> rooms){
        Scene scene = Deadwood.scene;
        for(Room room : rooms){
            String roomName = room.getRoomName();
            String cardImgID = null;
            if(room.isSetRoom() && room.getCurrentScene() == null){
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
                ImageView imageView = (ImageView) scene.lookup(cardImgID);
                imageView.setVisible(true);
            }
        }

    }



    //-------------------------------------------------------
    // setButtonBorders
    //
    // Highlight valid moves with green highlight
    //-------------------------------------------------------
    //array of button ids, boolean: true to enable visibility, false to disable visibility
    public void setButtonBorders(ArrayList<String> buttons, boolean enable){
        deactivateNodes = buttons;
        Scene scene = Deadwood.scene;
        String buttonID;
        for(String button : buttons){

            if(button.charAt(0) == '#'){
                buttonID = "#" + barwench.getCurrentRoom() + Character.toString(button.charAt(1)) + Character.toString(button.charAt(2));
            }else{
                buttonID = "#" + button;
            }
            Node tempButton = scene.lookup(buttonID);
            if(tempButton == null) {
                System.out.println("wtf");
            }
            tempButton.setVisible(enable);
        }
    }

    //-------------------------------------------------------
    // upgradeImage
    //
    // Change player image/die according to upgraded rank
    //-------------------------------------------------------
    public static void upgradeImage(int playerNum, int rank){
      String imagePath = null;
      Scene scene = Deadwood.scene;
      String playerID = null;
      switch (playerNum) {
        case 1:
        imagePath = "/Deadwood/dice/" + "b" + Integer.toString(rank) + ".png";
        playerID = "#player1";
        break;
        case 2:
        imagePath = "/Deadwood/dice/" + "r" + Integer.toString(rank) + ".png";
        playerID = "#player2";
        break;
        case 3:
        imagePath = "/Deadwood/dice/" + "o" + Integer.toString(rank) + ".png";
        playerID = "#player3";
        break;
        case 4:
        imagePath = "/Deadwood/dice/" + "c" + Integer.toString(rank) + ".png";
        playerID = "#player4";
        break;
        case 5:
        imagePath = "/Deadwood/dice/" + "p" + Integer.toString(rank) + ".png";
        playerID = "#player5";
        break;
        case 6:
        imagePath = "/Deadwood/dice/" + "v" + Integer.toString(rank) + ".png";
        playerID = "#player6";
        break;
        case 7:
        imagePath = "/Deadwood/dice/" + "y" + Integer.toString(rank) + ".png";
        playerID = "#player7";
        break;
        case 8:
        imagePath = "/Deadwood/dice/" + "g" + Integer.toString(rank) + ".png";
        playerID = "#player8";
        break;
      }
      Image tempImage = new Image(imagePath);
      ImageView image = (ImageView)scene.lookup(playerID);
      image.setImage(tempImage);
    }



    //-------------------------------------------------------
    // resetPlayersTrailer
    //
    // Move all player images to trailer room at beginning of new day
    //-------------------------------------------------------
    public void resetPlayersTrailer(int numPlayers){
        Scene scene = Deadwood.scene;
        for(int i = 1; i <= numPlayers; i++){
            String imageViewID = "#player" + i;
            ImageView player = (ImageView)scene.lookup(imageViewID);
            int Xcoord = 0;
            int Ycoord = 0;
            switch(i){
                case 1: Xcoord = 1108;
                        Ycoord = 357;
                        break;
                case 2: Xcoord = 1135;
                        Ycoord = 357;
                    break;
                case 3: Xcoord = 1161;
                        Ycoord = 357;
                    break;
                case 4: Xcoord = 1188;
                        Ycoord = 357;
                    break;
                case 5: Xcoord = 1108;
                        Ycoord = 385;
                    break;
                case 6: Xcoord = 1135;
                        Ycoord = 385;
                    break;
                case 7: Xcoord =  1161;
                        Ycoord = 385;
                    break;
                case 8: Xcoord = 1188;
                        Ycoord = 385;
                    break;
            }
            player.setLayoutX(Xcoord);
            player.setLayoutY(Ycoord);
            player.setFitHeight(26);
            player.setFitWidth(27);
        }
    }



    //-------------------------------------------------------
    // setVisiblePlayer
    //
    // Beginning of game, set number of visible players
    //-------------------------------------------------------
    public void setVisiblePlayer(int numPlayers){

        switch (numPlayers){
            case 2: player1.setVisible(true);
                    player2.setVisible(true);
                    break;
            case 3: player1.setVisible(true);
                    player1.setVisible(true);
                    player2.setVisible(true);
                    player3.setVisible(true);
                    break;
            case 4: player1.setVisible(true);
                    player1.setVisible(true);
                    player2.setVisible(true);
                    player3.setVisible(true);
                    player4.setVisible(true);
                    break;
            case 5: player1.setVisible(true);
                    player1.setVisible(true);
                    player2.setVisible(true);
                    player3.setVisible(true);
                    player4.setVisible(true);
                    player5.setVisible(true);
                    break;
            case 6: player1.setVisible(true);
                    player1.setVisible(true);
                    player2.setVisible(true);
                    player3.setVisible(true);
                    player4.setVisible(true);
                    player5.setVisible(true);
                    player6.setVisible(true);
                    break;
            case 7: player1.setVisible(true);
                    player1.setVisible(true);
                    player2.setVisible(true);
                    player3.setVisible(true);
                    player4.setVisible(true);
                    player5.setVisible(true);
                    player6.setVisible(true);
                    player7.setVisible(true);
                    break;
            case 8: player1.setVisible(true);
                    player1.setVisible(true);
                    player2.setVisible(true);
                    player3.setVisible(true);
                    player4.setVisible(true);
                    player5.setVisible(true);
                    player6.setVisible(true);
                    player7.setVisible(true);
                    player8.setVisible(true);
                    break;
            default: System.out.println("Error setting visible players - exiting...");
                     System.exit(1);
        }
    }
}
