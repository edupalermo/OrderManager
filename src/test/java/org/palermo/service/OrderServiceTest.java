package org.palermo.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.palermo.Application;
import org.palermo.entity.Item;
import org.palermo.entity.Order;
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

    @Test
    public void testAddItemOnEmptyOrder() throws Exception {

        // given
        Order order = new Order();

        order.setNotes("Waiter Palermo");
        order.setReference("Table 23");

        Item item = new Item();
        item.setSku("prdct_1");
        item.setUnitPrice(10);
        item.setQuantity(1);

        // when

        Order persistedOrder = orderService.create(order);
        persistedOrder = orderService.addItem(persistedOrder, item);

        // then

        assertThat(persistedOrder.getItems(), hasSize(1));
    }

    @Test
    public void testAddItemSameSkuAndDifferentPrice() throws Exception {

        // given
        Order order = new Order();

        order.setNotes("Waiter Palermo");
        order.setReference("Table 23");

        Item item1 = new Item();
        item1.setSku("prdct_1");
        item1.setUnitPrice(10);
        item1.setQuantity(1);

        Item item2 = new Item();
        item2.setSku("prdct_1");
        item2.setUnitPrice(15);
        item2.setQuantity(1);

        // when
        Order persistedOrder = orderService.create(order);

        persistedOrder = orderService.addItem(persistedOrder, item1);
        persistedOrder = orderService.addItem(persistedOrder, item2);

        // then
        assertThat(persistedOrder.getItems(), hasSize(2));
    }

    @Test
    public void testAddItemSameSkuAndSamePrice() throws Exception {

        // given
        Order order = new Order();

        order.setNotes("Waiter Palermo");
        order.setReference("Table 23");

        Item item1 = new Item();
        item1.setSku("prdct_1");
        item1.setUnitPrice(10);
        item1.setQuantity(1);

        Item item2 = new Item();
        item2.setSku("prdct_1");
        item2.setUnitPrice(10);
        item2.setQuantity(1);

        // when
        Order persistedOrder = orderService.create(order);

        persistedOrder = orderService.addItem(persistedOrder, item1);
        persistedOrder = orderService.addItem(persistedOrder, item2);

        // then
        assertThat(persistedOrder.getItems(), hasSize(1));
        assertThat(persistedOrder.getItems().get(0).getQuantity(), equalTo(2));
    }

}
