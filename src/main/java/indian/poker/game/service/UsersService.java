package indian.poker.game.service;

import indian.poker.game.data.dto.user.AuthorizationDto;
import indian.poker.game.data.dto.user.SetAuthorizationDto;
import indian.poker.game.data.dto.user.SignResponse;
import indian.poker.game.data.sign.UserData;
import indian.poker.game.entity.UserEntity;
import indian.poker.game.error.LogicalException;
import indian.poker.game.error.LogicalExceptionType;
import indian.poker.game.repository.UsersRepository;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsersService {

  /**
   * 유저 정보를 저장하고 있는 레포지토리
   */
  private final UsersRepository usersRepository;

  /**
   * jwt 생성/검사기
   */
  private final JwtService jwtService;

  @Value("${user.root.email}")
  private String rootEmail = "USER_ROOT_EMAIL";

  @Value("${user.root.saved}")
  private String rootSaved = "USER_ROOT_SAVED";

  @Value("${user.root.name}")
  private String rootUserName = "USER_ROOT_NAME";

  /**
   * 생성자
   *
   * @param usersRepository 유저 레포지토리
   * @param jwtService      jwt 서비스
   */
  public UsersService(UsersRepository usersRepository,
      JwtService jwtService) {

    this.usersRepository = usersRepository;
    this.jwtService = jwtService;
  }

  public AuthorizationDto getUserAuthorization(long userID) {

    return this.usersRepository.findById(userID)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"))
        .toAuthorizationDto();
  }

  public Page<UserEntity> getUsers(Pageable pageRequest) {

    return this.usersRepository.findAll(pageRequest);
  }

  public void initRootUser() {

    Optional<UserEntity> optional = this.usersRepository.findByEmail(this.rootEmail);
    if (optional.isPresent()) {
      return;
    }

    this.usersRepository.save(UserEntity.builder()
        .writeInfo(true)
        .writeAuth(true)
        .readInfo(true)
        .readAuth(true)
        .name(this.rootUserName)
        .email(this.rootEmail)
        .saved(this.rootSaved)
        .build());
  }

  /**
   * 이메일을 가진 유저가 이미 있는가
   *
   * @param email 이메일
   * @return 있으면 true
   */
  public boolean isExistEmailUser(@Nullable String email) {

    return this.usersRepository.findByEmail(email).isPresent();
  }

  /**
   * 유저를 지운다
   *
   * @param email 이메일
   * @param saved 로그인키
   */
  public void removeUser(@Nullable String email,
      @Nullable String saved) throws LogicalException {

    UserEntity users = this.usersRepository.findByEmail(email)
        .orElseThrow(() -> new LogicalException(LogicalExceptionType.INVALID_EMAIL));
    if (users.getSaved().equals(saved)) {
      this.usersRepository.delete(users);
    } else {
      throw new LogicalException(LogicalExceptionType.INVALID_PASSWORD);
    }

  }

  public void setUserAuthorization(long userID, SetAuthorizationDto authorizationDto) {

    UserEntity entity = this.usersRepository.findById(userID)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"));

    entity.setReadAuth(ObjectUtils.defaultIfNull(authorizationDto.getReadAuth(), false));
    entity.setReadInfo(ObjectUtils.defaultIfNull(authorizationDto.getReadInfo(), false));
    entity.setWriteAuth(ObjectUtils.defaultIfNull(authorizationDto.getWriteAuth(), false));
    entity.setWriteInfo(ObjectUtils.defaultIfNull(authorizationDto.getWriteInfo(), false));

    this.usersRepository.save(entity);
  }

  /**
   * 로그인
   *
   * @param email 이메일
   * @param saved 로그인키
   * @return 로그인 리스폰스
   */
  @NonNull
  public SignResponse signIn(@NonNull String email,
      @NonNull String saved) throws LogicalException {

    if (StringUtils.isBlank(email)
        || StringUtils.isBlank(saved)) {
      throw new LogicalException(LogicalExceptionType.EMPTY_FIELD);
    }

    UserEntity users = this.usersRepository.findByEmail(email)
        .orElseThrow(() -> new LogicalException(LogicalExceptionType.INVALID_EMAIL));

    if (users.getSaved().equals(saved)) {
      return SignResponse.builder()
          .token(this.jwtService.createJwt(users.getId(), users.getAuthValue()))
          .resultMessage("succeed")
          .id(users.getId())
          .build();
    } else {
      throw new LogicalException(LogicalExceptionType.INVALID_PASSWORD);
    }

  }

  /**
   * 회원가입
   *
   * @param email 이메일
   * @param name  이름
   * @param saved 로그인키
   * @return 로그인 정보
   */
  @NonNull
  public SignResponse signUp(@NonNull String email,
      @NonNull String name,
      @NonNull String saved) throws LogicalException {

    if (StringUtils.isBlank(email)) {
      throw new LogicalException(LogicalExceptionType.EMPTY_FIELD_EMAIL);
    }

    if (StringUtils.isBlank(name)) {
      throw new LogicalException(LogicalExceptionType.EMPTY_FIELD_NAME);
    }

    if (StringUtils.isBlank(saved)) {
      throw new LogicalException(LogicalExceptionType.EMPTY_FIELD_PASSWORD);
    }

    UserData userDto = UserData.builder()
        .email(email)
        .name(name)
        .saved(saved)
        .build();

    UserData signedUser = signUpUser(userDto);

    return this.signIn(signedUser.getEmail(), signedUser.getSaved());
  }

  /**
   * 회원가입 동작
   *
   * @param userDto 유저
   * @return 회원가입한 유저
   */
  @NonNull
  private UserData signUpUser(@NonNull UserData userDto) throws LogicalException {

    String email = userDto.getEmail();
    if (this.isExistEmailUser(email)) {
      throw new LogicalException(LogicalExceptionType.ALREADY_EXIST_EMAIL);
    }
    UserEntity entity = this.usersRepository.save(userDto.toEntity());
    return entity.toData();
  }
}
