package org.palermo.service;

import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.palermo.entity.Order;
import org.palermo.entity.enums.OrderStatus;
import org.palermo.exception.DuplicatedEntityException;
import org.palermo.repository.ItemRepository;
import org.palermo.repository.OrderRepository;
import org.palermo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    public Order create(@NotNull(message = "A null order cannot be saved") Order order) throws DuplicatedEntityException {

        // The specification says that the number is aa optional field. But if it is null, we will generate a random value
        if (order.getNumber() == null) {
            order.setNumber(UUID.randomUUID().toString());
        } else {
            Order persistedOrder = orderRepository.findByNumber(order.getNumber());

            if (persistedOrder != null) {
                throw new DuplicatedEntityException(String.format("Order with number[%s] aleady existis!", order.getNumber()));
            }
        }
        
        order.setStatus(OrderStatus.DRAFT);

        return orderRepository.save(order);
    }

    public Order findByNumber(String number) {
        return orderRepository.findByNumber(number);
    }

}
