package org.orderManager.service;

import java.time.LocalDateTime;


import javax.transaction.Transactional;


import org.orderManager.entity.Order;
import org.orderManager.entity.Transaction;
import org.orderManager.entity.enums.PaymentType;
import org.orderManager.exception.DuplicatedEntityException;
import org.orderManager.exception.EntityNotFoundException;
import org.orderManager.repository.OrderRepository;
import org.orderManager.repository.TransactionRepository;
import org.orderManager.service.helper.OrderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {
    
    @Autowired OrderRepository orderRepository;
    
    @Autowired TransactionRepository transactionRepository;
    
    @Transactional
    public Order add(String orderNumber, Transaction transaction) throws EntityNotFoundException, DuplicatedEntityException {

        Order lockedOrder = orderRepository.lockByNumber(orderNumber);
        
        if (lockedOrder == null) {
        	throw new EntityNotFoundException(String.format("The system was unnable to find Order with number [%s]", orderNumber));
        }

        // Check if Transaction (externalId) already exists
        for (Transaction it : lockedOrder.getTransactions()) {
        	if (it.getExternalId().equals(transaction.getExternalId())) {
        		throw new DuplicatedEntityException(String.format("There is a transaction with the same externalId [%s]", transaction.getExternalId()));
        	}
        }

        transaction.setOrder(lockedOrder);
        lockedOrder.getTransactions().add(transactionRepository.save(transaction));

        OrderHelper.updateOrderPrice(lockedOrder);
        
        OrderHelper.treatOrderStatus(lockedOrder);
        
        lockedOrder = orderRepository.save(lockedOrder);
        
        lockedOrder = orderRepository.save(lockedOrder);
        
        return lockedOrder;
    }

    
    @Transactional
    public Order cancel(String orderNumber, String externalId) throws EntityNotFoundException {

        Order lockedOrder = orderRepository.lockByNumber(orderNumber);
        
        if (lockedOrder == null) {
        	throw new EntityNotFoundException(String.format("The system was unnable to find Order with number [%s]", orderNumber));
        }
        
        boolean found = false;
        
        for (Transaction persistedTransaction : lockedOrder.getTransactions()) {
            
            if (persistedTransaction.getExternalId().equals(externalId)) {
                persistedTransaction.setPaymentType(PaymentType.CANCEL);
                transactionRepository.save(persistedTransaction);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new EntityNotFoundException(String.format("Not found transaction with externalId [%s]!", externalId));
        }
        
        OrderHelper.updateOrderPrice(lockedOrder);

        OrderHelper.treatOrderStatus(lockedOrder);
        
        lockedOrder.setUpdated(LocalDateTime.now());
        
        lockedOrder = orderRepository.save(lockedOrder);
        
        return lockedOrder;
    }
    


}
