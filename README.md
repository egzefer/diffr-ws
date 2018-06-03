# Diff - Binary JSON comparator


### Prerequisites
- Apache Maven
- Java 1.8


## Application start and test execution

### Running the Application
To start the application, execute the following command (at the command-line):
```
mvn spring-boot:run
```

Alternatively, from an IDE, run the `DiffrApplication` class as a Java Application.


### Running Tests

#### Unit Tests
To run **unit tests** at the command line, simply execute one of the following commands:
```
mvn package
```
```
mvn test
```

#### Integration Tests
To run **integration tests** at the command line, execute the following command, *with the application running*:
```
mvn verify
```
This command will execute both unit and integration tests.


## Comparison APIs 
The application exposes a total of 5 APIs, which are:

**`POST`** `/v1/diff/{id}/left`

**`POST`** `/v1/diff/{id}/right`

**`GET`** `/v1/diff/{id}`

**`GET`** `/v1/diff/{id}/left`

**`GET`** `/v1/diff/{id}/right`

**`GET`** `/v1/diff/{id}/left/decoded`

**`GET`** `/v1/diff/{id}/right/decoded`
