package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller for the savings scene.
 * @author Victor Martinez
 */
public class SavingsController {
    @FXML
    private Button transactionButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button chartsButton;
    @FXML
    private ListView savingsList;
    @FXML
    private Label totalSavings;


    /**
     * The handler for the button clicks in the savings scene.
     * Exclusively sets other scenes. (Requirement 1.1.0)
     * @param event The event passed by the user.
     * @throws IOException
     */
    @FXML
    public void onButtonClicked(ActionEvent event) throws IOException {
        if(event.getSource().equals(transactionButton)) {
            Stage theStage = (Stage) transactionButton.getScene().getWindow();
            BorderPane pane = FXMLLoader.load(getClass().getResource("transactions.fxml"));
            theStage.setScene(new Scene(pane, 1024, 768));
        } else if(event.getSource().equals(homeButton)) {
            Stage theStage = (Stage) transactionButton.getScene().getWindow();
            BorderPane pane = FXMLLoader.load(getClass().getResource("home.fxml"));
            theStage.setScene(new Scene(pane, 800, 500));
        } else if(event.getSource().equals(chartsButton)) {
            Stage theStage = (Stage) transactionButton.getScene().getWindow();
            BorderPane pane = FXMLLoader.load(getClass().getResource("charts.fxml"));
            theStage.setScene(new Scene(pane, 1024, 768));
        }
    }

    /**
     * Sets the ListView to all of the transactions of category "Savings"
     * as well as the label for the total savings amount.
     */
    @FXML
    public void initialize() {
        savingsList.setItems(BudgetTracker.getInstance().getSavingTransactions());
        totalSavings.setText("$" + String.format("%.2f", BudgetTracker.getInstance().getCategoryAmount(BudgetTracker.getInstance().getTransactions(),"Savings")));
    }
}
