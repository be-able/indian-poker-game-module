package indian.poker.game.entity;

import indian.poker.game.data.dto.user.AuthorizationDto;
import indian.poker.game.data.dto.user.UserDto;
import indian.poker.game.data.sign.UserData;
import indian.poker.game.utils.AuthUtils;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {@Index(columnList = "email")})
public class UserEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String email;

  @Column
  private String saved;

  @Column
  private String name;

  @Column
  private boolean writeInfo;

  @Column
  private boolean readInfo;

  @Column
  private boolean writeAuth;

  @Column
  private boolean readAuth;

  public long getAuthValue() {

    long value = 0;
    value = AuthUtils.setInfoWrite(value, this.writeInfo);
    value = AuthUtils.setInfoRead(value, this.readInfo);
    value = AuthUtils.setAuthWrite(value, this.writeAuth);
    value = AuthUtils.setAuthRead(value, this.readAuth);
    return value;
  }

  public AuthorizationDto toAuthorizationDto() {

    return AuthorizationDto.builder()
        .readAuth(this.readAuth)
        .readInfo(this.readInfo)
        .writeAuth(this.writeAuth)
        .writeInfo(this.writeInfo).build();
  }

  public UserData toData() {

    return UserData.builder()
        .email(this.email)
        .saved(this.saved)
        .name(this.name)
        .readAuth(this.readAuth)
        .readInfo(this.readInfo)
        .uid(this.id)
        .writeAuth(this.writeAuth)
        .writeInfo(this.writeInfo)
        .build();
  }

  public UserDto toDto() {

    return UserDto.builder()
        .email(this.email)
        .name(this.name)
        .author(this.toAuthorizationDto())
        .uid(this.id)
        .build();
  }

}
