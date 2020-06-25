package com.app.b;

import javax.inject.Inject;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class AppBTest {

    @Inject
    EmbeddedApplication application;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

}
