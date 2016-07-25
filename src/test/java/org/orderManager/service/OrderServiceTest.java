package org.orderManager.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.orderManager.Application;
import org.orderManager.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testNullOrderNumberMustBeCreated() throws Exception {

        // given
        Order order = new Order();

        order.setNotes("Waiter Palermo");
        order.setReference("Table 23");
        order.setNumber(null);

        // when
        Order persistedOrder = orderService.create(order);

        // then
        assertThat(persistedOrder.getNumber(), is(notNullValue()));
    }

}
