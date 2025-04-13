package com.training.studienplaner.embedded;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;

public class EmbeddedPostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static EmbeddedPostgres embeddedPostgres;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        // Проверяем, включена ли опция
        if ("true".equalsIgnoreCase(environment.getProperty("embedded.pg.enabled"))) {
            try {
                // Запускаем встроенный PostgreSQL (укажите порт, например 5432)
                int port = 5432;
                embeddedPostgres = EmbeddedPostgres.builder()
                        .setPort(port)
                        .start();

                // Устанавливаем системные свойства до автоконфигурации DataSource
                System.setProperty("SPRING_DATASOURCE_URL", embeddedPostgres.getJdbcUrl("postgres", "postgres"));
                System.setProperty("SPRING_DATASOURCE_USERNAME", "postgres");
                System.setProperty("SPRING_DATASOURCE_PASSWORD", "postgres");

                System.out.println("Embedded PostgreSQL started: " + System.getProperty("SPRING_DATASOURCE_URL"));
            } catch (IOException e) {
                throw new RuntimeException("Не удалось запустить Embedded PostgreSQL", e);
            }
        }
    }
}
