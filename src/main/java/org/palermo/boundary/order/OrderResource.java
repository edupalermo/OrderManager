package org.palermo.boundary.order;

import org.palermo.boundary.helper.RestExceptionHelper;
import org.palermo.entity.Order;
import org.palermo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/service")
public class OrderResource {

	@Autowired
	OrderService orderService;

	@RequestMapping(method = RequestMethod.POST, value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> create(@RequestBody(required = false) Order order) throws Exception {
		ResponseEntity<?> responseEntity = null;

		try {
			responseEntity = new ResponseEntity<Order>(orderService.save(order), HttpStatus.OK);
			
		} catch (Exception e) {
			responseEntity = RestExceptionHelper.treatException(e);
		}

		return responseEntity;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/order/{number}")
	public ResponseEntity<?> read(@PathVariable String number) {
		ResponseEntity<?> responseEntity = null;

		Order order = orderService.findByNumber(number);
		if (order == null) {
			responseEntity = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			responseEntity = new ResponseEntity<Order>(order, HttpStatus.OK);
		}

		return responseEntity;
	}

}
