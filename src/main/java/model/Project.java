package model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

import javax.swing.*;

public class Project {
    private StringProperty title;
    private StringProperty location;
    private StringProperty day;
    private IntegerProperty hourlyValue;
    private IntegerProperty regSlots;
    private IntegerProperty totalSlots;

    public Project(StringProperty title, StringProperty location, StringProperty day, IntegerProperty hourlyValue,
                   IntegerProperty regSlots, IntegerProperty totalSlots) {
        this.title = title;
        this.location = location;
        this.day = day;
        this.hourlyValue = hourlyValue;
        this.regSlots = regSlots;
        this.totalSlots = totalSlots;
    }

    //GETTERS AND SETTERS
    public void setTitle(StringProperty title){
        this.title = title;
    }
    public StringProperty getTitle(){
        return this.title;
    }

    public void setLocation(StringProperty location){
        this.location = location;
    }
    public StringProperty getLocation(){
        return this.location;
    }

    public void setDay(StringProperty day){
        this.day = day;
    }
    public StringProperty getDay(){
        return this.day;
    }

    public void setHourlyValue(IntegerProperty hourlyValue){
        this.hourlyValue = hourlyValue;
    }
    public IntegerProperty getHourlyValue(){
        return this.hourlyValue;
    }

    public void setRegSlots(IntegerProperty regSlots){
        this.regSlots = regSlots;
    }
    public IntegerProperty getRegSlots(){
        return this.regSlots;
    }

    public void setTotalSlots(IntegerProperty totalSlots){
        this.totalSlots = totalSlots;
    }
    public IntegerProperty getTotalSlots(){
        return this.totalSlots;
    }


}

