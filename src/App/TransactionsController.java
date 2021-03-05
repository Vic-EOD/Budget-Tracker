package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * The controller for the transactions scene.
 * @author Victor Martinez
 */
public class TransactionsController {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button transactionButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button chartsButton;
    @FXML
    private Button savingsButton;
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private Button deleteTransactionButton;

    /**
     * The initialization method called every time this scene is set.
     * Disables the delete transaction button and sets the table of transactions.
     */
    @FXML
    public void initialize() {
        deleteTransactionButton.setDisable(true);
        setTable();
    }

    /**
     * The handler for the button clicks in the savings scene.
     * Exclusively sets other scenes. (Requirement 1.1.0)
     * @param event The event passed by the user.
     * @throws IOException
     */
    @FXML
    public void onButtonClicked(ActionEvent event) throws IOException {
        if(event.getSource().equals(homeButton)) {
            Stage theStage = (Stage) transactionButton.getScene().getWindow();
            BorderPane pane = FXMLLoader.load(getClass().getResource("home.fxml"));
            theStage.setScene(new Scene(pane, 800, 500));
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
     * The handler for when the "Delete Transaction" button is clicked.
     * Creates a confirmation alert that asks the user to confirm the deletion.
     * Deletes the transaction if user confirms. Does nothing if the user exits
     * or clicks the cancel button.
     */
    @FXML
    public void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the transaction?");
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() & result.get() == ButtonType.OK) {
            transactionTable.getItems().remove(selected);
            BudgetTracker.getInstance().getTransactions().remove(selected);
        }
    }

    /**
     * The handler for when the user clicks the "Add Transaction" button.
     * Creates and shows a dialog pane for the adding of transactions. User
     * will enter amount of transaction and then enter date, category, subcategory,
     * and type of transaction. Transaction will then be added to view and transactions
     * list in the BudgetTracker instance.
     * (Requirement 2.1.0, 2.1.1, 2.2.0)
     */
    @FXML
    public void showAddEditDialog() {
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
            Transaction newItem = controller.processResult();
            transactionTable.getSelectionModel().select(newItem);
        }
    }

    /**
     * Sets the TableView with data from the BudgetTracker instance in four columns
     * showing the Type, Amount, Category, and Date of the transaction. Shows all transactions.
     * (Requirement 2.0.0, 2.3.0)
     */
    @FXML
    public void setTable() {

        TableColumn<Transaction, String> column0 = new TableColumn<>("Type");
        column0.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, String> column1 = new TableColumn<>("Amount");
        column1.setCellValueFactory(new PropertyValueFactory<>("formattedValue"));

        TableColumn<Transaction, String> column2 = new TableColumn<>("Category");
        column2.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Transaction, String> column3 = new TableColumn<>("Date");
        column3.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));

        transactionTable.getColumns().add(column0);
        transactionTable.getColumns().add(column1);
        transactionTable.getColumns().add(column2);
        transactionTable.getColumns().add(column3);
        transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        transactionTable.setItems(BudgetTracker.getInstance().getTransactions());

        transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Handler for when a transaction in the TableView is selected.
     * Enables the "Delete Transaction" button when a transaction is selected.
     */
    @FXML
    public void handleClickTransactionItem() {
        deleteTransactionButton.setDisable(false);
    }
}
