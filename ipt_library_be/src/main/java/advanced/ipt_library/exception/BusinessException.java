package advanced.ipt_library.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

  private String errorCode;

  public BusinessException(String message, String errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
