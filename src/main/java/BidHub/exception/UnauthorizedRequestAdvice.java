package BidHub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class UnauthorizedRequestAdvice {

	@ResponseBody
	@ExceptionHandler(UnauthorizedRequestException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String notFoundHandler(UnauthorizedRequestException ex) {
		return ex.getMessage();
	}
}