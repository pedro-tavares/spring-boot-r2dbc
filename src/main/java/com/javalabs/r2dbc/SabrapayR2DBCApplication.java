package com.javalabs.r2dbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.BaseStream;

@Slf4j
@SpringBootApplication
public class SabrapayR2DBCApplication {

	public static void main(String[] args) {
		SpringApplication.run(SabrapayR2DBCApplication.class, args);
	}

	@Bean
	public ApplicationRunner seeder(DatabaseClient client, MerchantRepository repository) {
		return args -> getSchema()
			.flatMap(sql -> executeSql(client, sql))
			.doOnSuccess(count -> log.info("Schema created, rows updated: {}", count))
			.then(repository.deleteAll())
			.doOnSuccess(v -> log.info("Repository cleared"))
			.thenMany(getPeople())
			.flatMap(repository::save)
			.subscribe(merchant -> log.info("Merchant saved: {}", merchant));
	}

	private Flux<Merchant> getPeople() {
		return Flux.just(
				new Merchant(null, "John Doe", "John Doe", "john@mail", "johnspassword", "Active"),
				new Merchant(null, "Jane Doe", "Jane Doe", "jane@mail", "janespassword", "Active")
		);
	}

	private Mono<Integer> executeSql(DatabaseClient client, String sql) {
		return client.execute(sql).fetch().rowsUpdated();
	}

	private Mono<String> getSchema() throws URISyntaxException {
		Path path = Paths.get(ClassLoader.getSystemResource("schema.sql").toURI());
		return Flux
			.using(() -> Files.lines(path), Flux::fromStream, BaseStream::close)
			.reduce((line1, line2) -> line1 + "\n" + line2);
	}
}
