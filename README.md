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

