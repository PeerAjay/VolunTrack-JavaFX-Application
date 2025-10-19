package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistrationView {
    private final int registrationID;
    private final int slotsRegistered;
    private final int hoursPerSlot;
    private final double totalContribution;
    private final LocalDateTime timestamp;
    private final String title;
    private final String location;
    private final String day;
    private final String username;


    public RegistrationView(int registrationID, int slotsRegistered, int hoursPerSlot, double totalContribution, LocalDateTime timestamp, String title, String location, String day, String username) {
        this.registrationID = registrationID;
        this.slotsRegistered = slotsRegistered;
        this.hoursPerSlot = hoursPerSlot;
        this.totalContribution = totalContribution;
        this.timestamp = timestamp;
        this.title = title;
        this.location = location;
        this.day = day;
        this.username = username;
    }

    public int getRegistrationID() { return registrationID; }
    public int getSlotsRegistered() { return slotsRegistered; }
    public int getHoursPerSlot() { return hoursPerSlot; }
    public double getTotalContribution() { return totalContribution; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getDay() { return day; }
    public String getUserId() { return username; }

    public String getFormattedTimestamp() {
        if (timestamp == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
        return timestamp.format(formatter);
    }

    public String getFormattedRegistrationID() {
        return String.format("%04d", this.registrationID);
    }

}
