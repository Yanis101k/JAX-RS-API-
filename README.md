# 📘 Smart Campus API

## 🚀 Overview

The **Smart Campus API** is a RESTful web service built using **Java, JAX-RS (Jersey), and Grizzly**. It simulates a smart campus environment where rooms, sensors, and sensor readings are managed through a structured API.

The API follows **REST principles** and demonstrates key architectural concepts such as:

* Resource-based API design
* Sub-resource locators
* Custom exception handling with mappers
* API observability using filters
* Clean separation of concerns

This project uses **in-memory storage**, making it lightweight and easy to run locally.

---

## 🏗️ API Design

### 🔹 Core Resources

* **Rooms** → Physical spaces in the campus
* **Sensors** → Devices assigned to rooms
* **Sensor Readings** → Measurements produced by sensors

---

### 🔹 Endpoints

#### Rooms

GET /api/v1/rooms
POST /api/v1/rooms
GET /api/v1/rooms/{id}
DELETE /api/v1/rooms/{id}

#### Sensors

GET /api/v1/sensors
POST /api/v1/sensors
GET /api/v1/sensors/{id}

Filtering:
GET /api/v1/sensors?type=CO2

#### Sensor Readings

GET /api/v1/sensors/{sensorId}/readings
POST /api/v1/sensors/{sensorId}/readings

---

## ⚙️ How to Run Locally

### 🔧 Prerequisites

* Java (JDK 8+)
* Maven

### ▶️ Steps

git clone <your-repo-url>
cd smart-campus-api
mvn clean install
mvn exec:java

API runs on:
http://localhost:8080/api/v1/

---

# 📚 Report Answers

---

## Question 1

In JAX-RS, a Resource class represents a REST endpoint that handles incoming HTTP requests such as GET, POST, PUT, and DELETE. By default, JAX-RS follows a request-scoped lifecycle, meaning that a new instance of the resource class is created for every incoming request. This ensures that each client request is handled independently, and no instance variables are shared between different requests.

However, JAX-RS also allows the use of a singleton lifecycle, where only one instance of the resource class is created and shared across all requests. While this can improve performance by reducing object creation overhead, it introduces potential concurrency issues because multiple threads may access and modify shared data at the same time.

This architectural decision has a significant impact on how in-memory data structures such as maps and lists are managed. In a request-scoped model, instance variables are safe because each request operates on its own object. However, if data is stored in static variables or shared structures, it can still lead to race conditions.

In contrast, when using a singleton resource, all requests share the same instance, meaning that any in-memory data structures must be carefully synchronized to prevent data corruption or loss. Without proper synchronization, concurrent modifications can lead to inconsistent states or unexpected behavior.

To prevent these issues, developers can use thread-safe data structures such as ConcurrentHashMap, or apply synchronization techniques such as the synchronized keyword. Another strategy is to avoid shared mutable state altogether by delegating data management to external systems such as databases.

---

## Question 2

Hypermedia (HATEOAS) is considered an advanced REST feature because it allows an API to guide the client by including links inside the responses. Instead of only returning data, the API also shows what actions can be done next and which resources are available. This makes the API easier to understand and use, because the client can follow the links provided instead of guessing or memorising URLs.

This approach is better than using static documentation. In traditional APIs, developers need to read documentation and hardcode all endpoints in their code. If the API changes, their application may stop working and must be updated manually.

With HATEOAS, the API becomes more flexible because the client depends on the links in the response, not on fixed URLs. This reduces errors and makes it easier to update the API without breaking existing clients.

In addition, providing rich metadata at the root endpoint (such as version, contact information, and available resources) makes the API self-documenting. Developers can understand how to use the API directly from the responses, without needing to rely heavily on external documentation.

---

## Question 3

When designing a REST API, there is an important choice between returning only resource IDs or returning full objects. Returning only IDs reduces the size of the response, which saves bandwidth and lowers payload overhead. This can be useful when there are many records, because smaller responses are faster to transfer and process. However, the client will usually need to make extra requests to fetch the full details of each resource.

In contrast, returning full objects gives the client more useful information immediately. This reduces the number of additional API calls and makes the API easier to use, especially for small or simple resources such as rooms. The disadvantage is that the response becomes larger, which increases bandwidth usage and payload size.

In this project, returning full room objects for GET /rooms and GET /rooms/{id} is a practical choice because the room resource contains only a few fields, such as ID, name, building, and capacity. The payload is still small, while the API remains simple and convenient for client developers. Therefore, full-object responses improve usability, while ID-only responses are more beneficial in cases where resources are very large or when minimising response size is a priority

---

## Question 4

In this implementation, the DELETE operation is idempotent because sending the same delete request multiple times does not keep changing the server state after the first successful deletion. The first DELETE /rooms/{id} request removes the room from the in-memory store, so the server state changes once at that moment.

If the client sends the exact same DELETE request again for the same room, the room no longer exists in the system. As a result, the API returns 404 Not Found, but the server state does not change any further. The room is already deleted, so repeating the request has no additional effect on the stored data.

The same reasoning applies when deletion is blocked because the room still has active sensors. In that case, every repeated DELETE request will continue to return the same conflict response, and the room will remain in the system. Again, the server state does not change across repeated identical requests.

Therefore, the operation is idempotent because multiple identical DELETE calls lead to the same final server state: either the room remains blocked from deletion, or it is deleted once and stays deleted.

---

## Question 5

When the @Consumes(MediaType.APPLICATION_JSON) annotation is applied to a POST method, it specifies that the endpoint only accepts request bodies formatted as JSON. This means the client must send data with the Content-Type: application/json header. If a client instead sends data in a different format, such as text/plain or application/xml, a content-type mismatch occurs. In this case, JAX-RS automatically detects that the request’s media type does not match the one defined in @Consumes and rejects the request before the resource method is executed. As a result, the server returns a 415 Unsupported Media Type response, indicating that the format of the request body is not supported. This behaviour ensures that the API processes only correctly formatted data, helping maintain consistency, reliability, and proper input validation.

---

## Question 6

Using @QueryParam is better for filtering because it clearly shows that we are working with the same collection and just narrowing the results. For example, in /api/v1/sensors?type=CO2, the main resource is still all sensors, and type=CO2 simply means “give me only the sensors that match this condition.” This makes it clear that filtering is optional and does not change the structure of the resource. On the other hand, using a path like /api/v1/sensors/type/CO2 makes it look like type and CO2 are part of the resource path, as if they represent a different or nested resource, which is not true because CO2 is just a filter value, not a unique resource. Query parameters are also more flexible because we can easily add more filters, such as /api/v1/sensors?type=CO2&state=Active&roomId=2, without making the URL complicated. In simple terms, query parameters are better for searching and filtering lists, while path parameters are better for accessing a specific item, like /api/v1/sensors/5.

---

## Question 7

The Sub-Resource Locator pattern helps organise an API by breaking it into smaller, focused parts instead of putting everything in one large class. In our case, the main SensorResource handles general sensor operations, and when we need to deal with readings, it delegates that responsibility to a separate SensorReadingResource. This means each class has a clear purpose, which makes the code easier to understand and maintain.

In large APIs, if we define every nested path like sensors/{id}/readings/{rid} inside one massive controller class, the class quickly becomes very complex. It would contain many methods handling different responsibilities, which makes it harder to read, debug, and extend. Even small changes could affect unrelated parts of the code, increasing the risk of errors.

By delegating logic to separate classes, we reduce this complexity. Each resource class focuses only on its own domain, following the idea of separation of concerns. For example, SensorReadingResource only manages reading-related operations such as retrieving history or creating a new reading. This makes the system more modular, so developers can work on one part without impacting others.

Another benefit is scalability. As the API grows, we can easily add more sub-resources or features without making existing classes overly complicated. It also improves code reusability and testing, since smaller classes are easier to test in isolation.

Overall, the Sub-Resource Locator pattern helps keep large APIs clean, modular, and easier to manage by delegating responsibilities to specialised classes instead of centralising everything in one place.

---

## Question 8

HTTP 422 (Unprocessable Entity) is often considered more accurate than 404 (Not Found) in this situation because the request itself is valid, but the data inside it has a logical problem. When a client sends a JSON payload, the server can successfully read and understand the structure of the request, so technically nothing is “missing” at the endpoint level. The API endpoint exists, and the request format is correct.

However, inside that valid JSON, there might be a reference to something that does not exist, for example a sensor being assigned to a room ID that is not in the system. In this case, the problem is not that the resource (URL) cannot be found, but that the data provided by the client cannot be processed correctly. That is why 422 is more semantically accurate: it clearly tells the client that “your request is well-formed, but I cannot process it because of invalid or inconsistent data.”

On the other hand, a 404 response is usually used when the endpoint itself or a direct resource in the URL does not exist, such as requesting /rooms/99 when that room is not found. Using 404 for errors inside the JSON payload can be misleading, because the endpoint is actually valid and reachable. Therefore, 422 provides a clearer and more precise communication of the problem in modern REST APIs.

---

## Question 9

Exposing internal Java stack traces to API users is a serious security risk because it reveals detailed information about how the system is built and how it behaves internally. A stack trace is meant for developers to debug errors, but if it is sent to external users, it can give attackers valuable insights that help them plan attacks.

From a cybersecurity standpoint, one major risk is information leakage. A stack trace can expose the structure of the application, including package names, class names, and method names. This allows an attacker to understand how the system is organized and identify specific components they might try to exploit.

It can also reveal file paths and server environment details, such as where the application is deployed on the server. This information can help attackers understand the underlying operating system, directory structure, and configuration, which makes targeted attacks easier.

Another important risk is that stack traces may expose technology details, such as the frameworks, libraries, or versions being used (for example, specific JAX-RS or server implementations). Attackers can use this information to search for known vulnerabilities in those technologies and exploit them.

Additionally, stack traces sometimes include sensitive data, such as input values, query parameters, or even parts of database queries. This can help attackers discover how the application handles data and potentially find weaknesses like injection points.

Overall, exposing stack traces gives attackers a “blueprint” of the system. That is why, in secure API design, we always return generic error messages (like HTTP 500 with a simple message) and keep detailed error logs only on the server side, where they are accessible to developers but not to external users.

---

## Question 10

Using JAX-RS filters for logging is much better than manually putting Logger.info() inside every resource method because it separates cross-cutting concerns from business logic. Logging is something that applies to every request in the system, not just one specific endpoint. By using a filter, you define the logging behavior once in a single place, and it is automatically applied to all requests and responses. This makes the code cleaner and easier to manage.

If logging is written manually inside every resource method, the code becomes repetitive and harder to maintain. Every time you create a new endpoint, you would need to remember to add logging again. If you forget even once, your observability becomes inconsistent. In contrast, with a filter, you cannot forget — logging is applied globally by the framework.

Another advantage is maintainability. If you later decide to change the logging format, add more information (like headers or timestamps), or switch logging levels, you only need to update the filter. Without filters, you would have to modify many different methods across multiple classes, which is time-consuming and error-prone.

Filters also improve readability and design quality. Resource classes should focus only on handling business logic, such as creating sensors or retrieving rooms. By moving logging into a filter, your resource methods stay clean and focused, which follows good software engineering principles like separation of concerns.

Finally, filters provide a centralized and consistent way to handle all requests and responses, including errors. Even if an exception occurs, the filter still logs the request and the final status code, ensuring full visibility of the API behavior. This level of consistency is very difficult to achieve with manual logging inside each method.

In summary, JAX-RS filters make logging more centralized, consistent, maintainable, and aligned with clean architecture principles, while manual logging leads to duplication, potential mistakes, and harder-to-maintain code.

---

## 👨‍💻 Author

Yanis Kaced
University of Westminster
