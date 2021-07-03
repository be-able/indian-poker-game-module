package indian.poker.game.data.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationDto {
    
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
}
