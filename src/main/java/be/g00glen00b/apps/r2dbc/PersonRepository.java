package be.g00glen00b.apps.r2dbc;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {

    @Query("SELECT * FROM person p WHERE p.firstname = :firstname")
    public Mono<Person> findByFirstName(String firstname);

}