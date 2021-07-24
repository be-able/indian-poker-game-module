package indian.poker.game.data.sign;

import indian.poker.game.entity.UserEntity;
import indian.poker.game.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserData {

  @NonNull
  private final String email;

  @NonNull
  private final String saved;

  @NonNull
  private final String name;

  private boolean writeInfo;

  private boolean readInfo;

  private boolean writeWeekly;

  private boolean readWeekly;

  private boolean writeBus;

  private boolean readBus;

  private boolean writeParking;

  private boolean readParking;

  private boolean writeAuth;

  private boolean readAuth;

  private Long uid;


  public long getAuthValue() {

    long value = 0;
    value = AuthUtils.setInfoWrite(value, this.writeInfo);
    value = AuthUtils.setInfoRead(value, this.readInfo);
    value = AuthUtils.setAuthWrite(value, this.writeAuth);
    value = AuthUtils.setAuthRead(value, this.readAuth);
    return value;
  }

  public UserEntity toEntity() {

    UserEntity userEntity = new UserEntity();
    userEntity.setEmail(this.email);
    userEntity.setName(this.name);
    userEntity.setSaved(this.saved);
    userEntity.setReadAuth(this.readAuth);
    userEntity.setReadInfo(this.readInfo);
    userEntity.setWriteAuth(this.writeAuth);
    userEntity.setWriteInfo(this.writeInfo);

    if (this.uid != null) {
      userEntity.setId(this.uid);
    }

    return userEntity;
  }
}
