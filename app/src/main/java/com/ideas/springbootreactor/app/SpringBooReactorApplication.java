package com.ideas.springbootreactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBooReactorApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringBooReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBooReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Observable
        Flux<String> nombres = Flux.just("Andres", "Daniel", "Jeronimo", "")
                .doOnNext(nombre -> {
                    if (nombre.isEmpty()) {
                        throw new RuntimeException("Nmbres no pueden ser vacios");
                    }
                    System.out.println(nombre);
                });

        //Debemos suscribrnos al observable para poder ver el resultado
        nombres.subscribe(e -> log.info(e), error -> log.error(error.getMessage()));
    }
}
