package project.br.useAuthentication.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import project.br.useAuthentication.exception.BadRequestExceptionResult;
import project.br.useAuthentication.exception.ExpiredJwtExceptionResult;
import project.br.useAuthentication.exception.InternalExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.exception.RefreshTokenException;
import project.br.useAuthentication.format.ErrorResponseResult;
import project.br.useAuthentication.format.StatusResult;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
		
	@ExceptionHandler(BadRequestExceptionResult.class)
	public final ResponseEntity<?> handleBadRequestExceptions(BadRequestExceptionResult message) {
		ErrorResponseResult<String> error = new ErrorResponseResult<String>(message.getLocalizedMessage(), 
				HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<ErrorResponseResult<String>>(error, HttpStatus.BAD_REQUEST);
	}
	
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
	
	@ExceptionHandler(RefreshTokenException.class)
	public final ResponseEntity<?> handleRefreshTokenExceptions(RefreshTokenException message) {
		StatusResult<String> token = new StatusResult<String>(HttpStatus.ACCEPTED.value(),
				message.getLocalizedMessage());
		return new ResponseEntity<StatusResult<String>>(token, HttpStatus.ACCEPTED);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<?> handleAuthenticationExceptions(AuthenticationException message) {
		//ErrorResponseResult<String> error = new ErrorResponseResult<String>(message.getLocalizedMessage(),
		ErrorResponseResult<String> error = new ErrorResponseResult<String>("Expired Token",
				HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<ErrorResponseResult<String>>(error, HttpStatus.UNAUTHORIZED);
	}
	
	/*@ExceptionHandler(AuthenticationExceptionResponse.class)
	public final ResponseEntity<?> handleAuthenticationExceptions(AuthenticationExceptionResponse message) {
		//ErrorResponseResult<String> error = new ErrorResponseResult<String>(message.getLocalizedMessage(),
		ErrorResponseResult<String> error = new ErrorResponseResult<String>("Expired Token",
				HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<ErrorResponseResult<String>>(error, HttpStatus.UNAUTHORIZED);
	}*/
	
	@ExceptionHandler(ExpiredJwtExceptionResult.class)
	public final ResponseEntity<?> handleExpiredJwtExceptions(ExpiredJwtExceptionResult message) {
		ErrorResponseResult<String> error = new ErrorResponseResult<String>(message.getLocalizedMessage(),
				HttpStatus.UNAUTHORIZED.value());
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







