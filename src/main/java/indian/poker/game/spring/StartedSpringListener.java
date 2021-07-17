package indian.poker.game.spring;

import indian.poker.game.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartedSpringListener implements ApplicationListener<ApplicationStartedEvent> {

  private final UsersService usersService;

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {

    log.info("=====================");
    log.info("Application starting");
    log.info("=====================");

    this.usersService.initRootUser();
  }
}
