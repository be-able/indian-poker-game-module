package indian.poker.game.controller;

import indian.poker.game.data.common.Response;
import indian.poker.game.data.dto.user.AuthorizationDto;
import indian.poker.game.data.dto.user.SetAuthorizationDto;
import indian.poker.game.data.dto.user.SignInRequest;
import indian.poker.game.data.dto.user.SignResponse;
import indian.poker.game.data.dto.user.SignUpRequest;
import indian.poker.game.data.dto.user.UserDto;
import indian.poker.game.data.sign.JwtData;
import indian.poker.game.entity.UserEntity;
import indian.poker.game.error.LogicalException;
import indian.poker.game.service.JwtService;
import indian.poker.game.service.UsersService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

  private static final String UNAUTHORIZED_MESSAGE = "권한이 없습니다";

  private final UsersService usersService;

  private final JwtService jwtService;

  @GetMapping(value = "/users/{userID}/authorization")
  public ResponseEntity<AuthorizationDto> getUserAuthorization(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @PathVariable(value = "userID") long userID) {

    JwtData jwtData = this.jwtService.checkJwt(token);
    if (jwtData.isAuthRead() || jwtData.getUid() == userID) {
      return Response.okJson(this.usersService.getUserAuthorization(userID));
    }
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
  }


  @GetMapping(value = "/users")
  public ResponseEntity<Page<UserDto>> getUsers(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, Pageable pageRequest) {

    JwtData jwtData = this.jwtService.checkJwt(token);
    if (!jwtData.isAuthRead()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
    }
    return Response.okJson(this.usersService.getUsers(pageRequest).map(UserEntity::toDto));
  }

  @PutMapping(value = "/users/{userID}/authorization")
  public ResponseEntity<String> setUserAuthorization(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @PathVariable(value = "userID") long userID,
      @RequestBody SetAuthorizationDto authorizationDto) {

    if (!this.jwtService.checkJwt(token).isAuthWrite()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
    }

    this.usersService.setUserAuthorization(userID, authorizationDto);
    return Response.ok();
  }

  @PostMapping(path = "/user/sign-in")
  public ResponseEntity<SignResponse> trySignIn(@Valid @RequestBody SignInRequest request)
      throws LogicalException {

    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식 입니다");
    }

    return Response.okJson(this.usersService.signIn(request.getEmail(), request.getSaved()));
  }

  @PostMapping(path = "/user/sign-up")
  public ResponseEntity<SignResponse> trySignUp(@Valid @RequestBody SignUpRequest request)
      throws LogicalException {

    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식 입니다");
    }

    String email = request.getEmail();
    String name = request.getName();
    String saved = request.getSaved();
    return Response.okJson(this.usersService.signUp(email, name, saved));
  }
}
