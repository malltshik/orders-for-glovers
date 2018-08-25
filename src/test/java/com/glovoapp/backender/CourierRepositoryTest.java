package com.glovoapp.backender;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class CourierRepositoryTest {

    @Test
    public void findOneExisting() {
        Courier courier = new CourierRepository().findById("courier-1");
        Courier expected = new Courier().withId("courier-1")
                .withBox(true)
                .withName("Manolo Escobar")
                .withVehicle(Vehicle.MOTORCYCLE)
                .withLocation(new Location(41.3965463, 2.1963997));

        assertEquals(expected, courier);
    }

    @Test
    public void findOneNotExisting() {
        Courier courier = new CourierRepository().findById("bad-courier-id");
        assertNull(courier);
    }

    @Test
    public void findAll() {
        List<Courier> all = new CourierRepository().findAll();
        assertFalse(all.isEmpty());
    }
}