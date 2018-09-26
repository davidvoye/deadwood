package Deadwood;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Deadwood extends Application {

    public static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("DeadwoodUI.fxml"));
        Parent root = loader.load();
        dController controller = loader.getController();
        primaryStage.setTitle("Deadwood");
        scene = new Scene(root);
        scene.getStylesheets().add("/Deadwood/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        ArrayList<Integer> choices = new ArrayList<>();
        choices.add(2);
        choices.add(3);
        choices.add(4);
        choices.add(5);
        choices.add(6);
        choices.add(7);
        choices.add(8);
        ChoiceDialog<Integer> dialog = new ChoiceDialog(2, choices);
        dialog.setHeaderText("Enter Number of Players");
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
        int numPlayers = dialog.showAndWait().get();
        controller.setVisiblePlayer(numPlayers);
        BarWench barwench = new BarWench(numPlayers, controller); //creates bar wench object, needed to do to get around static issue
        controller.setBarWench(barwench);
        barwench.gameStart(numPlayers);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
