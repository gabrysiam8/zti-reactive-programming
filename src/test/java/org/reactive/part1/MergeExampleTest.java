package org.reactive.part1;

import org.junit.Test;
import org.reactive.part1.model.Book;
import org.reactive.part1.repository.ReactiveBookRepository;
import org.reactive.part1.repository.ReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MergeExampleTest {

    MergeExample classUnderTest = new MergeExample();

    final static Book TEST_BOOK1 = new Book("test title 1", "test author", 2017);
    final static Book TEST_BOOK2 = new Book("test title 2", "test author 2", 2019);

    ReactiveRepository<Book> repositoryWithDelay = new ReactiveBookRepository(500);
    ReactiveRepository<Book> repository          = new ReactiveBookRepository(TEST_BOOK1, TEST_BOOK2);

    @Test
    public void mergeWithInterleave() {
        Flux<Book> flux = classUnderTest.mergeFluxWithInterleave(repositoryWithDelay.findAll(), repository.findAll());
        StepVerifier.create(flux)
                    .expectNext(TEST_BOOK1, TEST_BOOK2, Book.BOOK1, Book.BOOK2, Book.BOOK3, Book.BOOK4)
                    .verifyComplete();
    }

    @Test
    public void mergeWithNoInterleave() {
        Flux<Book> flux = classUnderTest.mergeFluxWithNoInterleave(repositoryWithDelay.findAll(), repository.findAll());
        StepVerifier.create(flux)
                    .expectNext(Book.BOOK1, Book.BOOK2, Book.BOOK3, Book.BOOK4, TEST_BOOK1, TEST_BOOK2)
                    .verifyComplete();
    }

    @Test
    public void multipleMonoToFlux() {
        Mono<Book> skylerMono = repositoryWithDelay.findFirst();
        Mono<Book> marieMono = repository.findFirst();
        Flux<Book> flux = classUnderTest.createFluxFromMultipleMono(skylerMono, marieMono);
        StepVerifier.create(flux)
                    .expectNext(Book.BOOK1, TEST_BOOK1)
                    .verifyComplete();
    }

}