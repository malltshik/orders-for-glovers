package com.glovoapp.backender;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.glovoapp.backender.Vehicle.BICYCLE;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(API.class)
public class APITest {

    private final static Location LOCATION = new Location(12.12, 13.13);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CourierRepository courierRepository;


    @Before
    public void before() throws Exception {
        when(orderRepository.findAll()).thenReturn(singletonList(
                new Order().withId("ID").withDescription("DESC")
                        .withFood(true).withVip(false)
                        .withDelivery(LOCATION).withPickup(LOCATION)));

        when(courierRepository.findById(anyString())).then(i -> {
            String id = i.getArgument(0);
            if (id.equals("exist")) return new Courier().withId("exist")
                    .withBox(true).withVehicle(BICYCLE).withLocation(LOCATION).withName("NAME-exist");
            else return null;
        });
    }

    @Test
    public void homePageTest() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string("¡Hola!"));
    }

    @Test
    public void orders() throws Exception {
        this.mvc.perform(get("/orders")).andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":\"ID\",\"description\":\"DESC\"}]"));
    }

    @Test
    public void courierOrders() throws Exception {
        this.mvc.perform(get("/orders/exist")).andExpect(status().isOk());
        this.mvc.perform(get("/orders/other")).andExpect(status().isNotFound());
        this.mvc.perform(get("/orders/!#!$!@#")).andExpect(status().isNotFound());
    }

}