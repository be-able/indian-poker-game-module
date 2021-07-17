package indian.poker.game.error;

import lombok.Getter;

public class LogicalException extends Exception {

  @Getter
  private final LogicalExceptionType messageType;

  public LogicalException(LogicalExceptionType message) {

    super(message.getMessage());
    this.messageType = message;
  }

  public LogicalException() {

    super();
    this.messageType = LogicalExceptionType.NONE;
  }

  public LogicalException(LogicalExceptionType message, Throwable cause) {

    super(message.getMessage(), cause);
    this.messageType = message;
  }

  public LogicalException(Throwable cause) {

    super(cause);
    this.messageType = LogicalExceptionType.NONE;
  }

  public LogicalException(LogicalExceptionType message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {

    super(message.getMessage(), cause, enableSuppression, writableStackTrace);
    this.messageType = message;
  }
}
