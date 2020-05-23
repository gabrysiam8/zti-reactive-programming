package org.reactive.part1;

import java.time.Duration;

import org.junit.Test;
import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StepVerifierExampleTest {

    StepVerifierExample classUnderTest = new StepVerifierExample();

    @Test
    public void expectElementsThenComplete() {
        classUnderTest.expectOneTwoComplete(Flux.just("one", "two"));
    }

    @Test
    public void expect2ElementsThenError() {
        classUnderTest.expectOneTwoError(Flux.just("foo", "bar").concatWith(Mono.error(new RuntimeException())));
    }

    @Test
    public void expectElementsWithThenComplete() {
        classUnderTest.expectTestExampleComplete(Flux.just(new Book("test", null, null), new Book("example", null, null)));
    }

    @Test
    public void count() {
        classUnderTest.expect10Elements(Flux.interval(Duration.ofSeconds(1)).take(10));
    }

    @Test
    public void countWithVirtualTime() {
        classUnderTest.expect3600Elements(() -> Flux.interval(Duration.ofSeconds(1)).take(3600));
    }
}