package org.palermo.service;

import javax.transaction.Transactional;

import org.palermo.entity.Item;
import org.palermo.entity.Order;
import org.palermo.entity.enums.OrderStatus;
import org.palermo.repository.ItemRepository;
import org.palermo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderService {
    
    @Autowired OrderRepository orderRepository;
    
    @Autowired ItemRepository itemRepository;
    
    @Transactional
    public void save(Order order) {
        order.setStatus(OrderStatus.DRAFT);
        orderRepository.save(order);
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
                
                
                found = true;
                break;
            }
        }

        
        
        
        if (item != null) {
            int actualQuantity = item.getQuantity();
            if (quantity >= actualQuantity) {
                // itemRepository.delete(item);
                lockedOrder.getItems().remove(item);
                itemsUpdated = true;
            }
            else {
                item.setQuantity(actualQuantity - quantity);
                // itemRepository.save(item);
                itemsUpdated = true;
            }
        }
        
        if (itemsUpdated) {
            updateOrderPrice(lockedOrder);
            lockedOrder = orderRepository.save(lockedOrder);
        }
        
        return lockedOrder;
    }
    
    private void updateOrderPrice(Order order) {
        int total = 0;
        
        for (Item item: order.getItems()) {
            total += item.getUnitPrice();
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
            itemRepository.save(item);
            
            updateOrderPrice(lockedOrder);
            lockedOrder = orderRepository.save(lockedOrder);
        }
        
        return lockedOrder;
    }
    
}
