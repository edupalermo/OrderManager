package org.palermo.service;

import javax.transaction.Transactional;

import org.palermo.entity.Item;
import org.palermo.entity.Order;
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
        orderRepository.save(order);
    }

    public Order findByNumber(String number) {
        return orderRepository.findByNumber(number);
    }

    @Transactional
    public void removeItem(Order order, String sku, int quantity) {
        
        Item item = itemRepository.findByOrderAndSku(order, sku);
        
        boolean itemsUpdated = false;
        
        if (item != null) {
            int actualQuantity = item.getQuantity();
            if (quantity >= actualQuantity) {
                // itemRepository.delete(item);
                order.getItems().remove(item);
                itemsUpdated = true;
            }
            else {
                item.setQuantity(actualQuantity - quantity);
                // itemRepository.save(item);
                itemsUpdated = true;
            }
        }
        
        if (itemsUpdated) {
            updateOrderPrice(order);
            orderRepository.save(order);
        }
        
    }
    
    private void updateOrderPrice(Order order) {
        int total = 0;
        
        for (Item item: order.getItems()) {
            total += item.getUnitPrice();
        }
        order.setPrice(total);
        
    }

    @Transactional
    public void addItem(Order order, Item item) {
        
        boolean found = false;
        
        for (Item persistedItem : order.getItems()) {
            if (persistedItem.getSku().equals(item.getSku())) {
                
                persistedItem.addQuantity(item.getQuantity());
                itemRepository.save(persistedItem);
                
                found = true;
                break;
            }
        }

        if (!found) {
            item.setOrder(order);
            itemRepository.save(item);
            
            updateOrderPrice(order);
            orderRepository.save(order);
        }
    }
    
}
