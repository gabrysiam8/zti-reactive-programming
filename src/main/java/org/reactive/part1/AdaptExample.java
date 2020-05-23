package org.reactive.part1;

import java.util.concurrent.CompletableFuture;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AdaptExample {
    // TODO Adapt Flux to RxJava Flowable
    Flowable<Book> fromFluxToFlowable(Flux<Book> flux) {
        return null;
    }

    // TODO Adapt RxJava Flowable to Flux
    Flux<Book> fromFlowableToFlux(Flowable<Book> flowable) {
        return null;
    }

    // TODO Adapt Flux to RxJava Observable
    Observable<Book> fromFluxToObservable(Flux<Book> flux) {
        return null;
    }

    // TODO Adapt RxJava Observable to Flux
    Flux<Book> fromObservableToFlux(Observable<Book> observable) {
        return null;
    }

    // TODO Adapt Mono to RxJava Single
    Single<Book> fromMonoToSingle(Mono<Book> mono) {
        return null;
    }

    // TODO Adapt RxJava Single to Mono
    Mono<Book> fromSingleToMono(Single<Book> single) {
        return null;
    }

    // TODO Adapt Mono to Java 8+ CompletableFuture
    CompletableFuture<Book> fromMonoToCompletableFuture(Mono<Book> mono) {
        return null;
    }

    // TODO Adapt Java 8+ CompletableFuture to Mono
    Mono<Book> fromCompletableFutureToMono(CompletableFuture<Book> future) {
        return null;
    }
}
