package App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The controller for the charts scene.
 * @author Victor Martinez
 */
public class ChartsController {
    @FXML
    private PieChart pieChart;
    @FXML
    private Button transactionButton;
    @FXML
    private Button homeButton;
    @FXML
    private ToggleGroup rangeToggleCategory;
    @FXML
    private Button savingsButton;
    @FXML
    private ToggleGroup rangeToggle;
    @FXML
    private ChoiceBox categoryField;

    /**
     * The handler for button clicks in the charts scene.
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
        } else if(event.getSource().equals(savingsButton)) {
            Stage theStage = (Stage) transactionButton.getScene().getWindow();
            BorderPane pane = FXMLLoader.load(getClass().getResource("savings.fxml"));
            theStage.setScene(new Scene(pane, 1024, 768));
        }
    }

    /**
     * Handles the generation of chart data depending on user input. Generates
     * chart of all transactions by user requested timeframe(month, year, or all).
     */
    @FXML
    public void handleChartGeneration() {
        //if no range selected do nothing
        if(rangeToggle.getSelectedToggle() == null) {
            return;
        }
        RadioButton selectedRange = (RadioButton) rangeToggle.getSelectedToggle();//the user selected toggle
        String rangeSelected = selectedRange.getText();//the String for the selected toggle

        //requests and sets chart by timeframe depending on user choice
        switch(rangeSelected) {
            case "This Month":
                pieChart.setData(BudgetTracker.getInstance().getMonthPieChartData());
                pieChart.setTitle("Spending " + rangeSelected);

                break;
            case "This Year":
                pieChart.setData(BudgetTracker.getInstance().getYearPieChartData());
                pieChart.setTitle("Spending " + rangeSelected);
                break;
            case "All Expenses":
                pieChart.setData(BudgetTracker.getInstance().getPieChartData());
                pieChart.setTitle(rangeSelected);
                break;
            default:
                System.out.println("Unexpected Result.");
        }
        setToolTips();
    }

    /**
     * Handles the generation of chart data depending on user input. Generates
     * chart of subcategory data depending on timeframe and subcategory chosen.
     * (Requirement 2.1.2)
     */
    @FXML
    public void handleSubCategoryChart() {
        //if no range selected do nothing
        if(rangeToggleCategory.getSelectedToggle() == null) {
            return;
        }
        RadioButton selectedRange = (RadioButton) rangeToggleCategory.getSelectedToggle();//the user selected toggle
        String selectedRangeText = selectedRange.getText();//String for selected toggle
        String categoryName = categoryField.getValue().toString();//category chosen

        //requests and sets chart by selected timeframe and category
        switch(selectedRangeText) {
            case "This Month":
                pieChart.setData(BudgetTracker.getInstance().getMonthPieChartData(categoryName));
                pieChart.setTitle("Spending in " + categoryName + " " + selectedRangeText);
                break;
            case "This Year":
                pieChart.setData(BudgetTracker.getInstance().getYearPieChartData(categoryName));
                pieChart.setTitle("Spending in " + categoryName + " " + selectedRangeText);
                break;
            case "All Expenses":
                pieChart.setData(BudgetTracker.getInstance().getPieChartData(categoryName));
                pieChart.setTitle("Spending in " + categoryName + " for " + selectedRangeText);
                break;
            default:
                System.out.println("Something weird happened.");
        }
        setToolTips();
    }

    /**
     * The initialization method called every time this scene is set.
     * Initializes default chart of all transactions as well as the
     * choiceboxes for category and subcategory.
     */
    @FXML
    public void initialize() {

        pieChart.setTitle("Spending Breakdown");
        pieChart.setData(BudgetTracker.getInstance().getPieChartData());
        setToolTips();

        ObservableList<String> categoryList = FXCollections.observableArrayList(
                BudgetTracker.getInstance().categoryMap.keySet()
        );
        categoryField.setItems(categoryList);
        categoryField.getSelectionModel().select(0);
    }

    /**
     * Sets tooltips for chart.
     */
    @FXML
    public void setToolTips() {
        pieChart.getData().forEach(data -> {
            String value = "$" + String.format("%.2f", data.getPieValue());
            Tooltip tooltip = new Tooltip(value);
            Tooltip.install(data.getNode(), tooltip);
        });
    }
}
