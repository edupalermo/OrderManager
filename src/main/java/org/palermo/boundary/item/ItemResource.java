package org.palermo.boundary.item;

import org.palermo.entity.Item;
import org.palermo.entity.Order;
import org.palermo.service.ItemService;
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
@RequestMapping("/v1/service/order/{orderNumber}/item")
public class ItemResource {
    
    @Autowired OrderService orderService;
    
    @Autowired ItemService itemService;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@PathVariable("orderNumber") String orderNumber, @RequestBody(required=false) Item item) {
        
        ResponseEntity<Void> responseEntity = null;
        
        Order order = orderService.findByNumber(orderNumber);
        
        if (order == null) {
            responseEntity = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        else {
            item.setOrder(order);
            itemService.save(item);
        }
        
        return responseEntity;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/{sku}/quantity/{quantity}")
    public ResponseEntity<Void> delete(@PathVariable("orderNumber") String orderNumber, @PathVariable("sku") String sku, @PathVariable("quantity") int quantity) {
        
        ResponseEntity<Void> responseEntity = null;
        
        Order order = orderService.findByNumber(orderNumber);
        
        if (order == null) {
            responseEntity = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        else {
            orderService.removeItem(order, sku, quantity);
        }
        
        return responseEntity;
    }
    

}
