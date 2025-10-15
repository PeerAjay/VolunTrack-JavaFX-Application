package model;

public class CartItem {
    private int cartID;
    private String username;
    private int projectID;
    private int numSlots;
    private int hoursPerSlot;

    public CartItem(){};

    public CartItem(int cartID, String username, int projectID, int numSlots, int hoursPerSlot){
        this.cartID = cartID;
        this.username = username;
        this.projectID = projectID;
        this.numSlots = numSlots;
        this.hoursPerSlot = hoursPerSlot;
    }

    //Getters and setters
    public int getCartID() {
        return cartID;
    }
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

    public void setCartID(int cartID) {
        this.cartID = cartID;
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
