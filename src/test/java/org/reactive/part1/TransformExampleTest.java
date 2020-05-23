package org.reactive.part1;

import org.junit.Test;
import org.reactive.part1.model.Book;
import org.reactive.part1.repository.ReactiveBookRepository;
import org.reactive.part1.repository.ReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TransformExampleTest {

    TransformExample classUnderTest = new TransformExample();
    ReactiveRepository<Book> repository = new ReactiveBookRepository();

    @Test
    public void transformMono() {
        Mono<Book> mono = repository.findFirst();
        StepVerifier.create(classUnderTest.capitalizeOne(mono))
                    .expectNext(new Book("SILENT PATIENT", "ALEX MICHAELIDES", 2019))
                    .verifyComplete();
    }

    @Test
    public void transformFlux() {
        Flux<Book> flux = repository.findAll();
        StepVerifier.create(classUnderTest.capitalizeMany(flux))
                    .expectNext(
                        new Book("SILENT PATIENT", "ALEX MICHAELIDES", 2019),
                        new Book("DUNE", "FRANK HERBERT", 1982),
                        new Book("NINETEEN EIGHTY-FOUR", "GEORGE ORWELL", 1953),
                        new Book("SGOODMAN", "SAUL", 2020))
                    .verifyComplete();
    }

    @Test
    public void  asyncTransformFlux() {
        Flux<Book> flux = repository.findAll();
        StepVerifier.create(classUnderTest.asyncCapitalizeMany(flux))
                    .expectNext(
                        new Book("SILENT PATIENT", "ALEX MICHAELIDES", 2019),
                        new Book("DUNE", "FRANK HERBERT", 1982),
                        new Book("NINETEEN EIGHTY-FOUR", "GEORGE ORWELL", 1953),
                        new Book("SGOODMAN", "SAUL", 2020))
                    .verifyComplete();
    }
}