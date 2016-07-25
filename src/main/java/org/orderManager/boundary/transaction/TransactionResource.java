package org.orderManager.boundary.transaction;

import javax.transaction.Transaction;

import org.orderManager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/service/order/{orderNumber}/transaction")
public class TransactionResource {
    
    @Autowired OrderService orderService;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@PathVariable("orderNumber") String orderNumber, @RequestBody(required=false) Transaction transaction) {
        
        ResponseEntity<Void> responseEntity = null;
        
        return responseEntity;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/externalId")
    public ResponseEntity<Void> delete(@PathVariable("orderNumber") String orderNumber, @PathVariable("externalId") String externalId) {
        
        return null;
        
    }


    
}
