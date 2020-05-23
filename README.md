# Reactive Spring - exercise 2

## Reactive Spring Data

1. Annotate `Officer` with `@Document` from `org.springframework.data.mongodb.core.mapping`.

2. Annotate `id` with `@Id` from `org.springframework.data.annotation`. Note that the data type for the `id` is `String`, since we will be using a MongoDB database.

3. Make a Spring Data interface called `OfficerRepository` that extends `ReactiveMongoRepository<Officer, String>` in the `com.reactive.reactiveofficers.dao` package.
```
public interface OfficerRepository extends ReactiveMongoRepository<Officer, String> { }
```
4. Create a test for the repository called `OfficerRepositoryTest`. Add the class annotation `@SpringBootTest`.

5. Autowire in an instance of `OfficerRepository` called repository.

6. Provide initialization data in the form of a list of officers:
```
private List<Officer> officers = Arrays.asList(
        new Officer(Rank.CAPTAIN, "James", "Kirk"),
        new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
        new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
        new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
        new Officer(Rank.CAPTAIN, "Jonathan", "Archer"));
```
7. Note that the `id` fields will be null or empty until the officers are saved. To save them, add a method called `setUp` that takes no arguments and returns `void`. Annotated it with `@BeforeEach` from JUnit 5. This method will drop the existing collection (if there is one) and recreate it before each test.
8. The body of the setUp method is:
```
repository.deleteAll()
          .thenMany(Flux.fromIterable(officers))
          .flatMap(repository::save)
          .then()
          .block();
```
9. To test the `save` method, create an officer and save it, then check that the generated id is not an empty string:
```
@Test
public void save() {
    Officer lorca = new Officer(Rank.CAPTAIN, "Gabriel", "Lorca");
    StepVerifier.create(repository.save(lorca))
            .expectNextMatches(officer -> !officer.getId().equals(""))
            .verifyComplete();
}
```

10. Test `findAll` by checking that there are five officers in the test collection:
```
@Test
public void findAll() {
    StepVerifier.create(repository.findAll())
            .expectNextCount(5)
            .verifyComplete();
}
```

11. To check `findById`, get the generated id’s from the officer collection, find each one by id, and verify that a single element is returned each time. For invalid id’s verify that no elements are emitted.
```
@Test
public void findById() {
    officers.stream()
            .map(Officer::getId)
            .forEach(id ->
                    StepVerifier.create(repository.findById(id))
                            .expectNextCount(1)
                            .verifyComplete());
}

@Test
public void findByIdNotExist() {
    StepVerifier.create(repository.findById("xyz"))
            .verifyComplete();
}
```

12. Check the `count` method by again verifying that there are five officers in the sample collection
```
@Test
public void count() {
    StepVerifier.create(repository.count())
            .expectNext(5L)
            .verifyComplete();
}
```

13. The tests should all pass.

## Spring WebFlux with Annotated Controllers

For the actual application, you’ll need a running instance of MongoDB. Once MongoDB is installed and assuming you have 
a running server, initialize a collection with sample data using a `OfficerInit` class and `ApplicationRunner` from Spring.

1. Add a `OfficerInit` constructor that takes an argument of type `OfficerRepository`. Add an attribute of that type and 
save the constructor argument to the attribute. The presence of a single constructor will automatically autowire in the 
arguments. Implement the required run method using the same approach taken in the test:
```
@Override
    public void run(ApplicationArguments args) throws Exception {
        dao.deleteAll()
            .thenMany(Flux.just(new Officer(Rank.CAPTAIN, "James", "Kirk"),
                                new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
                                new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
                                new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
                                new Officer(Rank.CAPTAIN, "Jonathan", "Archer")))
           .flatMap(dao::save)
           .thenMany(dao.findAll())
           .subscribe(System.out::println);
    }
```
2. Add a rest controller:
```
import com.nfjs.reactiveofficers.dao.OfficerRepository;
import com.nfjs.reactiveofficers.entities.Officer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/officers")
public class OfficerController {
    private OfficerRepository repository;

    public OfficerController(OfficerRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<Officer> getAllOfficers() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Officer> getOfficer(@PathVariable String id) {
        return repository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Officer> saveOfficer(@RequestBody Officer officer) {
        return repository.save(officer);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Officer>> updateOfficer(@PathVariable(value = "id") String id,
                                                       @RequestBody Officer officer) {
        return repository.findById(id)
                .flatMap(existingOfficer -> {
                    existingOfficer.setRank(officer.getRank());
                    existingOfficer.setFirst(officer.getFirst());
                    existingOfficer.setLast(officer.getLast());
                    return repository.save(existingOfficer);
                })
                .map(updateOfficer -> new ResponseEntity<>(updateOfficer, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteOfficer(@PathVariable(value = "id") String id) {
        return repository.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    public Mono<Void> deleteAllOfficers() {
        return repository.deleteAll();
    }
}
```

3. Add an integration test that uses `WebTestClient`. Note that the server must be running to execute this test.
```
import com.oreilly.reactiveofficers.dao.OfficerRepository;
import com.oreilly.reactiveofficers.entities.Officer;
import com.oreilly.reactiveofficers.entities.Rank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfficerControllerTest {
//    private WebTestClient client = WebTestClient.bindToServer()
//                                                .baseUrl("http://localhost:8080")
//                                                .build();

    @Autowired
    private WebTestClient client;

    @Autowired
    private OfficerRepository repository;

    private List<Officer> officers = Arrays.asList(
            new Officer(Rank.CAPTAIN, "James", "Kirk"),
            new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
            new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
            new Officer(Rank.CAPTAIN, "Jonathan", "Archer"));

    @BeforeEach
    public void setUp() {
        repository.deleteAll()
                  .thenMany(Flux.fromIterable(officers))
                  .flatMap(repository::save)
                  .doOnNext(System.out::println)
                  .then()
                  .block();
    }

    @Test
    public void testGetAllOfficers() {
        client.get().uri("/officers")
              .accept(MediaType.APPLICATION_JSON_UTF8)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
              .expectBodyList(Officer.class)
              .hasSize(5)
              .consumeWith(System.out::println);
    }

    @Test
    public void testGetOfficer() {
        client.get().uri("/officers/{id}", officers.get(0).getId())
              .exchange()
              .expectStatus().isOk()
              .expectBody(Officer.class)
              .consumeWith(System.out::println);
    }

    @Test
    public void testCreateOfficer() {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");

        client.post().uri("/officers")
              .contentType(MediaType.APPLICATION_JSON_UTF8)
              .accept(MediaType.APPLICATION_JSON_UTF8)
              .body(Mono.just(officer), Officer.class)
              .exchange()
              .expectStatus().isCreated()
              .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
              .expectBody()
              .jsonPath("$.id").isNotEmpty()
              .jsonPath("$.first").isEqualTo("Nyota")
              .jsonPath("$.last").isEqualTo("Uhuru")
              .consumeWith(System.out::println);
    }
}
```

## Spring WebFlux with Functional Endpoints
1. Create an `OfficerHandler` class in the `controllers` package that is just a regular `@Component`, 
with methods that each take a `ServerRequest` and return a `Mono<ServerResponse>`:
```
import com.oreilly.reactiveofficers.dao.OfficerRepository;
import com.oreilly.reactiveofficers.entities.Officer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class OfficerHandler {
    private OfficerRepository repository;

    public OfficerHandler(OfficerRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> listOfficers(ServerRequest request) {
        return ServerResponse.ok()
                             .contentType(APPLICATION_JSON)
                             .body(repository.findAll(), Officer.class);
    }

    public Mono<ServerResponse> createOfficer(ServerRequest request) {
        Mono<Officer> officerMono = request.bodyToMono(Officer.class);
        return officerMono.flatMap(officer ->
                                           ServerResponse.status(HttpStatus.CREATED)
                                                         .contentType(APPLICATION_JSON)
                                                         .body(repository.save(officer), Officer.class));
    }

    public Mono<ServerResponse> getOfficer(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Officer> personMono = this.repository.findById(id);
        return personMono
                .flatMap(person -> ServerResponse.ok()
                                                 .contentType(APPLICATION_JSON)
                                                 .body(fromObject(person)))
                .switchIfEmpty(notFound);
    }
}
```

2. Make a class called `RouterConfig` in the `com.reactive.reactiveofficers.configuration` package. 
Annotate it with `@Configuration` and add a bean to return a `RouterFunction<ServerResponse>`:
```
import com.oreilly.reactiveofficers.controllers.OfficerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> route(OfficerHandler handler) {
        return RouterFunctions
                .route(GET("/route/{id}").and(accept(APPLICATION_JSON)),
                       handler::getOfficer)
                .andRoute(GET("/route").and(accept(APPLICATION_JSON)),
                          handler::listOfficers)
                .andRoute(POST("/route").and(contentType(APPLICATION_JSON)),
                          handler::createOfficer);
    }
}
```

3. Add a test for the router function and handler:
```
import com.oreilly.reactiveofficers.dao.OfficerRepository;
import com.oreilly.reactiveofficers.entities.Officer;
import com.oreilly.reactiveofficers.entities.Rank;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfficerHandlerAndRouterTests {
    @Autowired
    private WebTestClient client;

    @Autowired
    private OfficerRepository repository;

    private List<Officer> officers = Arrays.asList(
            new Officer(Rank.CAPTAIN, "James", "Kirk"),
            new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
            new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
            new Officer(Rank.CAPTAIN, "Jonathan", "Archer"));

    @BeforeEach
    public void setUp() {
        repository.deleteAll()
                  .thenMany(Flux.fromIterable(officers))
                  .flatMap(repository::save)
                  .doOnNext(System.out::println)
                  .then()
                  .block();
    }

    @Test
    public void testCreateOfficer() {
        Officer officer = new Officer(Rank.LIEUTENANT, "Hikaru", "Sulu");
        client.post().uri("/route")
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .body(Mono.just(officer), Officer.class)
              .exchange()
              .expectStatus().isCreated()
              .expectHeader().contentType(MediaType.APPLICATION_JSON)
              .expectBody()
              .jsonPath("$.id").isNotEmpty()
              .jsonPath("$.first").isEqualTo("Hikaru")
              .jsonPath("$.last").isEqualTo("Sulu");
    }

    @Test
    public void testGetAllOfficers() {
        client.get().uri("/route")
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentType(MediaType.APPLICATION_JSON)
              .expectBodyList(Officer.class);
    }

    @Test
    public void testGetSingleOfficer() {
        Officer officer = repository.save(new Officer(Rank.ENSIGN, "Wesley", "Crusher")).block();

        client.get()
              //.uri("/route/{id}", Collections.singletonMap("id", officer.getId()))
              .uri("/route/{id}", officer.getId())
              .exchange()
              .expectStatus().isOk()
              .expectBody()
              .consumeWith(response ->
                                   Assertions.assertThat(response.getResponseBody()).isNotNull());
    }
}
```

4. The tests should all now pass.






