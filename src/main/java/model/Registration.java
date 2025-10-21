package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Registration {
    private final StringProperty userId = new SimpleStringProperty();
    private final IntegerProperty projectId = new SimpleIntegerProperty();
    private final IntegerProperty regSlots = new SimpleIntegerProperty();
    private final IntegerProperty hoursPerSlot = new SimpleIntegerProperty();
    private final IntegerProperty totalContribution = new SimpleIntegerProperty();
    private final LocalDateTime timeStamp;

    public Registration(String userId, int projectId, int regSlots, int hoursPerSlot, int totalContribution, LocalDateTime timeStamp){
        setUserId(userId);
        setProjectId(projectId);
        setRegSlots(regSlots);
        setHoursPerSlot(hoursPerSlot);
        setTotalContribution(totalContribution);
        this.timeStamp = timeStamp;
    };


    public String getUserId(){
        return userId.get();
    }
    public void setUserId(String value){
        userId.set(value);
    }

    public int getProjectId(){
        return projectId.get();
    }
    public void setProjectId(int value){
        projectId.set(value);
    }

    public int getRegSlots(){
        return regSlots.get();
    }
    public void setRegSlots(int value){
        regSlots.set(value);
    }

    public int getHoursPerSlot(){
        return hoursPerSlot.get();
    }
    public void setHoursPerSlot(int value){
        hoursPerSlot.set(value);
    }

    public int getTotalContribution(){
        return totalContribution.get();
    }
    public void setTotalContribution(int value){
        totalContribution.set(value);
    }


}
