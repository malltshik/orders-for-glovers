package com.glovoapp.backender;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.glovoapp.backender.DistanceCalculator.calculateDistance;
import static com.glovoapp.backender.Vehicle.BICYCLE;
import static com.glovoapp.backender.Vehicle.MOTORCYCLE;
import static junit.framework.TestCase.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

/**
 * Ordering service test.
 * Data for this tests provided with .json files in resources
 * Properties for filtering and ordering provided with spring properties (look application-test.properties)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    private final static Location LOCATION = new Location(41.39980694963036, 2.1829698538474984);
    private final static Courier COURIER = new Courier().withId("ID").withName("NAME")
            .withBox(false).withLocation(LOCATION).withVehicle(BICYCLE);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProperties orderProperties;

    @Test
    public void findAllKeywordsTest() {
        // Orders without keywords in description
        List<Order> all = orderService.findAll(COURIER.withBox(false));
        assertFalse(all.isEmpty());
        assertFalse(all.stream().anyMatch(x -> StringUtils.containsIgnoreCase(x.getDescription(), "pizza")));
        // Opposite case
        all = orderService.findAll(COURIER.withBox(true));
        assertFalse(all.isEmpty());
        assertTrue(all.stream().anyMatch(x -> StringUtils.containsIgnoreCase(x.getDescription(), "pizza")));
    }

    @Test
    public void findAllDistanceTest() {
        // Orders close then 2 kilometers for BICYCLE
        List<Order> all = orderService.findAll(COURIER.withVehicle(BICYCLE));
        assertFalse(all.isEmpty());
        assertFalse(all.stream().anyMatch(x -> calculateDistance(x.getPickup(), COURIER.getLocation()) > 2));
        // All orders for MOTORCYCLE
        all = orderService.findAll(COURIER.withVehicle(MOTORCYCLE));
        assertFalse(all.isEmpty());
        assertTrue(all.stream().anyMatch(x -> calculateDistance(x.getPickup(), COURIER.getLocation()) > 2));
    }

    @Test
    public void findAllOrderingTest() {
        List<Order> all = orderService.findAll(COURIER.withVehicle(MOTORCYCLE).withBox(true));
        assertFalse(all.isEmpty());
        assertThat(all, hasSize(7));
        // DISTANCE,FOOD,VIP - ordering
        assertEquals(all.get(0).getId(), "order-1");
        assertEquals(all.get(1).getId(), "order-2");
        assertEquals(all.get(2).getId(), "order-3");
        assertEquals(all.get(3).getId(), "order-5");
        assertEquals(all.get(4).getId(), "order-7");
        assertEquals(all.get(5).getId(), "order-6");
        assertEquals(all.get(6).getId(), "order-4");
    }

    @Test
    public void findAllOrdering2Test() {
        // Change order fields priority
        String[] originSortFields = orderProperties.getSortFields();
        orderProperties.setSortFields(new String[]{"FOOD", "VIP", "DISTANCE"});
        // Query
        List<Order> all = orderService.findAll(COURIER.withVehicle(MOTORCYCLE).withBox(true));
        // Change fields priority back for other tests
        orderProperties.setSortFields(originSortFields);

        assertFalse(all.isEmpty());
        assertThat(all, hasSize(7));
        // FOOD,VIP,DISTANCE - ordering
        assertEquals(all.get(0).getId(), "order-7");
        assertEquals(all.get(1).getId(), "order-1");
        assertEquals(all.get(2).getId(), "order-2");
        assertEquals(all.get(3).getId(), "order-3");
        assertEquals(all.get(4).getId(), "order-4");
        assertEquals(all.get(5).getId(), "order-5");
        assertEquals(all.get(6).getId(), "order-6");
    }

    @Test
    public void findAllOrdering3Test() {
        // Change order fields priority
        String[] originSortFields = orderProperties.getSortFields();
        orderProperties.setSortFields(new String[]{"VIP", "DISTANCE", "FOOD"});
        // Query
        List<Order> all = orderService.findAll(COURIER.withVehicle(MOTORCYCLE).withBox(true));
        // Change fields priority back for other tests
        orderProperties.setSortFields(originSortFields);

        assertFalse(all.isEmpty());
        assertThat(all, hasSize(7));
        // FOOD,VIP,DISTANCE - ordering
        assertEquals(all.get(0).getId(), "order-3");
        assertEquals(all.get(1).getId(), "order-7");
        assertEquals(all.get(2).getId(), "order-4");
        assertEquals(all.get(3).getId(), "order-1");
        assertEquals(all.get(4).getId(), "order-2");
        assertEquals(all.get(5).getId(), "order-5");
        assertEquals(all.get(6).getId(), "order-6");
    }

}