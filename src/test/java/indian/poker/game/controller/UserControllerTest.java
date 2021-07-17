package indian.poker.game.controller;

import indian.poker.game.data.dto.user.AuthorizationDto;
import indian.poker.game.data.dto.user.SetAuthorizationDto;
import indian.poker.game.data.dto.user.SignInRequest;
import indian.poker.game.data.dto.user.SignResponse;
import indian.poker.game.data.dto.user.SignUpRequest;
import indian.poker.game.data.dto.user.UserDto;
import indian.poker.game.error.LogicalException;
import indian.poker.game.error.LogicalExceptionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@ActiveProfiles("test")
final class UserControllerTest {

  private static String token;
  private static long userId;

  @Autowired
  private UserController userController;

  @Value("${user.root.email}")
  private String rootEmail = "USER_ROOT_EMAIL";

  @Value("${user.root.saved}")
  private String rootSaved = "USER_ROOT_SAVED";

  @Value("${user.root.name}")
  private String rootUserName = "USER_ROOT_NAME";

  @BeforeEach
  void signUp() {
    try {
      ResponseEntity<SignResponse> resultEntity = this.userController
          .trySignUp(SignUpRequest.builder()
              .email("email")
              .name("name")
              .saved("saved")
              .build());
      SignResponse body = resultEntity.getBody();
      Assertions.assertTrue(true, "회원가입 성공");
      if (body == null) {
        throw new NullPointerException("회원가입에 실패 했습니다");
      } else {
        token = body.getToken();
        userId = body.getId();
      }
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.ALREADY_EXIST_EMAIL, e.getMessageType(),
          "재가입 실패 성공 검사");
    }
  }

  String getRootToken() {
    try {
      ResponseEntity<SignResponse> resultEntity = this.userController
          .trySignIn(SignInRequest.builder()
              .email(rootEmail)
              .saved(rootSaved)
              .build());
      SignResponse body = resultEntity.getBody();
      if (body == null) {
        Assertions.fail("루트 로그인에 실패 했습니다");
        return "";
      } else {
        return body.getToken();
      }
    } catch (LogicalException e) {
      Assertions.fail("루트 로그인에 실패 했습니다, " + e.getMessage());
      return "";
    }
  }

  @Test
  void trySignUp() {

    try {
      this.userController.trySignUp(SignUpRequest.builder()
          .email("email")
          .name("name")
          .saved("saved")
          .build());
      Assertions.fail("재가입 하려는 회원이므로 회원가입에 성공하면 안됩니다");
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.ALREADY_EXIST_EMAIL, e.getMessageType(),
          "재가입 실패 성공 검사");
    }

    try {
      this.userController.trySignUp(SignUpRequest.builder()
          .email("email")
          .name("name")
          .saved("savedfdshafjkdsa")
          .build());
      Assertions.fail("재가입 하려는 회원이므로 회원가입에 성공하면 안됩니다");
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.ALREADY_EXIST_EMAIL, e.getMessageType(),
          "재가입 실패 성공 검사");
    }

    try {
      this.userController.trySignUp(SignUpRequest.builder()
          .email("email")
          .name("namfdsajkfdhsae")
          .saved("savedffjkdsa")
          .build());
      Assertions.fail("재가입 하려는 회원이므로 회원가입에 성공하면 안됩니다");
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.ALREADY_EXIST_EMAIL, e.getMessageType(),
          "재가입 실패 성공 검사");
    }

    try {
      this.userController.trySignUp(SignUpRequest.builder()
          .email("")
          .name("namfdsajkfdhsae")
          .saved("savedffjkdsa")
          .build());
      Assertions.fail("이메일을 입력하지 않았으므로 성공하면 안됩니다");
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.EMPTY_FIELD_EMAIL, e.getMessageType(),
          "재가입 실패 성공 검사");
    }

    try {
      this.userController.trySignUp(SignUpRequest.builder()
          .email("email0")
          .name("")
          .saved("savedffjkdsa")
          .build());
      Assertions.fail("이름을 입력하지 않았으므로 성공하면 안됩니다");
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.EMPTY_FIELD_NAME, e.getMessageType(),
          "재가입 실패 성공 검사");
    }

    try {
      this.userController.trySignUp(SignUpRequest.builder()
          .email("email0")
          .name("namfdsajkfdhsae")
          .saved("")
          .build());
      Assertions.fail("비밀번호를 입력하지 않았으므로 성공하면 안됩니다");
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.EMPTY_FIELD_PASSWORD, e.getMessageType(),
          "재가입 실패 성공 검사");
    }
  }

  @Test
  void trySignIn() {

    signIn();

    try {
      this.userController.trySignIn(SignInRequest.builder()
          .email("email0")
          .saved("saved")
          .build());
      Assertions.fail("존재하지 않는 계정이므로 로그인에 성공하면 안됩니다");
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.INVALID_EMAIL, e.getMessageType(),
          "존재하지 않는 계정 로그인 검사");
    }

    try {
      this.userController.trySignIn(SignInRequest.builder()
          .email("email")
          .saved("savedfdsafdsa")
          .build());
      Assertions.fail("존재하는 계정이지만 비밀번호가 다르므로 로그인에 성공하면 안됩니다");
    } catch (LogicalException e) {
      Assertions.assertEquals(LogicalExceptionType.INVALID_PASSWORD, e.getMessageType(),
          "잘못된 비밀번호 로그인 검사");
    }
  }

  private void signIn() {
    try {
      ResponseEntity<SignResponse> resultEntity = this.userController
          .trySignIn(SignInRequest.builder()
              .email("email")
              .saved("saved")
              .build());
      SignResponse body = resultEntity.getBody();
      Assertions.assertTrue(true, "로그인 성공");
      if (body == null) {
        throw new NullPointerException("로그인에 실패 했습니다");
      } else {
        token = body.getToken();
        userId = body.getId();
      }
    } catch (LogicalException e) {
      e.printStackTrace();
      Assertions.fail("로그인 실패 : " + e.getMessage());
    }
  }

  @Test
  void getUserAuthorization() {

  }

  @Test
  void getUsers() {
    PageRequest page = PageRequest.of(0, 10);
    try {
      this.userController.getUsers("fdhsakljfdshafdsa", page);
    } catch (ResponseStatusException e) {
      Assertions.assertEquals(HttpStatus.FORBIDDEN, e.getStatus(), "토큰 에러 검사");
    }

    try {
      this.userController.getUsers(token, page);
      Assertions.fail("권한이 없기 때문에 실패");
    } catch (ResponseStatusException e) {
      Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus(), "권한 없음 검사");
    }

    try {
      this.userController.getUserAuthorization(token, userId);
    } catch (ResponseStatusException e) {
      Assertions.assertEquals(HttpStatus.FORBIDDEN, e.getStatus(), "권한 읽기 실패 검사");
    }

    try {
      this.userController.setUserAuthorization(token, userId, SetAuthorizationDto.builder()
          .readAuth(true)
          .writeAuth(true)
          .readInfo(true)
          .writeInfo(true)
          .build());
    } catch (ResponseStatusException e) {
      Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus(), "권한 수정 실패 검사");
    }

    try {
      String rootToken = getRootToken();
      if (rootToken.isBlank()) {
        return;
      }
      this.userController.setUserAuthorization(rootToken, userId, SetAuthorizationDto.builder()
          .readAuth(true)
          .writeAuth(true)
          .readInfo(true)
          .writeInfo(true)
          .build());
    } catch (ResponseStatusException e) {
      Assertions.fail("권한 수정 실패 : " + e.getMessage());
    }

    try {
      ResponseEntity<AuthorizationDto> auth = this.userController
          .getUserAuthorization(token, userId);
      AuthorizationDto body = auth.getBody();
      if (body == null) {
        Assertions.fail("권한 읽기 실패");
      } else {
        Assertions.assertTrue(body.isReadAuth(), "권한을 설정 했으므로 true 여야 한다");
        Assertions.assertTrue(body.isReadInfo(), "권한을 설정 했으므로 true 여야 한다");
        Assertions.assertTrue(body.isWriteAuth(), "권한을 설정 했으므로 true 여야 한다");
        Assertions.assertTrue(body.isWriteInfo(), "권한을 설정 했으므로 true 여야 한다");
      }
    } catch (ResponseStatusException e) {
      Assertions.fail("권한 읽기 실패");
    }

    signIn(); // 토큰 갱신

    try {
      ResponseEntity<Page<UserDto>> users = this.userController.getUsers(token, page);
      if (users.getBody() == null) {
        Assertions.fail("유저 검사 실패");
      } else {
        Assertions.assertFalse(users.getBody().isEmpty());
      }
    } catch (ResponseStatusException e) {
      Assertions.fail("유저 검사 실패 : " + e.getMessage());
    }
  }
}
