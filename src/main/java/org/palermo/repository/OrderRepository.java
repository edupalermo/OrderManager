package org.palermo.repository;

import javax.persistence.LockModeType;

import org.palermo.entity.Order;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Order findByNumber(String number);
    
}
