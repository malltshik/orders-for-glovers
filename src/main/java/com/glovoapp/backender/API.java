package com.glovoapp.backender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API controller with method which provide orders (filtered and sorted for special courier)
 */
@Controller
class API {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final CourierRepository courierRepository;
    private final StatsService statsService;

    @Autowired
    API(OrderRepository orderRepository, OrderService orderService, CourierRepository courierRepository, StatsService statsService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.courierRepository = courierRepository;
        this.statsService = statsService;
    }

    @RequestMapping("/")
    @ResponseBody
    String root() {
        return "¡Hola!";
    }

    @RequestMapping("/orders")
    @ResponseBody
    List<OrderVM> orders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> new OrderVM(order.getId(), order.getDescription()))
                .collect(Collectors.toList());
    }

    @RequestMapping("/orders/{courierId}")
    @ResponseBody
    ResponseEntity<List<OrderVM>> courierOrders(@PathVariable("courierId") String courierId) {
        Courier courier = courierRepository.findById(courierId);
        if (courier == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(orderService.findAll(courier).stream()
                .map(order -> new OrderVM(order.getId(), order.getDescription()))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping("/stats")
    @ResponseBody
    OrderStats getStats() {
        return statsService.getStats();
    }

}
