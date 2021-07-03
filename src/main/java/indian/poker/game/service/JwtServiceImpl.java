package indian.poker.game.service;

import indian.poker.game.data.sign.JwtData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@PropertySource(value = "classpath:application.yml")
public class JwtServiceImpl implements JwtService {

  private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

  private static final String KEY_UID = "user";

  private static final String KEY_AUTHOR = "auth";

  @Value("${env.SECRET_KEY}")
  private String secretKey = "THIS_IS_TEST_KEY";

  private byte[] secretKeyByteArray = null;

  /**
   * 널이면 시간이 만료되었거나 유효하지 않은 토큰인 것이다
   *
   * @param token 토큰
   * @return 토큰
   */
  @Override
  @NonNull
  public JwtData checkJwt(@Nullable String token) {

    if (StringUtils.isBlank(token) || StringUtils.isBlank(this.secretKey)) {

      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유효하지 않은 토큰입니다");
    }

    Claims claims;
    try {
      claims = Jwts.parser()
          .setSigningKey(getSecretKeyByteArray())
          .parseClaimsJws(token)
          .getBody();

    } catch (ExpiredJwtException exception) {
      log.info("만료된 토큰이 체크를 시도했습니다. token : " + token);
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "만료된 토큰입니다");
    } catch (JwtException exception) {
      log.error("변조된 토큰이 체크를 시도했습니다. message : " + exception.getMessage());
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유효하지 않은 토큰입니다");
    }

    if (claims == null) {

      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 토큰입니다");
    }

    return JwtData.builder()
        .expired(claims.getExpiration())
        .uid(this.getLongValueToClaims(claims, KEY_UID))
        .auth(this.getLongValueToClaims(claims, KEY_AUTHOR))
        .build();
  }

  /**
   * 토큰을 생성한다
   *
   * @param author 유저 권한
   * @param uid    접속 유저
   * @return 토큰
   */
  @Override
  @Nullable
  public String createJwt(long uid, long author) {

    Date expireTime = new Date();
    expireTime.setTime(expireTime.getTime() + 1000 * 60 * 1440); // 1440분 = 하루

    JwtData jwtData = JwtData.builder()
        .expired(expireTime)
        .auth(author)
        .uid(uid)
        .build();

    Map<String, Object> map = new HashMap<>();
    map.put(KEY_UID, jwtData.getUid());
    map.put(KEY_AUTHOR, jwtData.getAuth());

    Map<String, Object> headerMap = new ConcurrentHashMap<>();
    headerMap.put("typ", "JWT");
    headerMap.put("alg", "HS256");

    Key signingKey = new SecretKeySpec(getSecretKeyByteArray(), ALGORITHM.getJcaName());

    return Jwts.builder().setHeader(headerMap)
        .setClaims(map)
        .setExpiration(jwtData.getExpired())
        .signWith(ALGORITHM, signingKey)
        .compact();
  }

  private long getLongValueToClaims(Claims claims, String keyUid) {

    Object object = claims.get(keyUid);
    long value;
    if (object instanceof Integer) {
      value = (Integer) object;
    } else if (object instanceof Long) {
      value = (Long) object;
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다");
    }
    return value;
  }

  private byte[] getSecretKeyByteArray() {

    if (this.secretKeyByteArray == null) {
      this.secretKeyByteArray = DatatypeConverter.parseBase64Binary(this.secretKey);
    }
    return this.secretKeyByteArray;
  }
}

