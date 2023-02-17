package com.ideas.springbootreactor.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBooReactorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBooReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Observable
		Flux<String> nombres = Flux.just("Andres","Daniel","Jeronimo")
				.doOnNext(nombre -> System.out.println(nombre));

		//Debemos suscribrnos al observable para poder ver el resultado
		nombres.subscribe();
	}
}
