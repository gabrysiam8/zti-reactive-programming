package org.reactive.part1;

import java.util.Iterator;

import org.junit.Test;
import org.reactive.part1.model.Book;
import org.reactive.part1.repository.ReactiveBookRepository;
import org.reactive.part1.repository.ReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class ReactiveToBlockingExampleTest {

    ReactiveToBlockingExample workshop = new ReactiveToBlockingExample();
    ReactiveRepository<Book> repository = new ReactiveBookRepository();

    @Test
    public void mono() {
        Mono<Book> mono = repository.findFirst();
        Book book = workshop.monoToValue(mono);
        assertEquals(Book.BOOK1, book);
    }

    @Test
    public void flux() {
        Flux<Book> flux = repository.findAll();
        Iterable<Book> books = workshop.fluxToValues(flux);
        Iterator<Book> it = books.iterator();
        assertEquals(Book.BOOK1, it.next());
        assertEquals(Book.BOOK2, it.next());
        assertEquals(Book.BOOK3, it.next());
        assertEquals(Book.BOOK4, it.next());
        assertFalse(it.hasNext());
    }
}