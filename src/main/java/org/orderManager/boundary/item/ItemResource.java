package org.orderManager.boundary.item;

import org.orderManager.boundary.helper.RestExceptionHelper;
import org.orderManager.entity.Item;
import org.orderManager.service.ItemService;
import org.orderManager.service.OrderService;
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
    public ResponseEntity<?> create(@PathVariable("orderNumber") String orderNumber, @RequestBody(required=false) Item item) throws Exception {
        
        ResponseEntity<?> responseEntity = null;
        
        try {
        	
			itemService.add(orderNumber, item);
			responseEntity = new ResponseEntity<Void>(HttpStatus.CREATED);
			
		} catch (Exception e) {
			responseEntity = RestExceptionHelper.treatException(e);
		}
        
        return responseEntity;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/{sku}/unitPrice/{unitPrice}/quantity/{quantity}")
    public ResponseEntity<?> delete(@PathVariable("orderNumber") String orderNumber, @PathVariable("sku") String sku, @PathVariable("unitPrice") int unitPrice, @PathVariable("quantity") int quantity) throws Exception {
        
        ResponseEntity<?> responseEntity = null;
        
        try {
        	
			itemService.remove(orderNumber, sku, unitPrice, quantity);
			responseEntity = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			responseEntity = RestExceptionHelper.treatException(e);
		}
        
        
        return responseEntity;
    }
    

}
