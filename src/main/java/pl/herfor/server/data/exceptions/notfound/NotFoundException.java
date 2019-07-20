package pl.herfor.server.data.exceptions.notfound;

public class NotFoundException extends RuntimeException {

    NotFoundException(String objectName, String id) {
        super(String.format("Could not find %s %s", objectName, id));
    }
}