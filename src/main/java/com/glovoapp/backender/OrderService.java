package com.glovoapp.backender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.glovoapp.backender.DistanceCalculator.calculateDistance;
import static com.glovoapp.backender.Vehicle.BICYCLE;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final OrderProperties props;

    @Autowired
    public OrderService(OrderRepository repo, OrderProperties props) {
        this.props = props;
        this.repo = repo;
    }

    public List<Order> findAll(Courier courier) {
        List<Order> orders = repo.findAll();
        orders = filterByKeyWords(courier, orders);
        orders = filterByDistance(courier, orders);
        orders.sort((o1, o2) -> orderComparator(courier, o1, o2));
        return orders;
    }

    private List<Order> filterByDistance(Courier courier, List<Order> orders) {
        if (courier.getVehicle().equals(BICYCLE)) {
            orders = orders.parallelStream()
                    .filter(order -> calculateDistance(courier.getLocation(),
                            order.getPickup()) <= props.getBicycleLimit())
                    .collect(toList());
        }
        return orders;
    }

    private List<Order> filterByKeyWords(Courier courier, List<Order> orders) {
        if (!courier.getBox()) {
            orders = orders.parallelStream()
                    .filter(order -> Arrays.stream(props.getKeywords())
                            .noneMatch(w -> containsIgnoreCase(order.getDescription(), w)))
                    .collect(toList());
        }
        return orders;
    }

    private int orderComparator(Courier courier, Order o1, Order o2) {
        for (String sortField : props.getSortFields()) {
            switch (sortField.toUpperCase()) {
                case "DISTANCE":
                    int o1Distance = (int) (calculateDistance(courier.getLocation(), o1.getPickup()) / props.getDistanceStep());
                    int o2Distance = (int) (calculateDistance(courier.getLocation(), o2.getPickup()) / props.getDistanceStep());
                    if (o1Distance == o2Distance) break;
                    return o1Distance > o2Distance ? 1 : -1;
                case "VIP":
                    int byVip = o1.getVip().compareTo(o2.getVip());
                    if (byVip == 0) break;
                    return byVip;
                case "FOOD":
                    int byFood = o1.getFood().compareTo(o2.getFood());
                    if (byFood == 0) break;
                    return byFood;
            }
        }
        return 0;
    }

}
