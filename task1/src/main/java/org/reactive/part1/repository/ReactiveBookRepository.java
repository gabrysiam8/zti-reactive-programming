package org.reactive.part1.repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.reactive.part1.model.Book;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveBookRepository implements ReactiveRepository<Book> {

	private final static long DEFAULT_DELAY_IN_MS = 100;

	private final long delayInMs;

	private final List<Book> books;

	public ReactiveBookRepository() {
		this(DEFAULT_DELAY_IN_MS);
	}

	public ReactiveBookRepository(long delayInMs) {
		this.delayInMs = delayInMs;
		books = new ArrayList<>(Arrays.asList(Book.BOOK1, Book.BOOK2, Book.BOOK3, Book.BOOK4));
	}

	public ReactiveBookRepository(Book... books) {
		this(DEFAULT_DELAY_IN_MS, books);
	}

	public ReactiveBookRepository(long delayInMs, Book... books) {
		this.delayInMs = delayInMs;
		this.books = new ArrayList<>(Arrays.asList(books));
	}


	@Override
	public Mono<Void> save(Publisher<Book> bookPublisher) {
		return withDelay(Flux.from(bookPublisher)).doOnNext(books::add).then();
	}

	@Override
	public Mono<Book> findFirst() {
		return withDelay(Mono.just(books.get(0)));
	}

	@Override
	public Flux<Book> findAll() {
		return withDelay(Flux.fromIterable(books));
	}

	@Override
	public Mono<Book> findById(String title) {
		Book user = books.stream().filter((p) -> p.getTitle().equals(title))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No user with title " + title + " found!"));
		return withDelay(Mono.just(user));
	}


	private Mono<Book> withDelay(Mono<Book> userMono) {
		return Mono
				.delay(Duration.ofMillis(delayInMs))
				.flatMap(c -> userMono);
	}

	private Flux<Book> withDelay(Flux<Book> userFlux) {
		return Flux
				.interval(Duration.ofMillis(delayInMs))
				.zipWith(userFlux, (i, user) -> user);
	}

}
