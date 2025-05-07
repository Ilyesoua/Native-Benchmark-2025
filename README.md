 # 🚀 Native Benchmark 2025 – Spring / Quarkus / Go / Rust

![Langs](https://img.shields.io/badge/languages-Spring_Boot%20|%20Quarkus%20|%20Go%20|%20Rust-blue)
![Last Commit](https://img.shields.io/github/last-commit/Ilyesoua/Native-Benchmark-2025)


 This project benchmarks multiple backend stacks, comparing their performance in both **JVM mode** and **native compilation** (where available), along with two natively compiled languages: **Go** and **Rust**.

To see the [results](docs/main.pdf)

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
 | Go                 | Native           | Go 1.20                |
 | Rust               | Native           | Rust (actix-web, etc.)  |

 ---

 ## 📦 Usage

 Each framework has its own Dockerfile and build setup. All benchmarks run in isolated containers using Docker Compose.

 ### Build and run the tests on a framework

 ```bash
cd restApi<Framework>
./<Framework>.sh
 ```

#### Or

```powershell
cd restApi<Framework>
.\<Framework>.ps1
 ```


 ---

 ## 📊 Metrics Collected

[Metrics](docs/metrics.md)

 All metrics are logged and can be exported via `k6` output integrations (InfluxDB, JSON, etc.).

 ---

## 🔧 Methodology

 We run the tests 10 times for each Framework, compute their average and then compare it.

 ---

 ## 🧪 Architecture

For an overview of the application and the Docker set up : [Architecture](docs/architecture.md)

For an overview of the k6 testing and the monitoring : [Testing](docs/testing.md)

 ---

 ## 📝 Notes

 - Native builds use `GraalVM Native Image`.

 ---

 ## 📌 Requirements

 - Docker
 - Docker compose

 ---

 ## 🤝 Contributions

 Feel free to open issues or PRs if you'd like to expand the test suite (more endpoints, other languages, etc.).

 ---

 ## 📖 License

[License](LICENSE)

