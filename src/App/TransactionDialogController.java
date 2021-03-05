package App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

/**
 * The controller for the dialog pane when adding a transaction.
 * @author Victor Martinez
 */
public class TransactionDialogController {
    @FXML
    private ChoiceBox categoryField;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker dateField;
    @FXML
    private ToggleGroup transactionType;
    @FXML
    private ChoiceBox subCategoryField;

    /**
     * Creates and returns a new transaction based on user input.
     * (Requirement 2.0.0, 2.1.0, 2.2.0)
     * @return The transaction to be added.
     */
    public Transaction processResult() {
        double amount = Double.parseDouble(amountField.getText().trim());
        String category = categoryField.getValue().toString();
        String subCategory = subCategoryField.getValue().toString();
        LocalDate date = dateField.getValue();
        RadioButton selected = (RadioButton) transactionType.getSelectedToggle();
        String typeSelected = selected.getText();


        Transaction newTransaction;
        if(typeSelected.equals("Deposit")) {
            newTransaction = new Deposit(amount, category, subCategory , date);
        } else{
            newTransaction = new Expense(amount, category, subCategory , date);
        }
        BudgetTracker.getInstance().addTransaction(newTransaction);
        return newTransaction;

    }

    /**
     * Sets the appropriate subcategory choicebox options when the category choicebox
     * field is changed.
     */
    @FXML
    public void onChange() {
        String categoryText = categoryField.getValue().toString();
        ObservableList<String> subCategoryList = FXCollections.observableArrayList(
                BudgetTracker.getInstance().categoryMap.get(categoryText)
        );
        subCategoryField.setItems(subCategoryList);
    }

    /**
     * The initialization method called every time this scene is set.
     * Sets the category choicebox options and selects the first category
     * in list then selects the first subcategory in the subcategory options.
     */
    @FXML
    public void initialize() {
        ObservableList<String> categoryList = FXCollections.observableArrayList(
                BudgetTracker.getInstance().categoryMap.keySet()
        );
        categoryField.setItems(categoryList);
        categoryField.getSelectionModel().select(0);
        subCategoryField.getSelectionModel().select(0);
        dateField.setValue(LocalDate.now());
    }
}
