package indian.poker.game.service;

import indian.poker.game.data.sign.JwtData;
import indian.poker.game.error.LogicalException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface JwtService {

  /**
   * 널이면 시간이 만료되었거나 유효하지 않은 토큰인 것이다
   *
   * @param token 토큰
   * @return 토큰
   */
  @NonNull
  JwtData checkJwt(@Nullable String token);

  /**
   * 토큰을 생성한다
   *
   * @param author 유저 권한
   * @param uid    접속 유저
   * @return 토큰
   */
  @Nullable
  String createJwt(long uid, long author) throws LogicalException;

}

