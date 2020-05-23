package org.reactive.part1;

import java.time.Duration;

import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoExampleTest {

    MonoExample classUnderTest = new MonoExample();

    @Test
    public void empty() {
        Mono<String> mono = classUnderTest.emptyMono();
        StepVerifier.create(mono)
                    .verifyComplete();
    }

    @Test
    public void noSignal() {
        Mono<String> mono = classUnderTest.monoWithNoSignal();
        StepVerifier
            .create(mono)
            .expectSubscription()
            .expectTimeout(Duration.ofSeconds(1))
            .verify();
    }

    @Test
    public void fromValue() {
        Mono<String> mono = classUnderTest.oneMono();
        StepVerifier.create(mono)
                    .expectNext("one")
                    .verifyComplete();
    }

    @Test
    public void error() {
        Mono<String> mono = classUnderTest.errorMono();
        StepVerifier.create(mono)
                    .verifyError(IllegalStateException.class);
    }
}