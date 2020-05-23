package org.reactive.part1;

import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransformExample {

    // TODO: Capitalize the book tile and author
    Mono<Book> capitalizeOne(Mono<Book> mono) {
        return null;
    }

    // TODO: Capitalize the books title and author
    Flux<Book> capitalizeMany(Flux<Book> flux) {
        return null;
    }

    // TODO: Capitalize the books title and author using #asyncCapitalizeBook
    Flux<Book> asyncCapitalizeMany(Flux<Book> flux) {
        return null;
    }

    Mono<Book> asyncCapitalizeBook(Book u) {
        return Mono.just(new Book(u.getTitle().toUpperCase(), u.getAuthor().toUpperCase(), u.getYear()));
    }

}
