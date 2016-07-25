package org.orderManager.service;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.orderManager.entity.Order;
import org.orderManager.entity.Transaction;
import org.orderManager.entity.enums.OrderStatus;
import org.orderManager.entity.enums.PaymentType;
import org.orderManager.repository.OrderRepository;
import org.orderManager.repository.TransactionRepository;
import org.orderManager.service.helper.PriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {
    
    @Autowired OrderRepository orderRepository;
    
    @Autowired TransactionRepository transactionRepository;
    
    @Transactional
    public Order add(Order order, Transaction transaction) {

        Order lockedOrder = orderRepository.lockByNumber(order.getNumber());

        transaction.setOrder(lockedOrder);
        lockedOrder.getTransactions().add(transactionRepository.save(transaction));

        PriceHelper.updateOrderPrice(lockedOrder);

        // If the Order have a price to pay then it mus be Entered
        if (lockedOrder.getPrice() > 0) {
            lockedOrder.setStatus(OrderStatus.ENTERED);
        }
        
        order.setUpdated(LocalDateTime.now());
        
        lockedOrder = orderRepository.save(lockedOrder);
        
        return lockedOrder;
    }

    
    @Transactional
    public Order cancel(Order order, Transaction transaction) {

        Order lockedOrder = orderRepository.lockByNumber(order.getNumber());
        
        
        boolean found = false;
        
        for (Transaction persistedTransaction : order.getTransactions()) {
            
            if (persistedTransaction.getExternalId().equals(transaction.getExternalId())) {
                persistedTransaction.setPaymentType(PaymentType.CANCEL);
                transactionRepository.save(persistedTransaction);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new EntityNotFoundException(String.format("Not found transaction with externalId [%s]!", transaction.getExternalId()));
        }
        
        PriceHelper.updateOrderPrice(lockedOrder);

        // If the Order have a price to pay then it mus be Entered
        if (lockedOrder.getPrice() > 0) {
            lockedOrder.setStatus(OrderStatus.ENTERED);
        }
        
        order.setUpdated(LocalDateTime.now());
        
        lockedOrder = orderRepository.save(lockedOrder);
        
        return lockedOrder;
    }
    


}
