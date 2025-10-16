package model;

public class CartItem {
    private String username;
    private int projectID;
    private int numSlots;
    private int hoursPerSlot;

    public CartItem(){};

    public CartItem(String username, int projectID, int numSlots, int hoursPerSlot){
        this.username = username;
        this.projectID = projectID;
        this.numSlots = numSlots;
        this.hoursPerSlot = hoursPerSlot;
    }

    //Getters and setters
    public String getUsername() {
        return username;
    }
    public int getProjectID() {
        return projectID;
    }
    public int getNumSlots() {
        return numSlots;
    }
    public int getHoursPerSlot() {
        return hoursPerSlot;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
    public void setNumSlots(int numSlots) {
        this.numSlots = numSlots;
    }
    public void setHoursPerSlot(int hoursPerSlot) {
        this.hoursPerSlot = hoursPerSlot;
    }


}
