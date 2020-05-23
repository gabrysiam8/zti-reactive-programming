# Reactive Spring - exercises

## Build a REST client

1. Add a private attribute to `JokeService` of type `RestTemplate` called template
2. Add a constructor to `JokeService` that takes a single argument of type `RestTemplateBuilder`.
3. Inside the constructor, invoke the `build()` method on the `RestTemplateBuilder` and assign the result to the template attribute.
4. The site providing the joke API is http://icndb.com, the Internet Chuck Norris Database. The site exposes the jokes through the URL http://api.icndb.com. The API supports a few properties that will be useful here: the client can specify the hero’s first and last names and the joke category.
5. Inside the method `getJokeSync()`, create a local variable of type String called base and assign it to the URL "http://api.icndb.com/jokes/random?limitTo=[nerdy]".
6. Then create the full URL for the API: 
```
String url = String.format("%s&firstName=%s&lastName=%s", base, first, last);
```
7. The `RestTemplate` class has a `getForObject` method that takes two arguments: the URL and the class to instantiate with the resulting JSON response. The documentation on the services says that the resulting JSON takes the form:
```
{
  "type": "success",
  "value": {
    "id": 268,
    "joke": "Time waits for no man. Unless that man is Chuck Norris."
  }
}
```

8. The JSON response from the web service can now be converted into an instance of the `JokeResponse` class. Add a line to do that inside the `getJoke` method:
```
return template.getForObject(url, JokeResponse.class)
    .getValue().getJoke();
```
9. Inside the test `JokeServiceTest`, add the capability to log the jokes to the console. Spring Boot provides loggers from a variety of sources. In this case, use the one from the SLF4J library by adding an attribute to the `JokeServiceTest` class:
```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// ...

private Logger logger = LoggerFactory.getLogger(JokeServiceTest.class);
```
10. Use the logger inside the `getJoke` method to log the joke, either before or after the assertion.
11. Execute the test and make any needed corrections until it passes.


## Asynchronous Access
1. In the `JokeService` class, add an attribute of type `WebClient` and use the create method to initialize it to the base URL.
```
private WebClient client = WebClient.create("http://api.icndb.com");
```
2. Add a new method called `getJokeAsync` that takes two `String` values as arguments called first and last. For the return type use `Mono<String>` instead of `String`. The implementation is:
```
public Mono<String> getJokeAsync(String first, String last) {
    String path = "/jokes/random?limitTo=[nerdy]&firstName={first}&lastName={last}";
        return client.get()
            .uri(path, first, last)
            .retrieve()
            .bodyToMono(JokeResponse.class)
            .map(jokeResponse -> jokeResponse.getValue().getJoke());
}
```
3. To test this, go back to the `JokeServiceTest` class. There are two ways to test the method. One is to invoke it and block until the request is complete. A test to do that is shown here:
```
@Test
public void getJokeAsync() {
    String joke = service.getJokeAsync("Craig", "Walls")
            .block(Duration.ofSeconds(2));
    logger.info(joke);
    assertTrue(joke.contains("Craig") || joke.contains("Walls"));
}
```
4. As an alternative, the Reactor Test project includes a class called `StepVerifier`, which includes assertion methods. A test using that class is given by:
```
@Test
public void useStepVerifier() {
    StepVerifier.create(service.getJokeAsync("Craig", "Walls"))
            .assertNext(joke -> {
                logger.info(joke);
                assertTrue(joke.contains("Craig") || joke.contains("Walls"));
            })
            .verifyComplete();
}
```
5. Both of the new tests should now pass.
