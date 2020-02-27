package com.javalabs.r2dbc;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        AtomicBoolean authenticated = new AtomicBoolean(false);
        Mono<Person> personMono = personRepository.findByEmail(email);

        personMono.subscribe(
                value -> {
                    logger.debug("\nFOUND:" + value.toString() + "\n");

                    authenticated.set(password.equals(value.getPassword()));
                    found.set(true);
                },
                error -> error.printStackTrace()
        );

        if (found.get()) {
            if (authenticated.get()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(name ="/login",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity login(@ModelAttribute("merchant") Person merchant) {
        logger.debug("\nlogin, email:" + merchant.getEmail() + ", password:" + merchant.getPassword() + "\n");

        return login(merchant.getEmail(), merchant.getPassword());
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
