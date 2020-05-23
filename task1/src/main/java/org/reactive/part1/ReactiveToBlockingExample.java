package org.reactive.part1;

import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveToBlockingExample {

    // TODO: Return the user contained in that Mono
    Book monoToValue(Mono<Book> mono) {
        return null;
    }


    // TODO: Return the users contained in that Flux
    Iterable<Book> fluxToValues(Flux<Book> flux) {
        return null;
    }
}
