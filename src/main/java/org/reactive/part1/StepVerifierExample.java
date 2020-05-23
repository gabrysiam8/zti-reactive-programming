package org.reactive.part1;

import java.util.function.Supplier;

import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;

public class StepVerifierExample {

    // TODO: Use StepVerifier to check that the flux parameter emits "one" and "two" values then completes successfully.
    void expectOneTwoComplete(Flux<String> flux) {
        fail();
    }

    // TODO: Use StepVerifier to check that the flux parameter emits "one" and "two" elements then a RuntimeException error.
    void expectOneTwoError(Flux<String> flux) {
        fail();
    }

    // TODO:: Use StepVerifier to check that the flux parameter emits a Book with "test" title and next with "example" then completes successfully.
    void expectTestExampleComplete(Flux<Book> flux) {
        fail();
    }

    // TODO: Expect 10 elements then complete and notice how long the test takes.
    void expect10Elements(Flux<Long> flux) {
        fail();
    }

    // TODO: Expect 3600 elements at intervals of 1 second, and verify quicker than 3600s by manipulating virtual time thanks to StepVerifier#withVirtualTime, notice how long the test takes
    void expect3600Elements(Supplier<Flux<Long>> supplier) {
        fail();
    }

    private void fail() {
        throw new AssertionError("method not implemented");
    }
}
