package indian.poker.game.data.dto.user;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
	
	/**
	 * 로그인 이메일
	 */
	@NotBlank
	private String email;
	
	/**
	 * 로그인 키
	 */
	@NotBlank
	private String saved;
}
