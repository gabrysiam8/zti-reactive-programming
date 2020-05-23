package org.reactive.part1.repository;

import org.reactive.part1.model.Book;
import reactor.core.publisher.Mono;

public class BlockingBookRepository implements BlockingRepository<Book> {

    private final ReactiveRepository<Book> reactiveRepository;

    private int callCount = 0;

    public BlockingBookRepository() {
        reactiveRepository = new ReactiveBookRepository();
    }

    public BlockingBookRepository(long delayInMs) {
        reactiveRepository = new ReactiveBookRepository(delayInMs);
    }

    public BlockingBookRepository(Book... books) {
        reactiveRepository = new ReactiveBookRepository(books);
    }

    public BlockingBookRepository(long delayInMs, Book... books) {
        reactiveRepository = new ReactiveBookRepository(delayInMs, books);
    }


    @Override
    public void save(Book book) {
        callCount++;
        reactiveRepository.save(Mono.just(book)).block();
    }

    @Override
    public Book findFirst() {
        callCount++;
        return reactiveRepository.findFirst().block();
    }

    @Override
    public Iterable<Book> findAll() {
        callCount++;
        return reactiveRepository.findAll().toIterable();
    }

    @Override
    public Book findById(String title) {
        callCount++;
        return reactiveRepository.findById(title).block();
    }

    public int getCallCount() {
        return callCount;
    }
}