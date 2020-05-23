package org.reactive.part1;

import java.util.concurrent.CompletableFuture;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Test;
import org.reactive.part1.model.Book;
import org.reactive.part1.repository.ReactiveBookRepository;
import org.reactive.part1.repository.ReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class AdaptExampleTest {

    AdaptExample classUnderTest = new AdaptExample();
    ReactiveRepository<Book> repository = new ReactiveBookRepository();

    @Test
    public void adaptToFlowable() {
        Flux<Book> flux = repository.findAll();
        Flowable<Book> flowable = classUnderTest.fromFluxToFlowable(flux);
        StepVerifier.create(classUnderTest.fromFlowableToFlux(flowable))
                    .expectNext(Book.BOOK1, Book.BOOK2, Book.BOOK3, Book.BOOK4)
                    .verifyComplete();
    }

    @Test
    public void adaptToObservable() {
        Flux<Book> flux = repository.findAll();
        Observable<Book> observable = classUnderTest.fromFluxToObservable(flux);
        StepVerifier.create(classUnderTest.fromObservableToFlux(observable))
                    .expectNext(Book.BOOK1, Book.BOOK2, Book.BOOK3, Book.BOOK4)
                    .verifyComplete();
    }

    @Test
    public void adaptToSingle() {
        Mono<Book> mono = repository.findFirst();
        Single<Book> single = classUnderTest.fromMonoToSingle(mono);
        StepVerifier.create(classUnderTest.fromSingleToMono(single))
                    .expectNext(Book.BOOK1)
                    .verifyComplete();
    }

    @Test
    public void adaptToCompletableFuture() {
        Mono<Book> mono = repository.findFirst();
        CompletableFuture<Book> future = classUnderTest.fromMonoToCompletableFuture(mono);
        StepVerifier.create(classUnderTest.fromCompletableFutureToMono(future))
                    .expectNext(Book.BOOK1)
                    .verifyComplete();
    }

}