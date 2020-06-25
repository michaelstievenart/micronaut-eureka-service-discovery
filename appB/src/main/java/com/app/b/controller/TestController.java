package com.app.b.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/api")
public class TestController {

    @Get("/testB")
    public HttpResponse<String> testB() {
        return HttpResponse.ok("Success");
    }
}
