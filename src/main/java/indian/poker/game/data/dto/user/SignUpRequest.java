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
public class SignUpRequest {

  /**
   * 로그인키
   */
  @NotBlank
  private String saved;

  /**
   * 이메일
   */
  @NotBlank
  private String email;

  /**
   * 유저 이름
   */
  @NotBlank
  private String name;
}
