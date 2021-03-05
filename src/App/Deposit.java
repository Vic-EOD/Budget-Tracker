package App;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * The class for transactions of type Deposit.
 * @author Victor Martinez
 */
public class Deposit implements Transaction{
    double amount;
    String category;
    String subCategory;
    LocalDate date;

    /**
     * Main constructor of class.
     * @param amount The dollar amount of the deposit.
     * @param category The category of the deposit.
     * @param subCategory The subcategory of the deposit.
     * @param date The date of the deposit.
     */
    public Deposit(double amount, String category, String subCategory ,LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.subCategory = subCategory;
    }
    @Override
    public String getSubCategory() {
        return subCategory;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public double getRawAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the date of the deposit in a formatted date.
     * The format is Month Day, Year or MMM DD, YYYY ex. April 1, 2020
     * @return The formatted date in MMM DD, YYYY.
     */
    @Override
    public String getFormattedDate() {
        return getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns a formatted string of the amount of the deposit.
     * Format is #,###.00 ex. 1,230.25 or 54.80
     * @return The formatted amount value in #,###.00 format.
     */
    @Override
    public String getFormattedValue() {
        DecimalFormat myFormat = new DecimalFormat("#,###.00");
        return "$" + myFormat.format(amount);
    }

    @Override
    public String getType() {
        return "Deposit";
    }

    /**
     * Returns a string with all class attributes separated by "|".
     * @return The attributes of the class separated by the | character.
     */
    @Override
    public String toString() {
        return getFormattedDate() + " | " + getCategory() + " | " +
                getType() + " | " + getFormattedValue();
    }
}
