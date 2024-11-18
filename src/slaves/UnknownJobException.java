package slaves;

public class UnknownJobException extends RuntimeException {
	public UnknownJobException() {
		super("Unknown job");
	}
}
