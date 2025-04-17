package com.benchmark.restApi;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class RestApiApplication {

    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
