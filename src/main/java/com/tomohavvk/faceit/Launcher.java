package com.tomohavvk.faceit;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@QuarkusMain
public class Launcher {

    public static void main(String... args) {
        log.info("Starting web application");
        Quarkus.run(args);
    }
}