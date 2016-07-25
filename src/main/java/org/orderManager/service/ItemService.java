package org.orderManager.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.orderManager.entity.Item;
import org.orderManager.entity.Order;
import org.orderManager.exception.EntityNotFoundException;
import org.orderManager.repository.ItemRepository;
import org.orderManager.repository.OrderRepository;
import org.orderManager.service.helper.OrderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemService {
    
    @Autowired private ItemRepository itemRepository;
    
    @Autowired private OrderRepository orderRepository;
    
    
    @Transactional
    public Order remove(String orderNumber, String sku, int unitPrice, int quantity) throws EntityNotFoundException  {

        Order lockedOrder = orderRepository.lockByNumber(orderNumber);
        
        if (lockedOrder == null) {
        	throw new EntityNotFoundException(String.format("The system was unnable to find Order with number [%s]", orderNumber));
        }

        boolean found = false;

        for (Item persistedItem : lockedOrder.getItems()) {
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
            throw new EntityNotFoundException(String.format("The system not found item with sku [%s] and unitPrice [%d]!", sku, unitPrice));
        }
        
        OrderHelper.updateOrderPrice(lockedOrder);
        
        OrderHelper.treatOrderStatus(lockedOrder);
        
        lockedOrder.setUpdated(LocalDateTime.now());

        return lockedOrder;
    }

    @Transactional
    public Order add(String orderNumber, Item item) throws EntityNotFoundException {

        Order lockedOrder = orderRepository.lockByNumber(orderNumber);
        
        if (lockedOrder == null) {
        	throw new EntityNotFoundException(String.format("The system was unnable to find Order with number [%s]", orderNumber));
        }

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

        OrderHelper.updateOrderPrice(lockedOrder);

        OrderHelper.treatOrderStatus(lockedOrder);
        
        lockedOrder.setUpdated(LocalDateTime.now());
        
        lockedOrder = orderRepository.save(lockedOrder);
        
        return lockedOrder;
    }


}
