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
import java.util.concurrent.atomic.AtomicReference;

@CrossOrigin(origins = {"http://127.0.0.1:8088","http://localhost:8088"}, maxAge = 3600)
@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private Logger LOG = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private final MerchantRepository merchantRepository;

//    @GetMapping("/login/{email}/{password}")
//    @PostMapping("/login/{email}/{password}")
    @ResponseBody
    public ResponseEntity login(@PathVariable String email, @PathVariable String password) {
        LOG.debug("\nlogin, email:" + email + ", password:" + password  + "\n");

        AtomicBoolean found = new AtomicBoolean(false);
        AtomicBoolean authenticated = new AtomicBoolean(false);
        AtomicReference<ResponseEntity> result = new AtomicReference<>(ResponseEntity.notFound().build());
        Mono<Merchant> merchantMono = merchantRepository.findByEmail(email);

        merchantMono.subscribe(
                value -> {
                    LOG.debug("\nFOUND:" + value.toString() + "\n");

                    found.set(true);
                    authenticated.set(password.equals(value.getPassword()));

                    if (authenticated.get()) {
                        LOG.debug("\nAUTHENTICATED:" + email + "\n");

                        result.set(ResponseEntity.ok().build());
                    } else {
                        LOG.debug("\nUNAUTHORIZED:" + email + "\n");

                        result.set(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                    }
                },
                error -> {
                    error.printStackTrace();
                    result.set(ResponseEntity.notFound().build());
                }
        );

        return result.get();
    }

    @PostMapping(value ="/login",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity login(@ModelAttribute("merchant") Merchant merchant) {
        return login(merchant.getEmail(), merchant.getPassword());
    }

    @GetMapping("/flux")
    public Flux<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    @GetMapping("/mono/name/{registeredName}")
    public Mono<Merchant> findByRegisteredName(@PathVariable String registeredName) {
        return merchantRepository.findByRegisteredName(registeredName);
    }

    @GetMapping("/mono/email/{email}")
    public Mono<Merchant> findByEmail(@PathVariable String email) {
        return merchantRepository.findByEmail(email);
    }

}
