package org.palermo.service;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.palermo.entity.Item;
import org.palermo.entity.Order;
import org.palermo.entity.Transaction;
import org.palermo.entity.enums.OrderStatus;
import org.palermo.entity.enums.PaymentType;
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

    @Transactional
    public Order removeItem(final Order order, String sku, int unitPrice, int quantity) {

        Order lockedOrder = orderRepository.lockByNumber(order.getNumber());

        boolean found = false;

        for (Item persistedItem : order.getItems()) {
            if ((persistedItem.getSku().equals(sku)) && (persistedItem.getUnitPrice() == unitPrice)) {

                int actualQuantity = persistedItem.getQuantity();
                if (quantity >= actualQuantity) {
                    lockedOrder.getItems().remove(persistedItem);
                } else {
                    persistedItem.setQuantity(actualQuantity - quantity);
                }

                found = true;
                break;
            }
        }

        if (!found) {
            throw new EntityNotFoundException(String.format("It not found item with sku [%s] and unitPrice [%d]!", sku, unitPrice));
        }

        return lockedOrder;
    }

    private void updateOrderPrice(Order order) {
        int total = 0;

        for (Item item : order.getItems()) {
            total += item.getUnitPrice();
        }
        
        for (Transaction transaction: order.getTrasactions()) {
            if (transaction.getPaymentType() == PaymentType.PAYMENT) {
                total -= transaction.getAmount();
            }
            
        }
        order.setPrice(total);

    }

    @Transactional
    public Order addItem(Order order, Item item) {

        Order lockedOrder = orderRepository.lockByNumber(order.getNumber());

        boolean found = false;

        for (Item persistedItem : lockedOrder.getItems()) {
            if ((persistedItem.getSku().equals(item.getSku())) && (persistedItem.getUnitPrice() == item.getUnitPrice())) {

                persistedItem.addQuantity(item.getQuantity());
                itemRepository.save(persistedItem);

                found = true;
                break;
            }
        }

        if (!found) {
            item.setOrder(lockedOrder);
            lockedOrder.getItems().add(itemRepository.save(item));
        }

        updateOrderPrice(lockedOrder);

        // If the Order have a price to pay then it mus be Entered
        if (lockedOrder.getPrice() > 0) {
            lockedOrder.setStatus(OrderStatus.ENTERED);
        }
        
        order.setUpdated(LocalDateTime.now());
        
        lockedOrder = orderRepository.save(lockedOrder);
        
        return lockedOrder;
    }
    
    
    @Transactional
    public Order addTransaction(Order order, Transaction transaction) {

        Order lockedOrder = orderRepository.lockByNumber(order.getNumber());

        transaction.setOrder(lockedOrder);
        lockedOrder.getTransactions().add(transactionRepository.save(transaction));

        updateOrderPrice(lockedOrder);

        // If the Order have a price to pay then it mus be Entered
        if (lockedOrder.getPrice() > 0) {
            lockedOrder.setStatus(OrderStatus.ENTERED);
        }
        
        order.setUpdated(LocalDateTime.now());
        
        lockedOrder = orderRepository.save(lockedOrder);
        
        return lockedOrder;
    }


}
