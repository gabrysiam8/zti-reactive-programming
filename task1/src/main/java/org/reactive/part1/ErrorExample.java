package org.reactive.part1;

import org.reactive.part1.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ErrorExample {

    // TODO: Return a Mono<Book> containing Book.BOOK1 when an error occurs in the input Mono, else do not change the input Mono.
    Mono<Book> returnMonoBook1OnError(Mono<Book> mono) {
        return null;
    }
    // TODO: Return a Flux<Book> containing Book.BOOK1 and Book.BOOK2 when an error occurs in the input Flux, else do not change the input Flux.
    Flux<Book> returnFluxBook1Book2OnError(Flux<Book> flux) {
        return null;
    }

    // TODO: Implement a method that capitalizes each book of the incoming flux using the capitalizeBook method and emits an error containing a GetOutOfHereException error
    Flux<Book> capitalizeMany(Flux<Book> flux) {
        return null;
    }

    Book capitalizeBook(Book book) throws GetOutOfHereException {
        if (book.equals(Book.BOOK4)) {
            throw new GetOutOfHereException();
        }
        return new Book(book.getTitle(), book.getAuthor(), book.getYear());
    }

    protected final class GetOutOfHereException extends Exception {
        private static final long serialVersionUID = 0L;
    }

}
