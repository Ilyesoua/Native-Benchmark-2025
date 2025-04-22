 # 🚀 Native Benchmark 2025 – Spring / Quarkus / Micronaut / Go / Rust

 This project benchmarks multiple backend stacks, comparing their performance in both **JVM mode** and **native compilation** (where available), along with two natively compiled languages: **Go** and **Rust**.

 ---

## 🎯 Goal

Evaluate and compare the following performance aspects:

- ⚙️ **Build & Packaging**

- 🚀 **Startup & Runtime Performance**

- 🌐 **API Responsiveness & Stability**

- 👥 **Load Simulation**


 ---

 ## 🧰 Stacks Tested

 | Stack              | Type            | Technology             |
 |--------------------|-----------------|-------------------------|
 | Spring Boot        | JVM             | Java 21 + Spring Boot   |
 | Spring Native      | Native (GraalVM) | Java 21 + Spring AOT    |
 | Quarkus            | JVM             | Java 21 + Quarkus       |
 | Quarkus Native     | Native (GraalVM) | Java 21 + Quarkus       |
 | Micronaut          | JVM             | Java 21 + Micronaut     |
 | Micronaut Native   | Native (GraalVM) | Java 21 + Micronaut     |
 | Go                 | Native           | Go 1.18                |
 | Rust               | Native           | Rust (actix-web, etc.)  |

 ---

 ## 📦 Usage

 Each framework has its own Dockerfile and build setup. All benchmarks run in isolated containers using Docker Compose.

 ### Build and run the tests on a framework

 ```bash
cd restApi<Framework>
docker-compose -f docker-compose.yml up —build
 ```

#### Or

```bash
cd restApi<Framework>
docker-compose -f docker-compose-native.yml up —build
 ```


 ---

 ## 📊 Metrics Collected

[Metrics](metrics.md)

 All metrics are logged and can be exported via `k6` output integrations (InfluxDB, JSON, etc.).

 ---

 ## 🧪 Endpoints Tested and their purpose

| **Method**  | **Route**                     | **Function**                                                                  | **Purpose**                                    |
|-------------|-------------------------------|-------------------------------------------------------------------------------|------------------------------------------------|
|  **GET**     | /api/movies?title=…                | Search for a movie by title via OMDB API call                                 | Web Request: search for a movie by title in OMDB |
|  **POST**    | /api/movies                    | Add a movie directly to the database (with JSON data)                         | Direct database access (write)                  |
|  **GET**     | /api/movies/{id}               | Read a movie from the database by ID                                           | Direct database access (read)                   |
|  **GET**     | /api/internal/process          | Trigger an internal CPU-bound process (e.g., calculation, parsing)            | Benchmark heavy processes                       |
|  **GET**     | /api/health                    | Check if the API is up (simple "pong" response)                               | Verify API availability                         |
|  **GET**     | /api/movies/random             | Retrieve a random movie from the database                                      | Database query + server-side logic             |


 ---

 ## 📝 Notes

 - Native builds use `GraalVM Native Image`.
 - Memory and startup performance are especially relevant for serverless / microservice environments.

 ---

 ## 📌 Requirements

 - Docker
 - Docker compose

 ---

 ## 📈 Coming Soon

 - Rust
 - Micronaut
 - Micronaut Native

 ---

 ## 🤝 Contributions

 Feel free to open issues or PRs if you'd like to expand the test suite (more endpoints, other languages, etc.).

 ---

 ## 📖 License

 MIT – free to use, modify, and share.
