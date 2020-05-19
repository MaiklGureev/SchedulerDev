package com.MGureev;

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

    public int getMainTimeDeliveryStart() {
        return mainTimeDeliveryStart;
    }

    public int getMainTimeDeliveryFinish() {
        return mainTimeDeliveryFinish;
    }

    public ArrayList<Order> calcTimeDelivery(ArrayList<Order> deliveryOrders, int numberTrip) {

        int timeStart = 0;
        int timeFinish = 0;
        int timeDelivery = 0;
        int timeLoading = 0;
        Order firstOrder, secondOrder;
        Coordinates pointB;
        Coordinates pointA;

        //расчёт времени погрузки на рейс
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
            if (deliveryOrders.size() > 1) {
                secondOrder = deliveryOrders.get(1);
                // если окно первого заказа больше окна второго то отталкиваемся от конца окна следующего заказа
                if (firstInAccessWindowNext(firstOrder, secondOrder)) {
                    //время доставки от ДС к первому закзау и от первого ко второму
                    timeDelivery = calculateTimeBetweenPoints(distributionCenter.getCoordinatesDC(), firstOrder.getCoordinates()) +
                            calculateTimeBetweenPoints(firstOrder.getCoordinates(), secondOrder.getCoordinates());
                    tripTimeDeliveryStart = secondOrder.getAccessWindow().getMinuteFinish() -
                            firstOrder.getUnloadingTime() - secondOrder.getUnloadingTime() -
                            timeDelivery - timeLoading;
                    //время доставки от ДС к первому закзау
                    timeDelivery = calculateTimeBetweenPoints(distributionCenter.getCoordinatesDC(), firstOrder.getCoordinates());
                    timeStart = tripTimeDeliveryStart + timeDelivery + timeLoading;
                    timeFinish = timeStart + secondOrder.getUnloadingTime();
                } else {
                    pointA = distributionCenter.getCoordinatesDC();
                    pointB = firstOrder.getCoordinates();
                    timeDelivery = calculateTimeBetweenPoints(pointA, pointB);
                    if (numberTrip == 1) {
                        //если рейс первый начинаем ориентируясь от конца окна клиента
                        tripTimeDeliveryStart = firstOrder.getAccessWindow().getMinuteFinish() - firstOrder.getUnloadingTime() - timeLoading - timeDelivery;
                        timeStart = tripTimeDeliveryStart + timeLoading + timeDelivery;
                    } else {
                        //если рейс второй третий и так далее приезжаем к началу окна, чтобы быстрее закончить
                        tripTimeDeliveryStart = firstOrder.getAccessWindow().getMinuteStart() - timeLoading - timeDelivery;
                        timeStart = tripTimeDeliveryStart;
                    }
                    timeFinish = timeStart + firstOrder.getUnloadingTime();
                }
            }
            //если заказ на рейсе единственный
            else {
                pointA = distributionCenter.getCoordinatesDC();
                pointB = firstOrder.getCoordinates();
                timeDelivery = calculateTimeBetweenPoints(pointA, pointB);
                //если дистрибьютерский центр закрыт до начала окона закза
                if (distributionCenter.getAccessWindow().getMinuteStart() >= firstOrder.getAccessWindow().getMinuteStart()) {
                    timeStart = distributionCenter.getAccessWindow().getMinuteStart()+timeLoading+timeDelivery;
                }else{
                    timeStart = firstOrder.getAccessWindow().getMinuteStart();
                }
                timeFinish = timeStart+firstOrder.getUnloadingTime();
                tripTimeDeliveryStart = timeStart - timeLoading - timeDelivery;
            }
            //выставляем время начала работы доставки по времени начала первого рейса
            if (mainTimeDeliveryStart == 0) {
                mainTimeDeliveryStart = tripTimeDeliveryStart;
            }

        }


        for (int a = 0; a < deliveryOrders.size(); a++) {


            //время окончания работы с заказом(после разрузки)
            //если заказ первый
            if (a == 0) {
                deliveryOrders.get(a).setTimeStartService(timeStart);
                deliveryOrders.get(a).setTimeFinishService(timeFinish);
            }
            //если заказ последний
            else if (a == (deliveryOrders.size() - 1)) {
                pointA = distributionCenter.getCoordinatesDC();
                pointB = deliveryOrders.get(a).getCoordinates();
                timeDelivery = calculateTimeBetweenPoints(pointA, pointB);
                //если прошлый заказа закончили раньше начала окна последнего
                if (timeFinish < deliveryOrders.get(a).getAccessWindow().getMinuteStart()) {
                    timeStart = deliveryOrders.get(a).getAccessWindow().getMinuteStart();
                    timeFinish = deliveryOrders.get(a).getAccessWindow().getMinuteStart() + deliveryOrders.get(a).getUnloadingTime();
                } else {
                    timeStart += timeDelivery;
                    timeFinish += timeDelivery + deliveryOrders.get(a).getUnloadingTime();
                }
            }
            //если промежуточный
            else {
                firstOrder = deliveryOrders.get(a);
                secondOrder = deliveryOrders.get(a + 1);

                //если время окна второго заказа поместилось в первый
                if (firstInAccessWindowNext(firstOrder, secondOrder)) {
                    //рассчитываем исходя из окончания окна второго закза
                    timeDelivery = calculateTimeBetweenPoints(firstOrder.getCoordinates(), secondOrder.getCoordinates());
                    timeStart = secondOrder.getAccessWindow().getMinuteFinish() -
                            firstOrder.getUnloadingTime() - secondOrder.getUnloadingTime() -
                            timeDelivery;
                    timeFinish = timeStart + secondOrder.getUnloadingTime();
                }
                //если время окна доставки второго не поместилось в первое
                else {
                    //если прошлый заказа закончили раньше начала окна текущего
                    if (timeFinish < deliveryOrders.get(a).getAccessWindow().getMinuteStart()) {
                        timeStart = deliveryOrders.get(a).getAccessWindow().getMinuteStart();
                        timeFinish = deliveryOrders.get(a).getAccessWindow().getMinuteStart() + deliveryOrders.get(a).getUnloadingTime();
                    }
                    //если прошлый заказа закончили в окне текущего
                    else {
                        timeStart += timeDelivery;
                        timeFinish += timeDelivery + deliveryOrders.get(a).getUnloadingTime();
                    }
                }

            }
            //выставляем время работы на заказе
            deliveryOrders.get(a).setTimeStartService(timeStart);
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
                deliveryOrders = calcTimeDelivery(deliveryOrders, numTrip);
                printTripTimeStatistic(deliveryOrders, numTrip);
                deliveryOrders.removeAll(deliveryOrders);
                currentWeight = 0;
            }
        }
    }

    public boolean firstInAccessWindowNext(Order firstOrder, Order nextOrder) {
        if (firstOrder.getAccessWindow().getMinuteStart() < nextOrder.getAccessWindow().getMinuteStart() &&
                firstOrder.getAccessWindow().getMinuteFinish() > nextOrder.getAccessWindow().getMinuteFinish()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean resourceIsFull(int addWeight) {
        currentWeight += addWeight;
        if (currentWeight > maxWeight) {
            currentWeight -= addWeight;
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
        System.out.println("TRIP STATISTIC:");
        System.out.println(String.format("TRIP #%d count orders %d weight %d/%d",
                numTrip, deliveryOrders.size(), currentWeight, maxWeight));
        System.out.println("trip loading start time: " + AccessWindow.timeConvert(tripTimeDeliveryStart));
        for (int a = 0; a < deliveryOrders.size(); a++) {
            System.out.println("--------------------------------");
            System.out.println("    order name " + deliveryOrders.get(a).getOrderName());
            System.out.println("    time loading & unloading " +
                    deliveryOrders.get(a).getLoadingTime() + ", " + deliveryOrders.get(a).getUnloadingTime());
            System.out.println("    access window " + deliveryOrders.get(a).getAccessWindow().getStandardStartTime() +
                    " - " + deliveryOrders.get(a).getAccessWindow().getStandardFinishTime());
            System.out.println("    order start time " + AccessWindow.timeConvert(deliveryOrders.get(a).getTimeServiceStart()));
            System.out.println("    order finish time " + AccessWindow.timeConvert(deliveryOrders.get(a).getTimeServiceFinish()));
        }
        System.out.println("--------------------------------");
        System.out.println("    resource return time to DC: " + AccessWindow.timeConvert(mainTimeDeliveryFinish));
        System.out.println("    resource trip time work: " + AccessWindow.timeConvert(tripTimeDeliveryFinish - tripTimeDeliveryStart));
        System.out.println("++++++++++++++++++++++++++++++++");
    }

    public void printMainTimeStatistic() {
        System.out.println("RESOURCE STATISTIC:");
        System.out.println("    resource start main time: " + AccessWindow.timeConvert(mainTimeDeliveryStart));
        System.out.println("    resource return main time: " + AccessWindow.timeConvert(mainTimeDeliveryFinish));
        System.out.println("    resource main time work: " + AccessWindow.timeConvert(mainTimeDeliveryFinish - mainTimeDeliveryStart));
    }
}
