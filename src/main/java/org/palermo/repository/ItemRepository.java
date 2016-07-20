package org.palermo.repository;

import org.palermo.entity.Item;
import org.palermo.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
    
    Item findByOrderAndSku(Order order, String sku);

}
