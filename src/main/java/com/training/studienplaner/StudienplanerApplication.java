package com.training.studienplaner;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class StudienplanerApplication {

    private static EmbeddedPostgres embeddedPostgres;

    static {
        if ("true".equalsIgnoreCase(System.getProperty("embedded.pg.enabled"))) {
            try {
                int port = 5432;
                embeddedPostgres = EmbeddedPostgres.builder()
                        .setPort(port)
                        .start();

                // Устанавливаем системные свойства, которые будут использованы автоконфигурацией DataSource
                System.setProperty("SPRING_DATASOURCE_URL", embeddedPostgres.getJdbcUrl("postgres", "postgres"));
                System.setProperty("SPRING_DATASOURCE_USERNAME", "postgres");
                System.setProperty("SPRING_DATASOURCE_PASSWORD", "postgres");

                System.out.println("Embedded PostgreSQL started at: " + System.getProperty("SPRING_DATASOURCE_URL"));

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        if (embeddedPostgres != null) {
                            embeddedPostgres.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            } catch (IOException e) {
                throw new RuntimeException("Не удалось запустить Embedded PostgreSQL", e);
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(StudienplanerApplication.class, args);
    }
}
