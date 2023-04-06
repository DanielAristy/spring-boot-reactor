package com.dac.springboot.reactor.app;

import com.dac.springboot.reactor.app.model.Comment;
import com.dac.springboot.reactor.app.model.User;
import com.dac.springboot.reactor.app.model.UserComment;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {


    private final static Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//		printNames();
//		useMapReactive();
//        getUser();
//        getIterableNames();
//        exampleFlatmapWebFlux();
//        exampleCollectList();
//        exampleUserCommentFlatmap();
//        exampleZipWith();
//        exampleZipWith2();
//        exampleZipWithRange();
//        delayZipWith();
//        delayElement();
//        infinityFlow();
//        exampleIntervalCreate();
        backPressure();
    }

    private void exampleSuscribeWebflux() {
        Flux<String> names = Flux.just("Daniel", "Cristian", "Monica", "Luisa")
                .doOnNext(name -> {
                    if (name.isBlank()) throw new RuntimeException("No se pueden nombres vacios");
                });
        names.subscribe(log::info, error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Se ha finalizado el flujo correctamente!");
            }
        });
    }

    private void useMapReactive() {
        Flux<String> names = Flux.just("Daniel", "Cristian", "Monica")
                .doOnNext(name -> {
                    if (name.isBlank()) throw new RuntimeException("No se pueden nombres vacios");
                })
                .map(String::toUpperCase);

        names.subscribe(log::info, error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Se ha finalizado el flujo correctamente!");
            }
        });
    }

    private void exampleMapWebflux() {
        Flux<String> names = Flux.just("Daniel Gomez", "Cristian Quinones", "Monica Garcia", "Luisa Uribe");

        Flux<User> users = names.map(name -> new User(name.split(" ")[0], name.split(" ")[1]))
                .filter(user -> user.getName().toLowerCase().equals("cristian"))
                .doOnNext(user -> {
                    if (user == null) throw new RuntimeException("Objeto vacio");
                })
                .map(user -> {
                    String name = user.getName().toUpperCase();
                    user.setName(name);
                    return user;
                });

        users.subscribe(user -> log.info(user.toString()), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Se ha finalizado el flujo correctamente!");
            }
        });
    }

    public void getIterableNames() {
        List<String> userList = Arrays.asList("Daniel.Gomez", "Cristian.Quinones", "Monica.Garcia", "Luisa.Uribe", "Julian.Gomez");

        Flux<String> names = Flux.fromIterable(userList);
        Flux<User> users = names.map(name -> new User(name.split("\\.")[0], name.split("\\.")[1]))
                .filter(user -> user.getName().toLowerCase().equals("cristian"))
                .doOnNext(user -> {
                    if (user == null) throw new RuntimeException("Objeto vacio");
                })
                .map(user -> {
                    String name = user.getName().toUpperCase();
                    user.setName(name);
                    return user;
                });

        users.subscribe(user -> log.info(user.toString()), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Se ha finalizado el flujo correctamente!");
            }
        });
    }

    public void exampleFlatmapWebFlux() {
        List<String> userList = Arrays.asList("Daniel-Gomez", "Cristian-Quinones", "Monica-Garcia", "Luisa-Uribe", "Julian-Gomez");

        Flux.fromIterable(userList)
                .map(name -> new User(name.split("-")[0], name.split("-")[1]))
                .flatMap(user -> "daniel".equalsIgnoreCase(user.getName().toLowerCase()) ?
                        Mono.just(user) : Mono.empty()
                )
                .subscribe(user -> log.info(user.toString()));
    }

    public void exampleCollectList() {
        List<User> userList = Arrays.asList(
                new User("Daniel", "Gomez"),
                new User("Cristian", "Quinones"),
                new User("Monica", "Garcia"));

        Flux.fromIterable(userList)
                .collectList()
                .subscribe(list -> log.info(list.toString()));
    }

    public void exampleUserCommentFlatmap() {
        Mono<User> user = Mono.fromCallable(() -> new User("Jorge", "Sosa"));
        Mono<Comment> comments = Mono.fromCallable(() -> {
            Comment userComments = new Comment();
            userComments.addComments("Hola Jorge, como estas?");
            userComments.addComments("Imaginate manito que estoy tomando el curso de Spring Reactor!");
            return userComments;
        });

        user.flatMap(userData -> comments.map(comment -> new UserComment(userData, comment)))
                .subscribe(userComment -> log.info(userComment.toString()));
    }

    public void exampleZipWith() {
        Mono<User> user = Mono.fromCallable(() -> new User("Samuel", "Perez"));
        Mono<Comment> comments = Mono.fromCallable(() -> {
            Comment userComments = new Comment();
            userComments.addComments("Hola Samu, como estas?");
            userComments.addComments("Imaginate manito que estoy aprendiendo ZipWith con Spring Reactor!");
            return userComments;
        });

        Mono<UserComment> userComment = user
                .zipWith(comments, (userData, commentsData) -> new UserComment(userData, commentsData));

        userComment.subscribe(userCommentData -> log.info(userCommentData.toString()));
    }

    public void exampleZipWith2() {
        Mono<User> user = Mono.fromCallable(() -> new User("Rigoberto", "Gomez"));
        Mono<Comment> comments = Mono.fromCallable(() -> {
            Comment userComments = new Comment();
            userComments.addComments("Hola Rigo, como estas?");
            userComments.addComments("Imaginate manito que estoy aprendiendo ZipWith con Tuplas en  Spring Reactor!");
            return userComments;
        });

        Mono<UserComment> userComment = user.zipWith(comments)
                .map(tuple -> {
                    User u = tuple.getT1();
                    Comment c = tuple.getT2();
                    return new UserComment(u, c);
                });

        userComment.subscribe(userCommentData -> log.info(userCommentData.toString()));
    }

    public void exampleZipWithRange() {
        Flux<Integer> ranges = Flux.range(0, 4);
        Flux.just(1, 2, 3, 4, 5)
                .map(i -> i * 2)
                .zipWith(ranges, (one, two) -> String.format("Primer flux: %d, Segundo flux: %d", one, two))
                .subscribe(log::info);
    }

    public void delayZipWith() {
        Flux<Integer> ranges = Flux.range(1, 12);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));

        ranges.zipWith(delay, (range, retard) -> range)
                .doOnNext(integer -> log.info(integer.toString())).blockLast();

    }

    public void delayElement() {
        Flux<Integer> ranges = Flux.range(1, 12)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(integer -> log.info(integer.toString()));

        ranges.blockLast();
    }

    public void infinityFlow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Flux.interval(Duration.ofSeconds(1))
                .doOnTerminate(latch::countDown)
                .flatMap(i -> (i >= 5) ?
                        Flux.error(new InterruptedException("Solo hasta el 5")) :
                        Flux.just(i)
                )
                .map(i -> "Interator: " + i)
                .retry(1)
                .subscribe(log::info, e -> log.error(e.getMessage()));

        latch.await();
    }

    public void exampleIntervalCreate() {
        Flux.create(emitter -> {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        private Integer contador = 0;

                        @Override
                        public void run() {
                            emitter.next(++contador);
                            if (contador == 10) {
                                timer.cancel();
                                emitter.complete();
                            }
                        }
                    }, 1000, 1000);
                })
                .doOnNext(count -> log.info(count.toString()))
                .doOnComplete(() -> log.info("Se ha finalizado la ejecucion!"))
                .subscribe();
    }

    public void backPressure() {

        /* Consumir datos por lotes utlizando Subscriber o propiedad limitRate
         *
         */

        Flux.range(1, 10)
                .log()
//                .limitRate(2) //Numero de elementos que queremos recibir por lotes
                .subscribe(new Subscriber<Integer>() {
                    private Subscription subscription;
                    private Integer limite = 5;
                    private Integer consumer = 0;

                    @Override
                    public void onSubscribe(Subscription s) {
                        this.subscription = s;
                        subscription.request(limite);

                    }

                    @Override
                    public void onNext(Integer i) {
                        log.info(i.toString());
                        consumer++;
                        if (consumer.equals(limite)) {
                            consumer = 0;
                            subscription.request(limite);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
