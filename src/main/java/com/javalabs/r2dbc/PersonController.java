package com.javalabs.r2dbc;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonRepository repository;

    @GetMapping("/flux")
    public Flux<Person> findAll() {
        return repository.findAll();
    }

    @GetMapping("/mono/{firstname}")
    public Mono<Person> findByFirstName(@PathVariable String firstname) {
        return repository.findByFirstName(firstname);
    }
}
