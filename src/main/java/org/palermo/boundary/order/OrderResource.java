package org.palermo.boundary.order;

import org.palermo.entity.Order;
import org.palermo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/service")
public class OrderResource  {
    
    @Autowired OrderService orderService;
    
    @RequestMapping(method = RequestMethod.POST, value="/order")
    public ResponseEntity<Void> create(@RequestBody(required=false) Order order) {
        ResponseEntity<Void> responseEntity = null;
        Order persistedOrder = orderService.findByNumber(order.getNumber());
        
        if (persistedOrder != null) {
            responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        else {
            orderService.save(order);
            responseEntity = new ResponseEntity<Void>(HttpStatus.CREATED);
        }
        
        return responseEntity;
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/order/{number}")
    public ResponseEntity<?> read(@PathVariable String number) {
        ResponseEntity<?> responseEntity = null;
        
        Order order = orderService.findByNumber(number);
        if (order == null) {
            responseEntity = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        else {
            responseEntity = new ResponseEntity<Order>(order, HttpStatus.OK);
        }
        
        System.out.println(order);
        
        return responseEntity;
    }
    
}

