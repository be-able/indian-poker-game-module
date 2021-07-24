package indian.poker.game.spring.dotenv;

import io.github.cdimascio.dotenv.Dotenv;

public class DotEnvPropertyLoader {

  private Dotenv dotenv;

  public DotEnvPropertyLoader() {

    dotenv = Dotenv.configure().ignoreIfMissing().load();
  }

  public Object getValue(String key) {

    return dotenv.get(key);
  }
}
