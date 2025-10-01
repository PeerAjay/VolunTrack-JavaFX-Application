package model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

import javax.swing.*;

public class Project {
    private final StringProperty title;
    private final StringProperty location;
    private final StringProperty day;
    private final IntegerProperty hourlyValue;
    private final IntegerProperty regSlots;
    private final IntegerProperty totalSlots;

    public Project(StringProperty title, StringProperty location, StringProperty day, IntegerProperty hourlyValue,
                   IntegerProperty regSlots, IntegerProperty totalSlots) {
        this.title = title;
        this.location = location;
        this.day = day;
        this.hourlyValue = hourlyValue;
        this.regSlots = regSlots;
        this.totalSlots = totalSlots;
    }
}

