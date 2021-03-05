package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * The controller for the home scene.
 * (Requirement 1.0.0)
 * @author Victor Martinez
 */
public class HomeController {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button transactionButton;
    @FXML
    private Button chartsButton;
    @FXML
    private Button savingsButton;
    @FXML
    private Label cashAmount;
    @FXML
    private Label savingsAmount;
    @FXML
    private Label creditDebtAmount;
    @FXML
    private ListView recentTransactionList;

    /**
     * The initialization method called every time this scene is set.
     * Sets all of the labels to the appropriate values as well as the
     * recentTransactionList ListView.
     */
    @FXML
    public void initialize() {
        cashAmount.setText("$" + String.format("%.2f", BudgetTracker.getInstance().getSubCategoryAmount(BudgetTracker.getInstance().getTransactions(),"Cash")));
        savingsAmount.setText("$" + String.format("%.2f", BudgetTracker.getInstance().getCategoryAmount(BudgetTracker.getInstance().getTransactions(),"Savings")));
        creditDebtAmount.setText("$" + String.format("%.2f", BudgetTracker.getInstance().getSubCategoryAmount(BudgetTracker.getInstance().getTransactions(),"Credit Cards")));
        recentTransactionList.getItems().setAll(BudgetTracker.getInstance().getRecentTransactions());
    }

    /**
     * The handler for all of the button clicks in the sidebar of the scene.
     * (Requirement 1.0.0, 1.1.0)
     * @param event The event passed by the user.
     * @throws IOException
     */
    @FXML
    public void onButtonClicked(ActionEvent event) throws IOException {
        if(event.getSource().equals(transactionButton)) {
            Stage theStage = (Stage) transactionButton.getScene().getWindow();
            BorderPane pane = FXMLLoader.load(getClass().getResource("transactions.fxml"));
            theStage.setScene(new Scene(pane, 1024, 768));
        } else if(event.getSource().equals(chartsButton)) {
            Stage theStage = (Stage) transactionButton.getScene().getWindow();
            BorderPane pane = FXMLLoader.load(getClass().getResource("charts.fxml"));
            theStage.setScene(new Scene(pane, 1024, 768));
        } else if(event.getSource().equals(savingsButton)) {
            Stage theStage = (Stage) transactionButton.getScene().getWindow();
            BorderPane pane = FXMLLoader.load(getClass().getResource("savings.fxml"));
            theStage.setScene(new Scene(pane, 1024, 768));
        }
    }

    /**
     * The handler for when the user clicks the "Add Transaction" button.
     * Same handler as in the TransactionsController. Creates and shows a dialog
     * pane for the adding of transactions.
     * (Requirement 2.1.0, 2.1.0, 2.2.0)
     */
    @FXML
    public void showAddDialogue() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.setTitle("Add or Edit a Transaction");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addTransactionDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Couldn't load.");
            e.printStackTrace();
            return;
        }


        dialog.getDialogPane().setPrefWidth(680);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);


        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            TransactionDialogController controller = fxmlLoader.getController();
            controller.processResult();
            initialize();
        }
    }
}
