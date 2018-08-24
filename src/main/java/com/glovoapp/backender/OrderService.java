package com.glovoapp.backender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.glovoapp.backender.DistanceCalculator.calculateDistance;
import static com.glovoapp.backender.Vehicle.BICYCLE;
import static java.util.stream.Collectors.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Value("#{'${backender.filter.keywords}'.split(',')}")
    private List<String> keywords;

    @Value("${backender.filter.bicycle-limit:#{5.0}}")
    private double bicycleLimit;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll(Courier courier) {
        List<Order> orders = orderRepository.findAll();
        if (courier.getBox()) {
            orders = orders.parallelStream()
                    .filter(order -> this.keywords.parallelStream().anyMatch(order.getDescription()::contains))
                    .collect(toList());
        }
        if (courier.getVehicle().equals(BICYCLE)) {
            orders = orders.parallelStream()
                    .filter(order -> calculateDistance(courier.getLocation(), order.getPickup()) <= bicycleLimit)
                    .collect(toList());
        }
        return orders;
    }

}
