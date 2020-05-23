package org.reactive.part1;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxExampleTest {

    FluxExample classUnderTest = new FluxExample();

    @Test
    public void testEmpty() {
        Flux<String> flux = classUnderTest.emptyFlux();

        StepVerifier.create(flux)
                    .verifyComplete();
    }

    @Test
    public void testFromValues() {
        Flux<String> flux = classUnderTest.oneTwoFluxFromValues();
        StepVerifier.create(flux)
                    .expectNext("one", "two")
                    .verifyComplete();
    }

    @Test
    public void testFromList() {
        Flux<String> flux = classUnderTest.oneTwoFluxFromList();
        StepVerifier.create(flux)
                    .expectNext("one", "two")
                    .verifyComplete();
    }

    @Test
    public void testError() {
        Flux<String> flux = classUnderTest.errorFlux();
        StepVerifier.create(flux)
                    .verifyError(IllegalStateException.class);
    }

    @Test
    public void testCountEach100ms() {
        Flux<Long> flux = classUnderTest.counter();
        StepVerifier.create(flux)
                    .expectNext(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)
                    .verifyComplete();
    }
}
