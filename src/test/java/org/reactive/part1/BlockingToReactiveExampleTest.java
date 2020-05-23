package org.reactive.part1;

import java.util.Iterator;

import org.junit.Test;
import org.reactive.part1.model.Book;
import org.reactive.part1.repository.BlockingBookRepository;
import org.reactive.part1.repository.ReactiveBookRepository;
import org.reactive.part1.repository.ReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.*;

public class BlockingToReactiveExampleTest {

    BlockingToReactiveExample classUnderTest = new BlockingToReactiveExample();

    @Test
    public void slowPublisherFastSubscriber() {
        BlockingBookRepository repository = new BlockingBookRepository();
        Flux<Book> flux = classUnderTest.blockingRepositoryToFlux(repository);
        assertEquals("The call to findAll must be deferred until the flux is subscribed", 0, repository.getCallCount());
        StepVerifier.create(flux)
                    .expectNext(Book.BOOK1, Book.BOOK2, Book.BOOK3, Book.BOOK4)
                    .verifyComplete();
    }

    @Test
    public void fastPublisherSlowSubscriber() {
        ReactiveRepository<Book> reactiveRepository = new ReactiveBookRepository();
        BlockingBookRepository blockingRepository = new BlockingBookRepository(new Book[]{});
        Mono<Void> complete = classUnderTest.fluxToBlockingRepository(reactiveRepository.findAll(), blockingRepository);
        assertEquals(0, blockingRepository.getCallCount());
        StepVerifier.create(complete)
                    .verifyComplete();
        Iterator<Book> it = blockingRepository.findAll().iterator();
        assertEquals(Book.BOOK1, it.next());
        assertEquals(Book.BOOK2, it.next());
        assertEquals(Book.BOOK3, it.next());
        assertEquals(Book.BOOK4, it.next());
        assertFalse(it.hasNext());
    }
}