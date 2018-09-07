package com.glovoapp.backender;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
class OrderRepository {
    private static final String ORDERS_FILE = "/orders.json";
    private static final List<Order> orders;
    private static long count;
    private static long nonFoodCount;

    static {
        try (Reader reader = new InputStreamReader(OrderRepository.class.getResourceAsStream(ORDERS_FILE))) {
            Type type = new TypeToken<List<Order>>() {
            }.getType();
            orders = new Gson().fromJson(reader, type);
            count = orders.size();
            nonFoodCount = orders.stream().filter(o -> !o.getFood()).toArray().length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    long getNonFoodCount() {
        return nonFoodCount;
    }

    long getCount() {
        return count;
    }

}
