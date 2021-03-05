package App;

import java.time.LocalDate;

public interface Transaction {
    public String getCategory();

    public void setCategory(String category);

    public String getSubCategory();

    public double getAmount();

    public double getRawAmount();

    public void setAmount(double amount);

    public void setDate(LocalDate date);

    public LocalDate getDate();

    public String getFormattedDate();

    public String getFormattedValue();

    public String getType();
}
