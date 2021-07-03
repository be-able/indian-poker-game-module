package indian.poker.game.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignResponse {
	
	/**
	 * 자기 자신의 아이디
	 */
	private Long id;
	
	/**
	 * 생성된 토큰을 반환한다
	 */
	private String token;
	
	/**
	 * 에러 메시지를 설정한다
	 */
	private String resultMessage;
}
