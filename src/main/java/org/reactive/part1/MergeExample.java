package org.reactive.part1;

import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MergeExample {

    // TODO: Merge flux1 and flux2 values with interleave
    Flux<Book> mergeFluxWithInterleave(Flux<Book> flux1, Flux<Book> flux2) {
        return null;
    }

    // TODO: Merge flux1 and flux2 values with no interleave (flux1 values and then flux2 values)
    Flux<Book> mergeFluxWithNoInterleave(Flux<Book> flux1, Flux<Book> flux2) {
        return null;
    }

    // TODO: Create a Flux containing the value of mono1 then the value of mono2
    Flux<Book> createFluxFromMultipleMono(Mono<Book> mono1, Mono<Book> mono2) {
        return null;
    }

}
