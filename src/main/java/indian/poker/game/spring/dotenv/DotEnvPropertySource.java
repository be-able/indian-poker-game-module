package indian.poker.game.spring.dotenv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

@Slf4j
public class DotEnvPropertySource extends PropertySource<DotEnvPropertyLoader> {
    
    /**
     * Name of the env {@link PropertySource}.
     */
    public static final String DOTENV_PROPERTY_SOURCE_NAME = "env";
    
    private static final String PREFIX = "env.";
    
    public DotEnvPropertySource(String name) {
        
        super(name, new DotEnvPropertyLoader());
    }
    
    public DotEnvPropertySource() {
        
        this(DOTENV_PROPERTY_SOURCE_NAME);
    }
    
    public static void addToEnvironment(ConfigurableEnvironment environment) {
        
        environment
                .getPropertySources()
                .addAfter(
                        StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                        new DotEnvPropertySource(DOTENV_PROPERTY_SOURCE_NAME));
        
        log.trace("DotenvPropertySource add to Environment");
    }
    
    /**
     * Return the value associated with the given name, or {@code null} if not found.
     *
     * @param name the property to find
     * @see PropertyResolver#getRequiredProperty(String)
     */
    @Override
    public Object getProperty(String name) {
        
        if (!name.startsWith(PREFIX)) {
            return null;
        }
        
        if (log.isTraceEnabled()) {
            log.trace("Getting env property for '" + name + "'");
        }
        
        return getSource().getValue(name.substring(PREFIX.length()));
    }
}
