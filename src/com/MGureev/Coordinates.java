package com.MGureev;

public class Coordinates {

    private double latitude;//широта f
    private double longitude;//долгота l

    public Coordinates() {
    }

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    //https://en.wikipedia.org/wiki/Great-circle_distance
    //https://calculatorium.ru/math/degree-radian-conversion
    //https://www.kobzarev.com/programming/calculation-of-distances-between-cities-on-their-coordinates/
    public static double calculateDistance(Coordinates pointA, Coordinates pointB){

        double distance = 0;
        double earthRadius = 6371;

        double l1 = pointA.getLongitude();
        double f1 = pointA.getLatitude();
        double l2 = pointB.getLongitude();
        double f2 = pointB.getLatitude();

        l1= l1*Math.PI/180;
        f1= f1*Math.PI/180;
        l2= l2*Math.PI/180;
        f2= f2*Math.PI/180;

        double deltaX = Math.cos(f2)*Math.cos(l2)-Math.cos(f1)*Math.cos(l1);
        double deltaY = Math.cos(f2)*Math.sin(l2)-Math.cos(f1)*Math.sin(l1);
        double deltaZ = Math.sin(f2)-Math.sin(f1);
        double c = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2)+Math.pow(deltaZ,2));
        double sigma = 2*Math.asin(c/2);

        distance = sigma*earthRadius;
        return distance;
    }
}
