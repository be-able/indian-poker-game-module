package indian.poker.game.error;

public class LogicalException extends Exception {

  public LogicalException(String errorMessage) {

    super(errorMessage);
  }

  public LogicalException() {

    super();
  }

  public LogicalException(String message, Throwable cause) {

    super(message, cause);
  }

  public LogicalException(Throwable cause) {

    super(cause);
  }

  public LogicalException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {

    super(message, cause, enableSuppression, writableStackTrace);
  }
}
