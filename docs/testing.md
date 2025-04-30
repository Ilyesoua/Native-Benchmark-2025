 # 🧪 Description of K6 Tests

 This file describes the operation and objectives of the automated load testing performed with the K6 tool on the `api-benchmark` API.

 # 🎯 Test Objectives
 Evaluate the stability, responsiveness, and robustness of several REST endpoints by simulating user traffic and measuring performance and errors.

 # ⚙️ General Configuration
 - VUs (Virtual Users): 10
 - Total iterations: 1000
 - Setup timeout: 1300s
 Performance thresholds:
   - p(95) < 500ms for each endpoint
   - Failure rate < 1%

 # 🔄 Setup (Preparation)
 Send requests to `/health` until receiving "ok" or exceeding 60 attempts with a 20s pause between requests. If the API is not ready, the setup loop waits and retries automatically (up to 20 minutes).

 # 🔍 Test Groups

 ## 1. ✅ Healthcheck
   - Endpoint: GET /health
   - Verifies that the API is alive
   - Checks:
     - Status 200
     - Content: "ok"

 ## 2. 🎬 Create Movie
   - Endpoint: POST /movies
   - Sends a dynamically generated movie with a unique name
   - Checks:
     - Status 200
     - The movie ID is returned

 ## 3. 🎫 Get Movie by ID
   - Endpoint: GET /movies/{id}
   - Uses the previously created movie ID
   - Verifies that the movie can be retrieved

 ## 4. 🔍 Search Movie by Title
   - Endpoint: GET /movies?title=Inception
   - Searches for a movie by its title
   - Checks:
     - Status 200
     - The movie is returned

 ## 5. 🎲 Random Movie
   - Endpoint: GET /movies/random
   - Retrieves a random movie
   - Verifies that the movie is returned correctly

 ## 6. 🔧 Internal Process
   - Endpoint: GET /internal/process
   - Simulates an internal process
   - Verifies that the process is completed (status 200)

 # 📈 Custom Metrics (Trend)
 Each group records the request duration in a `Trend`:
   - http_req_duration_health
   - http_req_duration_create
   - http_req_duration_get_by_id
   - http_req_duration_search
   - http_req_duration_random
   - http_req_duration_internal

   These metrics help limit the impact of timeouts on the measurements.

 # 🧠 Conclusion
 These tests simulate realistic user load, measure the precise performance of each endpoint, and can be integrated into a CI/CD pipeline to detect regressions.

 # 🧪 Description of Monitoring via Docker

 Docker container monitoring allows tracking system resource usage during the execution of load tests. This real-time monitoring is essential for analyzing the impact of load tests on system performance.

 ## 1. 📊 Collecting Docker Statistics
 Using Docker to run the tests allows us to collect data on the performance of the containers hosting the `api-benchmark` API. We use Docker tools to monitor the following resources:

 - **CPU Usage**: Tracks the processor load used by the container, helping to understand the impact of the tests on CPU performance.
 - **Memory**: Memory usage by the container, which helps detect memory leaks or excessive resource usage.

 These data are collected continuously, allowing us to visualize the load evolution during test execution.

 ## 2. 📋 Exporting Data
 The collected statistics (CPU usage and memory) are exported to a CSV file, which enables visualizing this data over a given period. This provides an overview of the resources used and helps identify trends or anomalies during the tests.

 ## 3. 🖥️ Using Docker Desktop Tools
 Docker Desktop also provides several useful metrics that are not limited to test execution. These metrics complement the overall performance monitoring of the application by providing additional information on:

 - **Compilation Time**: The time required to transform the source code into an executable. This helps evaluate the efficiency of the build process and continuous integration (CI).
 - **Compression Time**: The time taken to compress the application before distribution, an important factor in fast deployment cycles.
 - **Startup Time**: The time required to start a Docker container, critical for applications that need to scale rapidly, such as in serverless architectures or with Kubernetes.
 - **Executable Size**: The final size of the binary or executable file generated. This can impact network transfer times and the disk space required on servers.
