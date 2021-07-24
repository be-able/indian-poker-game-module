package indian.poker.game.data.sign;

import indian.poker.game.utils.AuthUtils;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JwtData {

  /**
   * 익스파이어드 날짜
   */
  @NonNull
  private Date expired;

  /**
   * 이메일
   */
  @NonNull
  private Long uid;

  /**
   * 권한
   */
  @NonNull
  private Long auth;

  public boolean isAuthRead() {

    return AuthUtils.isAuthRead(this.auth);
  }

  public boolean isAuthWrite() {

    return AuthUtils.isAuthWrite(this.auth);
  }

  public boolean isInfoRead() {

    return AuthUtils.isInfoRead(this.auth);
  }

  public boolean isInfoWrite() {

    return AuthUtils.isInfoWrite(this.auth);
  }
}
