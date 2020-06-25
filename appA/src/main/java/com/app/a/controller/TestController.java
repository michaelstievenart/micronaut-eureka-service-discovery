package com.app.a.controller;

import javax.inject.Inject;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;

@Controller("/api")
public class TestController {

    @Inject
    @Client(id = "appB")
    private RxHttpClient rxHttpClient;

    @Get("/testA")
    public HttpResponse<?> testA() {
        final String value = rxHttpClient.retrieve(HttpRequest
                .GET("/api/testB")
                .accept(MediaType.APPLICATION_JSON), String.class)
                .blockingFirst();

        return HttpResponse.ok(value);
    }
}
