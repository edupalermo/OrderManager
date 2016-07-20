package org.palermo.boundary.order;

import org.palermo.boundary.order.bean.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("service")
public class OrderResource  {
    
    @RequestMapping(method = RequestMethod.POST, value="/order", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> authorize(@RequestBody(required=false) Order order) {
        System.out.println(order);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/order/{reference}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public Order get(@PathVariable String reference) {
        return null; 
    }
    
    
}

