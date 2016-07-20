package org.palermo.repository;

import org.palermo.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Order findByNumber(String number);
    
}
