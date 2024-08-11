package amirhs.de.stage.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalEnvConfig {

    @Bean
    public Dotenv loadDotenv() {
        return Dotenv.configure().directory("/app").load();
    }
}
