package com.MGureev;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;

public class Resource {

    private int speed;
    private int maxWeight;
    private int currentWeight;
    private int mainTimeDeliveryStart = 0;
    private int mainTimeDeliveryFinish = 0;

    private int tripTimeDeliveryStart = 0;
    private int tripTimeDeliveryFinish = 0;

    private ArrayList<Order> orders = new ArrayList<>();

    private DistributionCenter distributionCenter;

    public Resource(int speed, int maxWeight, DistributionCenter distributionCenter) {
        this.speed = speed;
        this.maxWeight = maxWeight;
        this.distributionCenter = distributionCenter;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public double getSpeed() {
        return speed;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public ArrayList<Order> calcTimeDelivery(ArrayList<Order> deliveryOrders) {
        int timeStart = 0;
        int timeFinish = 0;
        int timeDelivery = 0;
        int timeLoading = 0;
        Order firstOrder;
        Coordinates pointB;
        Coordinates pointA;

        for (int a = 0; a < deliveryOrders.size(); a++) {
            timeLoading += deliveryOrders.get(a).getLoadingTime();
        }

        //рассчёт время начала погрузки по первому заказу в списке
        if (!deliveryOrders.isEmpty()) {
            firstOrder = deliveryOrders.get(0);
            pointA = distributionCenter.getCoordinatesDC();
            pointB = deliveryOrders.get(0).getCoordinates();
            timeDelivery = calculateTimeBetweenPoints(pointA, pointB);


            //выставление времени начала работы ресурса
            tripTimeDeliveryStart = firstOrder.getAccessWindow().getMinuteStart() - timeLoading - timeDelivery;
            if (mainTimeDeliveryStart == 0) {
                mainTimeDeliveryStart = tripTimeDeliveryStart;
            }
            timeStart = tripTimeDeliveryStart;
            timeFinish = tripTimeDeliveryStart;
        }


        for (int a = 0; a < deliveryOrders.size(); a++) {
            //время начала работы с заказом(начало загрузки)
            deliveryOrders.get(a).setTimeStartService(timeStart);
            timeStart += deliveryOrders.get(a).getLoadingTime();

            //время окончания работы с заказом(после разрузки)
            if (a == 0 || a == (deliveryOrders.size() - 1)) {
                pointA = distributionCenter.getCoordinatesDC();
                pointB = deliveryOrders.get(a).getCoordinates();
                timeDelivery = calculateTimeBetweenPoints(pointA, pointB);
                timeFinish += timeLoading + timeDelivery + deliveryOrders.get(a).getUnloadingTime();
            } else {
                pointA = deliveryOrders.get(a).getCoordinates();
                pointB = deliveryOrders.get(a + 1).getCoordinates();
                timeDelivery = calculateTimeBetweenPoints(pointA, pointB);
                timeFinish += deliveryOrders.get(a).getLoadingTime() + timeDelivery + deliveryOrders.get(a).getUnloadingTime();
            }
            deliveryOrders.get(a).setTimeFinishService(timeFinish);
        }

        //время возвращения с последнего заказа в DC
        pointA = deliveryOrders.get(deliveryOrders.size() - 1).getCoordinates();
        pointB = distributionCenter.getCoordinatesDC();
        timeDelivery = calculateTimeBetweenPoints(pointA, pointB);
        //выставление/обновление времени окончания работы ресурса
        tripTimeDeliveryFinish = timeFinish + timeDelivery;
        mainTimeDeliveryFinish = tripTimeDeliveryFinish;
        return deliveryOrders;
    }

    public void calcAndPrintTripsTimeDelivery() {

        ArrayList<Order> tempOrders = orders;
        ArrayList<Order> deliveryOrders = new ArrayList<>();
        int numTrip = 0;

        while (!tempOrders.isEmpty()) {
            //добавляем в рейс заказы
            for (int a = 0; a < tempOrders.size(); a++) {
                if (!resourceIsFull(tempOrders.get(a).getWeight())) {
                    deliveryOrders.add(tempOrders.get(a));
                    tempOrders.remove(tempOrders.get(a));
                    a -= 1;
                } else {
                    break;
                }
            }
            if (!deliveryOrders.isEmpty()) {
                numTrip += 1;
                deliveryOrders = calcTimeDelivery(deliveryOrders);
                printTripTimeStatistic(deliveryOrders, numTrip);
                deliveryOrders.removeAll(deliveryOrders);
                currentWeight = 0;
            }
        }


    }

    public boolean resourceIsFull(int addWeight) {
        currentWeight += addWeight;
        if (currentWeight > maxWeight) {
            return true;
        } else
            return false;
    }

    //время поездки между двумя точками
    public int calculateTimeBetweenPoints(Coordinates pointA, Coordinates pointB) {
        int time = 0;
        double distance = Coordinates.calculateDistance(pointA, pointB);
        time = (int) (distance / speed * 60);
        return time;
    }


    public void printTripTimeStatistic(ArrayList<Order> deliveryOrders, int numTrip) {
        System.out.println(String.format("TRIP #%d count orders %d", numTrip,deliveryOrders.size()));
        System.out.println("trip loading start time: " + AccessWindow.timeConvert(mainTimeDeliveryStart));
        for (int a = 0; a < deliveryOrders.size(); a++) {
            System.out.println("--------------------------------");
            System.out.println("order name " + deliveryOrders.get(a).getOrderName());
            System.out.println("access window " + deliveryOrders.get(a).getAccessWindow().getStandardStartTime() +
                    " - " + deliveryOrders.get(a).getAccessWindow().getStandardFinishTime());
            System.out.println("order start time " + AccessWindow.timeConvert(deliveryOrders.get(a).getTimeServiceStart()));
            System.out.println("order finish time " + AccessWindow.timeConvert(deliveryOrders.get(a).getTimeServiceFinish()));
        }
        System.out.println("--------------------------------");
        System.out.println("resource return time: " + AccessWindow.timeConvert(mainTimeDeliveryFinish));
        System.out.println("resource trip time work: " + AccessWindow.timeConvert(tripTimeDeliveryFinish - tripTimeDeliveryStart));
        System.out.println("++++++++++++++++++++++++++++++++");
    }

    public void printMainTimeStatistic(){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("resource start main time: " + AccessWindow.timeConvert(mainTimeDeliveryStart));
        System.out.println("resource return main time: " + AccessWindow.timeConvert(mainTimeDeliveryFinish));
        System.out.println("resource main time work: " + AccessWindow.timeConvert(mainTimeDeliveryFinish - mainTimeDeliveryStart));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");}
}
