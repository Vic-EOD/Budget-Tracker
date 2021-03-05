package App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Singleton Class for application.
 * @author Victor Martinez
 */
public class BudgetTracker {
    //The sole instance of the class.
    private static BudgetTracker instance = new BudgetTracker();

    final private static String transactionFileName = "transactions.txt";

    //Map for categories and subcategories. (Requirement 2.1.2)
    public HashMap<String, String[]> categoryMap = new HashMap<>();

    //List of transactions in class. Set as ObservableList for convenience.
    //(Requirement 2.1.1)
    private ObservableList<Transaction> transactions;

    /**
     * Returns the instance of the class.
     * @return The BudgetTracker instance.
     */
    public static BudgetTracker getInstance() {
        return instance;
    }


    /**
     * Private constructor so no other BudgetTracker classes can
     * be instantiated outside the class.
     */
    private BudgetTracker() {

    }

    /**
     * Returns an ObservableList of the transactions in the instance.
     * @return Attribute transactions, an ObservableList.
     */
    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Adds a transaction to the list of transactions.
     * (Requirement 2.1.1, 2.2.0)
     * @param newTransaction The transaction to be added.
     */
    public void addTransaction(Transaction newTransaction) {
        transactions.add(newTransaction);
    }

    /**
     * Loads transactions from text file.
     * (Requirement 2.1.1)
     * @throws IOException If text file does not exist.
     */
    public void loadTransactions() throws IOException {
        transactions = FXCollections.observableArrayList();
        Path path = Paths.get(transactionFileName);
        BufferedReader reader = Files.newBufferedReader(path);


        String input;

        try {
            while((input = reader.readLine()) != null) {
                String[] transactionPieces = input.split("\t");

                double amount = Double.parseDouble(transactionPieces[0]);
                String category = transactionPieces[1];
                String subCategory = transactionPieces[2];
                LocalDate date = LocalDate.parse(transactionPieces[3], DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

                if(amount < 0) {
                    Transaction transaction = new Expense(-amount, category, subCategory, date);
                    transactions.add(transaction);
                } else {
                    Transaction transaction = new Deposit(amount, category, subCategory, date);
                    transactions.add(transaction);
                }


            }
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Loads categories and subcategories to map.
     * (Requirement 2.1.2)
     */
    public void loadCategoryMap() {
        categoryMap.put("Housing", new String[]{"Mortgage", "Property Taxes", "Repairs", "HOA Fees"});
        categoryMap.put("Transportation", new String[]{"Car Payment", "Gas", "Maintenance", "Repairs", "Registration"});
        categoryMap.put("Food", new String[]{"Groceries", "Restaurants"});
        categoryMap.put("Utilities", new String[]{"Electricity", "Water", "Garbage", "Phone", "Internet", "Cable"});
        categoryMap.put("Clothing", new String[]{"Clothing", "Shoes"});
        categoryMap.put("Medical", new String[]{"Primary Care", "Dental Care", "Specialty Care", "Urgent Care",
                        "Medication", "Medical Devices"});
        categoryMap.put("Household", new String[]{"Toiletries", "Laundry Supplies", "Cleaning Supplies", "Tools"});
        categoryMap.put("Personal", new String[]{"Gym", "Grooming", "Cosmetics"});
        categoryMap.put("Debt", new String[]{"Personal Loans", "Student Loans", "Credit Cards"});
        categoryMap.put("Investments", new String[]{"Retirement", "Investment"});
        categoryMap.put("Savings", new String[]{"Emergency Fund", "Big Purchases", "Other Savings"});
        categoryMap.put("Entertainment", new String[]{"Alcohol/Bars", "Games", "Movies", "Concerts",
                        "Vacations", "Subscriptions"});
        categoryMap.put("Deposit/Payment", new String[]{"Cash", "Credit Card", "Student Loans",
                        "Personal Loans"});
    }

    /**
     *
     * @throws IOException If file is un-writeable.
     */
    public void storeTransactions() throws IOException {
        Path path = Paths.get(transactionFileName);
        path.toFile().setWritable(true);
        BufferedWriter writer = Files.newBufferedWriter(path);

        try {
            Iterator<Transaction> iterator = transactions.iterator();
            while(iterator.hasNext()) {
                Transaction transaction = iterator.next();
                writer.write(String.format("%s\t%s\t%s\t%s",
                        transaction.getAmount(),
                        transaction.getCategory(),
                        transaction.getSubCategory(),
                        transaction.getFormattedDate()));
                writer.newLine();
            }
        } finally {
            if(writer != null) {
                writer.close();
                path.toFile().setReadOnly();
                Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
            }
        }
    }

    /**
     * Returns up to 5 recently added transactions.
     * @return A List of transactions.
     */
    public List<Transaction> getRecentTransactions() {
        return transactions.subList(Math.max(transactions.size() - 5, 0), transactions.size());
    }

    /**
     * Returns all transactions in category "Savings".
     * @return The list of savings transactions.
     */
    public ObservableList<Transaction> getSavingTransactions() {
        ObservableList<Transaction> theList = FXCollections.observableArrayList();
        for(Transaction transaction : transactions) {
            if(transaction.getCategory().equals("Savings")) {
                theList.add(transaction);
            }
        }
        return theList;
    }

    /**
     * Returns all transactions in month.
     * @param date The date for the month requested.
     * @return The list of transactions in requested month.
     */
    public ObservableList<Transaction> getTransactionsInMonth(LocalDate date) {
        ObservableList<Transaction> transactionsInRange = FXCollections.observableArrayList();
        for(Transaction transaction : transactions) {
            if(transaction.getDate().getMonth() == date.getMonth()) {
                transactionsInRange.add(transaction);
            }
        }
        return transactionsInRange;
    }

    /**
     * Returns all transactions in a year.
     * @param date The date for the year requested.
     * @return The list of transactions in requested year.
     */
    public ObservableList<Transaction> getTransactionsInYear(LocalDate date) {
        ObservableList<Transaction> transactionsInRange = FXCollections.observableArrayList();
        for(Transaction transaction : transactions) {
            if(transaction.getDate().getYear() == date.getYear()) {
                transactionsInRange.add(transaction);
            }
        }
        return transactionsInRange;
    }

    /**
     * Returns pie chart data for subcategories in a category.
     * (Requirement 2.1.2)
     * @param categoryName The category requested.
     * @return The list of chart data by subcategory for requested category.
     */
    public ObservableList<PieChart.Data> getPieChartData(String categoryName) {
        ObservableList<PieChart.Data> theList = FXCollections.observableArrayList();
        for(String subCategoryName : categoryMap.get(categoryName)) {
            double subCategoryAmount = getSubCategoryAmount(transactions, subCategoryName);
            if(subCategoryAmount > 0) {
                theList.add(new PieChart.Data(subCategoryName, subCategoryAmount));
            }
        }

        return theList;
    }

    /**
     * Returns pie chart data for all transactions.
     * @return The list of chart data by category for all transactions.
     */
    public ObservableList<PieChart.Data> getPieChartData() {
        ObservableList<PieChart.Data> theList = FXCollections.observableArrayList();
        for(String categoryName : categoryMap.keySet()) {
            double categoryAmount = getCategoryAmount(transactions, categoryName);
            if(categoryAmount > 0) {
                theList.add(new PieChart.Data(categoryName, categoryAmount));
            }
        }

        return theList;
    }

    /**
     * Returns pie chart data for subcategories in a category restricted to current month.
     * (Requirement 2.1.2)
     * @param categoryName The category requested.
     * @return The list of chart data for current month by subcategory for requested category.
     */
    public ObservableList<PieChart.Data> getMonthPieChartData(String categoryName) {
        ObservableList<PieChart.Data> theList = FXCollections.observableArrayList();
        LocalDate date = LocalDate.now();
        ObservableList<Transaction> transactionsInMonth = getTransactionsInMonth(date);
        for(String subCategory : categoryMap.get(categoryName)) {
            double subCategoryAmount = getSubCategoryAmount(transactionsInMonth, subCategory);
            if(subCategoryAmount > 0) {
                theList.add(new PieChart.Data(subCategory, subCategoryAmount));
            }
        }

        return theList;
    }


    /**
     * Returns pie chart data for all transactions in current month.
     * @return The list of chart data for all transactions in current month.
     */
    public ObservableList<PieChart.Data> getMonthPieChartData() {
        ObservableList<PieChart.Data> theList = FXCollections.observableArrayList();
        LocalDate date = LocalDate.now();
        ObservableList<Transaction> transactionsInMonth = getTransactionsInMonth(date);
        for(String categoryName : categoryMap.keySet()) {
            double categoryAmount = getCategoryAmount(transactionsInMonth, categoryName);
            if(categoryAmount > 0) {
                theList.add(new PieChart.Data(categoryName, categoryAmount));
            }
        }

        return theList;
    }

    /**
     * Returns pie chart data for subcategories in a category restricted to current year.
     * (Requirement 2.1.2)
     * @param categoryName The requested category.
     * @return The list of chart data for current year by subcategory for requested category.
     */
    public ObservableList<PieChart.Data> getYearPieChartData(String categoryName) {
        ObservableList<PieChart.Data> theList = FXCollections.observableArrayList();
        LocalDate date = LocalDate.now();
        ObservableList<Transaction> transactionsInYear = getTransactionsInYear(date);
        for(String subCategoryName : categoryMap.get(categoryName)) {
            double subCategoryAmount = getSubCategoryAmount(transactionsInYear, subCategoryName);
            if(subCategoryAmount > 0) {
                theList.add(new PieChart.Data(subCategoryName, subCategoryAmount));
            }
        }

        return theList;

    }

    /**
     * Returns pie chart data for all transactions in current year.
     * @return The list of chart data for all transactions in current year.
     */
    public ObservableList<PieChart.Data> getYearPieChartData() {
        ObservableList<PieChart.Data> theList = FXCollections.observableArrayList();
        LocalDate date = LocalDate.now();
        ObservableList<Transaction> transactionsInYear = getTransactionsInYear(date);
        for(String categoryName : categoryMap.keySet()) {
            double categoryAmount = getCategoryAmount(transactionsInYear, categoryName);
            if(categoryAmount > 0) {
                theList.add(new PieChart.Data(categoryName, categoryAmount));
            }
        }

        return theList;

    }

    /**
     * Returns amount spent for requested category in requested list.
     * @param list The requested list in which the category amount will be tallied.
     * @param categoryName The requested category.
     * @return The total amount spent in a category in the list requested.
     */
    public double getCategoryAmount(ObservableList<Transaction> list, String categoryName) {
        double total = 0;
        for(Transaction transaction : list) {
            if(transaction.getCategory().equals(categoryName)) {
                total += transaction.getRawAmount();
            }
        }
        return total;

    }

    /**
     * Returns amount spent for requested subcategory in requested list.
     * (Requirement 2.1.2)
     * @param list The requested list in which the subcategory amount will be tallied.
     * @param subCategoryName The requested subcategory.
     * @return The total amount spent in a subcategory in the list requested.
     */
    public double getSubCategoryAmount(ObservableList<Transaction> list, String subCategoryName) {
        double total = 0;
        for(Transaction transaction : list) {
            if(transaction.getSubCategory().equals(subCategoryName)) {
                total += transaction.getRawAmount();
            }
        }
        return total;

    }

}
