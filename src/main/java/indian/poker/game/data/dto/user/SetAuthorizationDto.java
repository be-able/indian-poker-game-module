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
public class SetAuthorizationDto {
    
  private Boolean writeInfo;

  private Boolean readInfo;

  private Boolean writeWeekly;

  private Boolean readWeekly;

  private Boolean writeBus;

  private Boolean readBus;

  private Boolean writeParking;

  private Boolean readParking;

  private Boolean writeAuth;

  private Boolean readAuth;
}
