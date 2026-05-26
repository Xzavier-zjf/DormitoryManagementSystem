package System;

public class Dormitory {
    private int dormitoryId;
    private String building;
    private int floor;
    private String roomNumber;
    private int bedCount;
    private double price;

    public Dormitory(int dormitoryId, String building, int floor, String roomNumber, int bedCount, double price) {
        this.dormitoryId = dormitoryId;
        this.building = building;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.bedCount = bedCount;
        this.price = price;
    }

    public int getDormitoryId() {
        return dormitoryId;
    }

    public void setDormitoryId(int dormitoryId) {
        this.dormitoryId = dormitoryId;
    }

    // 其他getter和setter方法
    public String getBuilding() {
        return building;
    }
    public void setBuilding(String building) {
        this.building = building;
    }
    public int getFloor() {
        return floor;
    }
    public void setFloor(int floor) {
        this.floor = floor;
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getBedCount() {
        return bedCount;
    }
    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

}
