package org.orderManager.repository;

import org.orderManager.entity.Item;
import org.orderManager.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
    
    Item findByOrderAndSku(Order order, String sku);

}
