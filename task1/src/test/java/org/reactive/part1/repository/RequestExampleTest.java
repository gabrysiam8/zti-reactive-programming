package org.reactive.part1.repository;

import org.junit.Test;
import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class RequestExampleTest {

    RequestExample workshop = new RequestExample();
    ReactiveRepository<Book> repository = new ReactiveBookRepository();

    @Test
    public void requestAll() {
        Flux<Book> flux = repository.findAll();
        StepVerifier verifier = workshop.requestAllExpectFour(flux);
        verifier.verify();
    }

    @Test
    public void requestOneByOne() {
        Flux<Book> flux = repository.findAll();
        StepVerifier verifier = workshop.requestOneExpectBook1ThenRequestOneExpectBook2(flux);
        verifier.verify();
    }

    @Test
    public void experimentWithLog() {
        Flux<Book> flux = workshop.fluxWithLog();
        StepVerifier.create(flux, 0)
                    .thenRequest(1)
                    .expectNextMatches(u -> true)
                    .thenRequest(1)
                    .expectNextMatches(u -> true)
                    .thenRequest(2)
                    .expectNextMatches(u -> true)
                    .expectNextMatches(u -> true)
                    .verifyComplete();
    }

    @Test
    public void experimentWithDoOn() {
        Flux<Book> flux = workshop.fluxWithDoOnPrintln();
        StepVerifier.create(flux)
                    .expectNextCount(4)
                    .verifyComplete();
    }
}