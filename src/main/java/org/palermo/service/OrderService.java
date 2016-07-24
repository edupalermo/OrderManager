package org.palermo.service;

import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.palermo.entity.Item;
import org.palermo.entity.Order;
import org.palermo.exception.DuplicatedEntityException;
import org.palermo.repository.ItemRepository;
import org.palermo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class OrderService {
    
    @Autowired OrderRepository orderRepository;
    
    @Autowired ItemRepository itemRepository;

    @Transactional
    public Order save(@NotNull(message="A null order cannot be saved") Order order) throws DuplicatedEntityException {
    	
    	// The specification says that the number is aa optional field. But if it is null, we will generate a random value
    	if (order.getNumber() == null) {
    		order.setNumber(UUID.randomUUID().toString());
    	}
    	else {
        	Order persistedOrder = orderRepository.findByNumber(order.getNumber());
        	
        	if (persistedOrder != null) {
        		throw new DuplicatedEntityException(String.format("Order with number[%s] aleady existis!", order.getNumber()));
        	}
    	}
    	
        return orderRepository.save(order);
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
