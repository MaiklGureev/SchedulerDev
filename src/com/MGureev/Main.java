package com.MGureev;

public class Main {

    public static void main(String[] args) {
	// write your code here

        DistributionCenter distributionCenter =
                new DistributionCenter(new Coordinates(53.195538, 50.101783),
                        new AccessWindow(9,0,18,0));

        Order order_1 =
                new Order("order_1",20,new AccessWindow(12,30,13,0),
                        new Coordinates(53.215397, 50.146258),10,10);

        Order order_2 =new Order("order_2",10,new AccessWindow(15,0,16,0),
                new Coordinates(53.205815, 50.189533),5,5);

        Order order_3 =new Order("order_3",20,new AccessWindow(16,0,17,0),
                new Coordinates(53.250913, 50.222821),15,5);


        Resource r1 = new Resource(40,40,distributionCenter);
        Resource r2 = new Resource(60,50,distributionCenter);
        Resource r3 = new Resource(60,50,distributionCenter);

        r1.addOrder(order_1);
        r1.addOrder(order_2);
        r1.addOrder(order_3);

        r2.addOrder(order_1);
        r2.addOrder(order_2);
        r2.addOrder(order_3);

        r3.addOrder(order_1);
        r3.addOrder(order_2);
        r3.addOrder(order_3);

        distributionCenter.addResource(r1);
        distributionCenter.addResource(r2);
        distributionCenter.addResource(r3);

        r1.calcAndPrintTripsTimeDelivery();
        r1.printMainTimeStatistic();




//        Resource r2 = new Resource(2,100,distributionCenter);
//        Resource r3 = new Resource(3,100,distributionCenter);



//        distributionCenter.addResource(r2);
//        distributionCenter.addResource(r3);

//        r2.addOrder(order_3);
//        r2.addOrder(order_4);
//        r2.addOrder(order_5);
//        r3.addOrder(order_6);
//        r3.addOrder(order_7);

        //r1.calcFullTimeDelivery();

//        Coordinates coordinates = new Coordinates(53.125874, 49.917233);
//        Coordinates coordinates1 = new Coordinates(53.212862, 50.193016);
//        System.out.println(Coordinates.calculateDistance(coordinates,coordinates1));
//        System.out.println(r1.calculateTimeBetweenPoints(coordinates,coordinates1));


    }
}
