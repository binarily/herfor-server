package pl.herfor.server.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.herfor.server.data.exceptions.notallowed.NotAllowedException;
import pl.herfor.server.data.exceptions.notfound.NotFoundException;

@ControllerAdvice
class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String notAllowedHandler(NotAllowedException ex) {
        return ex.getMessage();
    }
}