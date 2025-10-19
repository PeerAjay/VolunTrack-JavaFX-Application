package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProjectAdd {
    //Initialize properties directly
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty location = new SimpleStringProperty();
    private final StringProperty day = new SimpleStringProperty();
    private final IntegerProperty hourlyValue = new SimpleIntegerProperty();
    private final StringProperty regSlots = new SimpleStringProperty();
    private final StringProperty totalSlots = new SimpleStringProperty();
    private String isEnabled;

    //Constructor for the plain data types
    public ProjectAdd(String title, String location, String day, int hourlyValue, String regSlots, String totalSlots, String isEnabled) {
        setTitle(title);
        setLocation(location);
        setDay(day);
        setHourlyValue(hourlyValue);
        setRegSlots(regSlots);
        setTotalSlots(totalSlots);
        this.isEnabled = isEnabled;
    }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }

    public String getLocation() { return location.get(); }
    public void setLocation(String value) { location.set(value); }

    public String getDay() { return day.get(); }
    public void setDay(String value) { day.set(value); }

    public int getHourlyValue() { return hourlyValue.get(); }
    public void setHourlyValue(int value) { hourlyValue.set(value); }

    public String getRegSlots() { return regSlots.get(); }
    public void setRegSlots(String value) { regSlots.set(value); }

    public String getTotalSlots() { return totalSlots.get(); }
    public void setTotalSlots(String value) { totalSlots.set(value); }

    public String getIsEnabled() { return isEnabled; }
    public void setIsEnabled(String isEnabled) { this.isEnabled = isEnabled; }


    //Property Getters
    public StringProperty titleProperty() { return title; }
    public StringProperty locationProperty() { return location; }
    public StringProperty dayProperty() { return day; }
    public IntegerProperty hourlyValueProperty() { return hourlyValue; }
    public StringProperty regSlotsProperty() { return regSlots; }
    public StringProperty totalSlotsProperty() { return totalSlots; }
}