package org.orderManager.service;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.orderManager.entity.Item;
import org.orderManager.entity.Order;
import org.orderManager.entity.enums.OrderStatus;
import org.orderManager.repository.ItemRepository;
import org.orderManager.repository.OrderRepository;
import org.orderManager.service.helper.PriceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemService {
    
    @Autowired private ItemRepository itemRepository;
    
    @Autowired private OrderRepository orderRepository;
    
    
    @Transactional
    public Order remove(final Order order, String sku, int unitPrice, int quantity) {

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
        
        PriceHelper.updateOrderPrice(lockedOrder);

        return lockedOrder;
    }

    @Transactional
    public Order add(Order order, Item item) {

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
