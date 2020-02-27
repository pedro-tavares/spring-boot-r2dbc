package com.javalabs.r2dbc;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MerchantRepository extends ReactiveCrudRepository<Merchant, Long> {

    @Query("SELECT * FROM merchant m WHERE m.registeredName = :registeredName")
    public Mono<Merchant> findByRegisteredName(String registeredName);

    @Query("SELECT * FROM merchant m WHERE m.email = :email")
    public Mono<Merchant> findByEmail(String email);
}
