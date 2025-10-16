package model;

public class CartEntry {
    private String title;
    private String location;
    private String day;
    private int hourlyValue;
    private int numSlots;
    private int hoursPerSlot;
    private int cartItemID;

    public CartEntry(){};

    public CartEntry(String title, String location, String day, int hourlyValue, int numSlots, int hoursPerSlot, int cartItemID){
        this.title = title;
        this.location = location;
        this.day = day;
        this.hourlyValue = hourlyValue;
        this.numSlots = numSlots;
        this.hoursPerSlot = hoursPerSlot;
        this.cartItemID = cartItemID;
    }

    //Getters and setters
    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location){
        this.title = title;
    }

    public String getDay() {
        return day;
    }
    public void setDay(String day){
        this.day = day;
    }

    public int getHourlyValue() {
        return hourlyValue;
    }
    public void setHourlyValue(int hourlyValue){
        this.hourlyValue = hourlyValue;
    }

    public int getNumSlots() {
        return numSlots;
    }
    public void setNumSlots(int numSlots){
        this.numSlots = numSlots;
    }

    public int getHoursPerSlot() {
        return hoursPerSlot;
    }
    public void setHoursPerSlot(int hoursPerSlot){
        this.hoursPerSlot = hoursPerSlot;
    }

    public int getCartItemID() {
        return cartItemID;
    }
    public void setCartItemID() {
        this.cartItemID = cartItemID;
    }


}
