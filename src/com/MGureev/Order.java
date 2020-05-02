package com.MGureev;

public class Order {

    private String orderName;
    private int weight;

    private AccessWindow accessWindow;
    private Coordinates coordinates;

    private int timeLoading;
    private int timeUnloading;


    private int timeServiceStart;
    private int timeServiceFinish;

    public Order() {
    }

    public Order(String orderName, int weight, AccessWindow accessWindow, Coordinates coordinates, int timeLoading, int timeUnloading) {
        this.orderName = orderName;
        this.weight = weight;
        this.accessWindow = accessWindow;
        this.coordinates = coordinates;
        this.timeLoading = timeLoading;
        this.timeUnloading = timeUnloading;
    }



    public void setTimeStartService(int timeStartService) {
        this.timeServiceStart = timeStartService;
    }

    public void setTimeFinishService(int timeFinishService) {
        this.timeServiceFinish = timeFinishService;
    }

    public int getTimeServiceStart() {
        return timeServiceStart;
    }

    public int getTimeServiceFinish() {
        return timeServiceFinish;
    }

    public String getOrderName() {
        return orderName;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getWeight() {
        return weight;
    }

    public AccessWindow getAccessWindow() {
        return accessWindow;
    }

    public int getLoadingTime() {
        return timeLoading;
    }

    public int getUnloadingTime() {
        return timeUnloading;
    }

}
