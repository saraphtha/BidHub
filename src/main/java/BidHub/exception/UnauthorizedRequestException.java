package BidHub.exception;

public class UnauthorizedRequestException extends RuntimeException {

	public UnauthorizedRequestException(String msg) {
		super(msg);
	}
}