package indian.poker.game.spring.dotenv;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DotEnvApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    /**
     * Initialize the given application context.
     *
     * @param applicationContext the application to configure
     */
    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        
        DotEnvPropertySource.addToEnvironment(applicationContext.getEnvironment());
    }
}
