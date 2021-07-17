package indian.poker.game.spring;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class IndianNamingStrategyImpl extends SpringPhysicalNamingStrategy {
    
    private Identifier convertToSnakeCase(final Identifier identifier) {
        
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = identifier.getText()
                .replaceAll(regex, replacement)
                .toLowerCase();
        return Identifier.toIdentifier(newName);
    }
    
    @Override
    public Identifier toPhysicalColumnName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        
        return convertToSnakeCase(identifier);
    }
    
    @Override
    public Identifier toPhysicalSequenceName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        
        return convertToSnakeCase(identifier);
    }
    
    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        
        String changeName = identifier.getText().replace("Entity", "");
        Identifier id = Identifier.toIdentifier(changeName);
        return convertToSnakeCase(id);
    }
}
