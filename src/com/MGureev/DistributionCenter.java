package com.MGureev;

import java.util.ArrayList;

public class DistributionCenter {

    private Coordinates coordinatesDC;
    private AccessWindow accessWindow;
    private ArrayList<Resource> resources = new ArrayList<>();

    public DistributionCenter(Coordinates coordinatesDC, AccessWindow accessWindow) {
        this.coordinatesDC = coordinatesDC;
        this.accessWindow = accessWindow;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public Coordinates getCoordinatesDC() {
        return coordinatesDC;
    }

    public AccessWindow getAccessWindow() {
        return accessWindow;
    }

    public void printFullTimeStatistic() {
        for (int a = 0; a < resources.size(); a++) {
            System.out.println("RESOURCE #"+(a+1));
            resources.get(a).calcAndPrintTripsTimeDelivery();
            resources.get(a).printMainTimeStatistic();
            System.out.println("DC time work "+ accessWindow.getStandardStartTime() + " - "+ accessWindow.getStandardFinishTime());
            System.out.print("CONCLUSION: ");
            if (dcIsWork(resources.get(a))) {
                System.out.println("resource #"+(a+1)+" fits in DC access window");
            }
            else {
                System.out.println("resource #"+(a+1)+" not fits in DC access window");
            }
            System.out.println();
        }
    }

    public boolean dcIsWork(Resource resource) {
        int rTimeStart = resource.getMainTimeDeliveryStart();
        int rTimeFinish = resource.getMainTimeDeliveryFinish();

        int dcTimeStart = accessWindow.getMinuteStart();
        int dcTimeFinish = accessWindow.getMinuteFinish();

        if (dcTimeStart <= rTimeStart && dcTimeFinish >= rTimeFinish) {
            return true;
        } else {
            return false;
        }
    }
}
