package org.reactive.part1;

import org.reactive.part1.model.Book;
import org.reactive.part1.repository.BlockingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BlockingToReactiveExample {

    // TODO: Create a Flux for reading all users from the blocking repository deferred until the flux is subscribed, and run it with an elastic scheduler
    Flux<Book> blockingRepositoryToFlux(BlockingRepository<Book> repository) {
        return null;
    }

    // TODO: Insert books contained in the Flux parameter in the blocking repository using an elastic scheduler and return a Mono<Void> that signal the end of the operation
    Mono<Void> fluxToBlockingRepository(Flux<Book> flux, BlockingRepository<Book> repository) {
        return null;
    }
}
