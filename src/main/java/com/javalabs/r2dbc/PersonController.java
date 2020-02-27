package com.javalabs.r2dbc;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private final PersonRepository personRepository;

    @GetMapping("/login/{email}/{password}")
    @PostMapping("/login/{email}/{password}")
    public ResponseEntity login(@PathVariable String email, @PathVariable String password) {
        logger.debug("\nlogin, email:" + email + ", password:" + password  + "\n");

        AtomicBoolean found = new AtomicBoolean(false);
        Mono<Person> personMono = personRepository.findByEmail(email);

        personMono.subscribe(
                value -> {
                    logger.debug("\nFOUND:" + value.toString() + "\n");

                    found.set(true);
                },
                error -> error.printStackTrace()
        );

        return found.get() ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/flux")
    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    @GetMapping("/mono/name/{firstname}")
    public Mono<Person> findByFirstName(@PathVariable String firstname) {
        return personRepository.findByFirstName(firstname);
    }

    @GetMapping("/mono/email/{email}")
    public Mono<Person> findByEmail(@PathVariable String email) {
        return personRepository.findByEmail(email);
    }

}
