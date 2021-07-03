package indian.poker.game.data.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 반환을 위한 유틸 클래스
 */
public final class Response {

  /**
   * private 생성자
   */
  private Response() {

    super();
  }

  /**
   * Json 형태로 성공을 반환합니다
   *
   * @return ResponseEntity
   */
  public static ResponseEntity<String> ok() {

    return statusJson(HttpStatus.OK, "succeed");
  }

  /**
   * 일반적인 형태로 성공을 반환합니다
   *
   * @return ResponseEntity
   */
  public static <T> ResponseEntity<T> ok(T model, MediaType type) {

    return statusType(HttpStatus.OK, model, type);
  }

  /**
   * Json 형태로 모델을 반환합니다
   *
   * @param model 모델
   * @param <T>   모델 타입
   * @return ResponseEntity
   */
  public static <T> ResponseEntity<T> okJson(T model) {

    return statusJson(HttpStatus.OK, model);
  }

  /**
   * Json 형태로 모델을 반환합니다
   *
   * @param status 상태
   * @param model  모델
   * @param <T>    모델 타입
   * @return ResponseEntity
   */
  public static <T> ResponseEntity<T> statusJson(HttpStatus status, T model) {

    return statusType(status, model, MediaType.APPLICATION_JSON);
  }

  /**
   * Json 형태로 모델을 반환합니다
   *
   * @param status 상태
   * @param model  모델
   * @param <T>    모델 타입
   * @return ResponseEntity
   */
  public static <T> ResponseEntity<T> statusType(HttpStatus status, T model, MediaType type) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(type);

    return ResponseEntity.status(status)
        .headers(headers)
        .body(model);
  }
}
