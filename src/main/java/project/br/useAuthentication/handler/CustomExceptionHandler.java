package project.br.useAuthentication.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;
import project.br.useAuthentication.exception.InternalExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.format.ErrorResponseResult;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
			
	@ExceptionHandler(InternalExceptionResult.class)
	public final ResponseEntity<?> handleAllExceptions(InternalExceptionResult message) {
		ErrorResponseResult<String> error = new ErrorResponseResult<String>(message.getLocalizedMessage(), 
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorResponseResult<String>>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public final ResponseEntity<?> handleIllegalExceptions(IllegalArgumentException message) {
		ErrorResponseResult<String> error = new ErrorResponseResult<String>(message.getLocalizedMessage(), 
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<ErrorResponseResult<String>>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AuthenticationExceptionResponse.class)
	public final ResponseEntity<?> handleExpiredJwtExceptions(AuthenticationExceptionResponse exception) {
		String message = "";
		if (exception.getLocalizedMessage() == JwtType.INVALID_USER.toString()) {
    		message = JwtType.INVALID_USER.getValue();
    	}
    	else if (exception.getLocalizedMessage() == JwtType.INVALID_AT.toString()) {
    		message = JwtType.INVALID_AT.getValue();
    	}
    	else if (exception.getLocalizedMessage() == JwtType.INVALID_RT.toString()) {
    		message = JwtType.INVALID_RT.getValue();
    	}
    	else if (exception.getLocalizedMessage() == JwtType.EXPIRED_AT.toString()) {
    		message = JwtType.EXPIRED_AT.getValue();
    	}
    	else if (exception.getLocalizedMessage() == JwtType.EXPIRED_RT.toString()) {
    		message = JwtType.EXPIRED_RT.getValue();
    	}
		ErrorResponseResult<String> error = new ErrorResponseResult<String>(message, HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<ErrorResponseResult<String>>(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<?> handleUsernameNotFoundExceptions(UsernameNotFoundException message){
		ErrorResponseResult<String> error = new ErrorResponseResult<String>(message.getLocalizedMessage(), 
				HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorResponseResult<String>>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NotFoundExceptionResult.class)
	public final ResponseEntity<?> handleNotFoundExceptions(NotFoundExceptionResult message) {
		ErrorResponseResult<String> error = new ErrorResponseResult<String>(message.getLocalizedMessage(), 
				HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorResponseResult<String>>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<?> handleValidationExceptions(ConstraintViolationException message) {
		List<String> lista = new ArrayList<String>();
	    for (ConstraintViolation<?> cv : message.getConstraintViolations()) {
	    	lista.add(String.format("%s", cv.getMessage()));
	    }
		ErrorResponseResult<List<String>> error = new ErrorResponseResult<List<String>>(lista, HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<ErrorResponseResult<List<String>>>(error, HttpStatus.BAD_REQUEST);
	}
}







