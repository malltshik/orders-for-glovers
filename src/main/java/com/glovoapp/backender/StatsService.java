package com.glovoapp.backender;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.glovoapp.backender.DistanceCalculator.calculateDistance;

@Service
public class StatsService {

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    @Autowired
    public StatsService(OrderRepository orderRepository, CourierRepository courierRepository) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
    }

    /**
     * Number of orders
     * Number of couriers
     * Percentage of non food orders
     * Average courier-to-pickup distance in meters
     * Average pickup-to-delivery distance in meters
     *
     * @return stat
     */
    public OrderStats getStats() {
        OrderStats stats = new OrderStats();

        List<Order> orders = orderRepository.findAll();
        List<Courier> couriers = courierRepository.findAll();

        stats.setNumberOfOrders(orders.size());
        stats.setNumberOfCouriers(couriers.size());
        stats.setNonFoodPercentage(percentageNonFoodOrders(orders));

        ImmutablePair<Double, Double> averageDistances = averageDistances(orders, couriers);
        stats.setAveragePickup(averageDistances.getLeft());
        stats.setAverageDeliver(averageDistances.getRight());

        return stats;
    }

    private double percentageNonFoodOrders(List<Order> orders) {
        long nonFood = orders.stream().filter(o -> !o.getFood()).toArray().length;
        long size = orders.size();
        if (size <= 0 || nonFood <= 0) return 0;
        else return (nonFood * 100) / size;
    }

    private ImmutablePair<Double, Double> averageDistances(List<Order> orders, List<Courier> couriers) {
        double averageToPick = 0;
        double averageToDelivery = 0;
        for (Order order : orders) {
            for (Courier courier : couriers) {
                double toPick = calculateDistance(courier.getLocation(), order.getPickup()) * 1000;
                double toDelivery = calculateDistance(courier.getLocation(), order.getDelivery()) * 1000;

                if (averageToPick == 0) averageToPick = toPick;
                else averageToPick = (averageToPick + toPick) / 2;

                if (averageToDelivery == 0) averageToDelivery = toDelivery;
                else averageToDelivery = (averageToDelivery + toDelivery) / 2;
            }
        }
        return new ImmutablePair<>(averageToPick, averageToDelivery);
    }
}
