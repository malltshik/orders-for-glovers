package com.glovoapp.backender;

public class OrderStats {

    private long numberOfOrders;
    private long numberOfCouriers;
    private double nonFoodPercentage;
    private double averagePickup;
    private double averageDeliver;

    public OrderStats() {
    }

    public long getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(long numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public long getNumberOfCouriers() {
        return numberOfCouriers;
    }

    public void setNumberOfCouriers(long numberOfCouriers) {
        this.numberOfCouriers = numberOfCouriers;
    }

    public double getNonFoodPercentage() {
        return nonFoodPercentage;
    }

    public void setNonFoodPercentage(double nonFoodPercentage) {
        this.nonFoodPercentage = nonFoodPercentage;
    }

    public double getAveragePickup() {
        return averagePickup;
    }

    public void setAveragePickup(double averagePickup) {
        this.averagePickup = averagePickup;
    }

    public double getAverageDeliver() {
        return averageDeliver;
    }

    public void setAverageDeliver(double averageDeliver) {
        this.averageDeliver = averageDeliver;
    }
}
