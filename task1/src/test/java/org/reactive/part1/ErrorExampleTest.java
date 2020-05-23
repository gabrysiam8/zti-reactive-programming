package org.reactive.part1;

import org.junit.Test;
import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ErrorExampleTest {

    ErrorExample classUnderTest = new ErrorExample();

    @Test
    public void monoWithValueInsteadOfError() {
        Mono<Book> mono = classUnderTest.returnMonoBook1OnError(Mono.error(new IllegalStateException()));
        StepVerifier.create(mono)
                    .expectNext(Book.BOOK1)
                    .verifyComplete();

        mono = classUnderTest.returnMonoBook1OnError(Mono.just(Book.BOOK2));
        StepVerifier.create(mono)
                    .expectNext(Book.BOOK2)
                    .verifyComplete();
    }

    @Test
    public void fluxWithValueInsteadOfError() {
        Flux<Book> flux = classUnderTest.returnFluxBook1Book2OnError(Flux.error(new IllegalStateException()));
        StepVerifier.create(flux)
                    .expectNext(Book.BOOK1, Book.BOOK2)
                    .verifyComplete();

        flux = classUnderTest.returnFluxBook1Book2OnError(Flux.just(Book.BOOK3, Book.BOOK4));
        StepVerifier.create(flux)
                    .expectNext(Book.BOOK3, Book.BOOK4)
                    .verifyComplete();
    }

    @Test
    public void handleCheckedExceptions() {
        Flux<Book> flux = classUnderTest.capitalizeMany(Flux.just(Book.BOOK4, Book.BOOK3));

        StepVerifier.create(flux)
                    .verifyError(ErrorExample.GetOutOfHereException.class);
    }
}