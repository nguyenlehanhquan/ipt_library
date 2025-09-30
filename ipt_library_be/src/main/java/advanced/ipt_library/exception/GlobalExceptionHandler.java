package advanced.ipt_library.exception;

import advanced.ipt_library.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(exception.getMessage(), exception.getErrorCode()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(exception.getMessage(), exception.getErrorCode()));
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {
//        log.error("Exception: ", exception);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse("Internal server error", "Uncategorized error"));
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getAllErrors();
        var errorMessages = new ArrayList<String>();
        for (var error : errors) {
            errorMessages.add(error.getDefaultMessage());
        }
        var resultMessages = String.join(", ", errorMessages);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(resultMessages, null));
    }

}
