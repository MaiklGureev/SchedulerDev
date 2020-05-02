package com.MGureev;

import java.util.ArrayList;

public class DistributionCenter {

    private Coordinates coordinatesDC;
    private AccessWindow accessWindow;
    private int countResources;
    private ArrayList<Resource> resources = new ArrayList<>();

    public DistributionCenter(Coordinates coordinatesDC, AccessWindow accessWindow) {
        this.coordinatesDC = coordinatesDC;
        this.accessWindow = accessWindow;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
        countResources = resources.size();
    }

    public void calculateTimeLoading(){
        int timeDCStart = accessWindow.getMinuteStart();
        int timeDCFinish = accessWindow.getMinuteFinish();
    }

    public Coordinates getCoordinatesDC() {
        return coordinatesDC;
    }


}
