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
The application exposes APIs to compare Base64 dncoded JSON Strings.
There are a total of 7 APIs.

The first 3 are crucial for the correct application operation.
The other 4 were optionally added, in order to ease usage and allow verification of currently active (already sent) Diff contents.

### 1 Add Left-side diff content with the given ID
<details><summary>Details</summary>
Adds the diff content to the left-side of the given ID.

```
POST - /v1/diff/{id}/left
```

Sample cURL:
```
curl -v -X POST \
  http://localhost:8080/v1/diff/1/left \
  -d ewoJImZpcnN0TmFtZSI6IkpvaG4iLAoJImxhc3ROYW1lIjoiRG9lIiwKCSJjb2RlIjoiMDAwMSIKfQ==
  ```
- The return will be an HTTP 201 (Created) code.
- If a content with the same ID already exists at the left side, it's overriden with the one form the current call.
</details>


### 2 Add Right-side diff content with the given ID
<details><summary>Details</summary>
Adds the diff content to the right-side of the given ID.

```
POST - /v1/diff/{id}/right
```

Sample cURL:
```
curl -v -X POST \
  http://localhost:8080/v1/diff/1/right \
  -d ewoJImZpcnN0TmFtZSI6Ikpvc2giLAoJImxhc3ROYW1lIjoiRG9lIiwKCSJjb2RlIjoiMDAwMiIKfQ==
  ```
- The return will be an HTTP 201 (Created) code.
- If a content with the same ID already exists at the left side, it's overriden with the one form the current call.
</details>


### 3 Compare both contents added on Left and Right Side, with a given ID
<details><summary>Details</summary>
Executes the comparison between two contents under the same ID.

```
GET - /v1/diff/{id}
```
The return JSON will always have the following format:

```json
{
    "id": [informed ID],
    "equal": [true/false],
    "sameSize": [true/false],
    "message": [Detailed message with comparison results]
}  
```
Sample cURL:
```
curl -v -X GET http://localhost:8080/v1/diff/1
```
Sample Responses:
- considering the curl calls for items #1 and #2:
```json
{
    "id": "1",
    "equal": false,
    "sameSize": true,
    "message": "Differences found at the following offset(s):\n offset: 24, length: 3\n offset: 73, length: 1"
}
```
- considering both calls for items #1 and #2 were made with the same content:
```json
{
    "id": "1",
    "equal": true,
    "sameSize": true,
    "message": "Both contents are equal"
}
```
- considering one of the sides wasn't informed yet (right side, in this case):
```json
{
    "id": "1",
    "equal": false,
    "sameSize": false ,
    "message": "Unable to compare. Right side is null"
}
```
</details>


### 4 Show the Left-Side content (encoded)
<details><summary>Details</summary>
Simply returns the left-side encoded JSON conent for the given ID.

```
GET - /v1/diff/{id}/left
```
Sample cURL:
```
curl -v -X GET http://localhost:8080/v1/diff/1/left
```
Sample Response:
```
ewoJImZpcnN0TmFtZSI6IkpvaG4iLAoJImxhc3ROYW1lIjoiRG9lIiwKCSJjb2RlIjoiMDAwMSIKfQ==
```
</details>

### 5 Show the Right-Side content (encoded)
<details><summary>Details</summary>
Simply returns the right-side encoded JSON conent for the given ID.

```
GET - /v1/diff/{id}/right
```
Sample cURL:
```
curl -v -X GET http://localhost:8080/v1/diff/1/right
```
Sample Response:
```
ewoJImZpcnN0TmFtZSI6Ikpvc2giLAoJImxhc3ROYW1lIjoiRG9lIiwKCSJjb2RlIjoiMDAwMiIKfQ==
```
</details>


### 6 Show the Left-Side content (decoded, raw JSON string)
<details><summary>Details</summary>
Similar to #4 but instead of the raw (encoded) content, returns the left-side decoded JSON String of the given ID.

```
GET - /v1/diff/{id}/left/decoded
```
Sample cURL:
```
curl -v -X GET http://localhost:8080/v1/diff/1/left/decoded
```
Sample Response:
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "code": "0001"
}
```
</details>


### 7 Show the Left-Side content (decoded, raw JSON string)
<details><summary>Details</summary>
Similar to #4 but instead of the raw (encoded) content, returns the right-side decoded JSON String of the given ID.

```
GET - /v1/diff/{id}/right/decoded
```
Sample cURL:
```
curl -v -X GET http://localhost:8080/v1/diff/1/right/decoded
```
Sample Response:
```json
{
	"firstName":"Josh",
	"lastName":"Doe",
	"code":"0002"
}
```
</details>

