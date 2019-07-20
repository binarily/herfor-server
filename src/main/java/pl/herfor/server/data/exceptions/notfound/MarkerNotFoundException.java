package pl.herfor.server.data.exceptions.notfound;

public class MarkerNotFoundException extends NotFoundException {
    public MarkerNotFoundException(String id) {
        super("marker", id);
    }
}
