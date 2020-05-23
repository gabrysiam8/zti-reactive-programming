package org.reactive.part1.repository;

import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class RequestExample {

    ReactiveRepository<Book> repository = new ReactiveBookRepository();

    // TODO Create a StepVerifier that initially requests all values and expect 4 values to be received
    StepVerifier requestAllExpectFour(Flux<Book> flux) {
        return null;
    }

    // TODO Create a StepVerifier that initially requests 1 value and expects Book.BOOK1 then requests another value and expects Book.BOOK2.
    StepVerifier requestOneExpectBook1ThenRequestOneExpectBook2(Flux<Book> flux) {
        return null;
    }

    // TODO Return a Flux with all books stored in the repository that prints automatically logs for all Reactive Streams signals
    Flux<Book> fluxWithLog() {
        return null;
    }

    // TODO Return a Flux with all books stored in the repository that prints "Library:" on subscribe, "title author" for all values and "The end!" on complete
    Flux<Book> fluxWithDoOnPrintln() {
        return null;
    }

}
