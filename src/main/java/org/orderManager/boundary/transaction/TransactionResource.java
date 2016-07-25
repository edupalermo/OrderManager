package org.orderManager.boundary.transaction;



import org.orderManager.boundary.helper.RestExceptionHelper;
import org.orderManager.entity.Transaction;
import org.orderManager.exception.EntityNotFoundException;
import org.orderManager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/service/order/{orderNumber}/transaction")
public class TransactionResource {
    
    @Autowired TransactionService transactionService;
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@PathVariable("orderNumber") String orderNumber, @RequestBody(required=false) Transaction transaction) throws Exception {
        
        ResponseEntity<?> responseEntity = null;
        
        try {
			transactionService.add(orderNumber, transaction);
			responseEntity = new ResponseEntity<Void>(HttpStatus.CREATED);
			
		} catch (Exception e) {
			responseEntity = RestExceptionHelper.treatException(e);
		}
        
        return responseEntity;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/externalId")
    public ResponseEntity<?> delete(@PathVariable("orderNumber") String orderNumber, @PathVariable("externalId") String externalId) throws Exception {
        
        ResponseEntity<?> responseEntity = null;
        
        try {
			transactionService.cancel(orderNumber, externalId);
			responseEntity = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			
		} catch (Exception e) {
			responseEntity = RestExceptionHelper.treatException(e);
		}
        
        return responseEntity;
        
    }


    
}
