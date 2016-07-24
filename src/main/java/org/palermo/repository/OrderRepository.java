package org.palermo.repository;

import javax.persistence.LockModeType;

import org.palermo.entity.Order;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Order findByNumber(String number);
    
    @Query("from Order where number = :number")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Order lockByNumber(@Param("number") String number);
    
}
