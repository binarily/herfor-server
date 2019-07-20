package pl.herfor.server.data.exceptions.notallowed;

public class IncorrectLocationNotAlllowedException extends NotAllowedException {
    public IncorrectLocationNotAlllowedException() {
        super("The provided set of coordinates is incorrect.");
    }
}
