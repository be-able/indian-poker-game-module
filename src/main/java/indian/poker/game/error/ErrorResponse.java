package indian.poker.game.error;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

/**
 * 에러 리스폰스
 */
@AllArgsConstructor
public class ErrorResponse {
	
	/**
	 * 에러 시간
	 */
	@Getter
	private final String timestamp;
	
	/**
	 * 상태 코드
	 */
	@Getter
	private final int status;
	
	/**
	 * 에러 종류
	 */
	@Getter
	private final String error;
	
	/**
	 * 에러 메시지
	 */
	@Getter
	private final String message;
	
	/**
	 * 에러 경로
	 */
	@Getter
	private final String path;
	
	/**
	 * 생성자
	 *
	 * @param status  상태
	 * @param error   에러
	 * @param message 메시지
	 * @param path    경로
	 */
	@Builder
	public ErrorResponse(@NonNull HttpStatus status,
						 @NonNull String error,
						 @NonNull String message,
						 @NonNull String path) {
		
		this.timestamp = LocalDateTime.now().toString();
		this.status = status.value();
		this.error = error;
		this.message = message;
		this.path = path;
	}
}
