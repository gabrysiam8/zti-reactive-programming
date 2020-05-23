package org.reactive.part1;

import reactor.core.publisher.Flux;

public class FluxExample {

    // TODO: Return an empty Flux
    Flux<String> emptyFlux() {
        return null;
    }

    // TODO: Return a Flux that contains 2 values: "one" and "two" (without using an array or a collection)
    Flux<String> oneTwoFluxFromValues() {
        return null;
    }

    // TODO: Create a Flux from a List with "one" and "two"
    Flux<String> oneTwoFluxFromList() {
        return  null;
    }

    // TODO: Create a Flux that emits an IllegalStateException
    Flux<String> errorFlux() {
        return null;
    }

    // TODO: Create a Flux that emits increasing values from 0 to 9 each 100ms
    Flux<Long> counter() {
        return null;
    }
}
