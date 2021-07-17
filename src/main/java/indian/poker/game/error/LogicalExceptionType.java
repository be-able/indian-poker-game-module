package indian.poker.game.error;

import lombok.Getter;

public enum LogicalExceptionType {
  NONE("잘못된 형식입니다"),
  EMPTY_FIELD("정보가 비어있습니다"),
  EMPTY_FIELD_EMAIL("이메일이 비어있습니다"),
  EMPTY_FIELD_NAME("이름이 비어있습니다"),
  EMPTY_FIELD_PASSWORD("비밀번호가 비어있습니다"),
  ALREADY_EXIST_EMAIL("이미 존재하는 계정입니다"),
  INVALID_EMAIL("존재하지 않는 계정입니다"),
  INVALID_PASSWORD("잘못된 비밀번호 입니다");

  @Getter
  private final String message;

  LogicalExceptionType(String message) {
    this.message = message;
  }
}
