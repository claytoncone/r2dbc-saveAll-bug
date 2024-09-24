package org.claytoncone.r2dbcsaveallbug.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@TestConfiguration
public class TestDataConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestDataConfig.class);

    @Bean
    public Mono<Void> initializeTestData(DatabaseClient databaseClient) {
        ResourceDatabasePopulator schema = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        ResourceDatabasePopulator data = new ResourceDatabasePopulator(new ClassPathResource("testdata.sql"));

        logger.debug("Populating schema...");
        return Mono.from(schema.populate(databaseClient.getConnectionFactory()))
                .doOnSuccess(unused -> logger.debug("Schema populated successfully"))
                .doOnError(error -> logger.error("Error populating schema", error))
                .then(Mono.from(data.populate(databaseClient.getConnectionFactory()))
                        .doOnSuccess(unused -> logger.debug("Data populated successfully"))
                        .doOnError(error -> logger.error("Error populating data", error)))
                .then(verifyDataPopulation(databaseClient));
    }

    private Mono<Void> verifyDataPopulation(DatabaseClient databaseClient) {
        return databaseClient.sql("SELECT COUNT(*) AS count FROM contact")
                .map(row -> row.get("count", Long.class))
                .first()
                .doOnNext(count -> {
                    if (count == 0) {
                        logger.error("No contact records found in the database.");
                    } else {
                        logger.debug("Contact records found in the database: {}", count);
                    }
                })
                .then();
    }
}