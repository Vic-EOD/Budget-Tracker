package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    /**
     * Sets the main window and scene.
     * @param primaryStage The main stage at startup.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        primaryStage.setTitle("BudgetTrackerFX");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    /**
     * Stores transactions in file.
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        try {
            BudgetTracker.getInstance().storeTransactions();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initializes category map and loads transactions.
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        try {
            BudgetTracker.getInstance().loadCategoryMap();
            BudgetTracker.getInstance().loadTransactions();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
