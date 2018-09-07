package com.glovoapp.backender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main class for start spring boot application
 *
 * Sentence "Ideally, you shouldn't need to modify any of the provided classes" is in test requirements.
 * <a href="file:../WORDING.md">WORDING.md</a>
 * However, I did that a bit, cause I had to run a web test for the controller {@link API}
 *
 * Also, I think that this structure of project not so useful as possible. I would like to create some packages
 * for layers of the application (e.g. dao, business, rest api, utilities and so on). It makes application easier
 * to read and understand.
 *
 * I also would like to create interfaces for repositories and service for avoiding couple Spring specific problems
 * with injection classes. And for more flexible structure. Today we have .json files, tomorrow relation database,
 * after that in-memory system. But interface would be only one with a strict API. For services the same.
 *
 * If not this sentence in test requirements, I did everything above. Everything else I did as well as I can.
 *
 * @author Artem Gavrilov
 */
@SpringBootApplication
@EnableConfigurationProperties(OrderProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
