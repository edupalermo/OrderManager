package org.orderManager.boundary.helper;

import javax.validation.ConstraintViolationException;

import org.orderManager.exception.DuplicatedEntityException;
import org.orderManager.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestExceptionHelper {
	
	private static Logger logger = LoggerFactory.getLogger(RestExceptionHelper.class);
	
	public static ResponseEntity<?> treatException(Exception e) throws Exception {
		ResponseEntity<?> responseEntity = null;

		if (e instanceof DuplicatedEntityException) {
			logger.error(e.getMessage());
			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} else if (e instanceof EntityNotFoundException) {
			logger.error(e.getMessage());
			responseEntity = new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		else if (e instanceof ConstraintViolationException) {
			logger.error(((ConstraintViolationException) e).getConstraintViolations().iterator().next().getMessage());
			responseEntity = new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		else {
			throw e;
		}
		
		return responseEntity;
	}

}
