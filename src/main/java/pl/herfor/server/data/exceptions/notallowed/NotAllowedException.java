package pl.herfor.server.data.exceptions.notallowed;

public class NotAllowedException extends RuntimeException {
    NotAllowedException(String reason) {
        super(String.format("Action is not allowed due to following reason: %s", reason));
    }
}
