package com.app.b.config;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.OncePerRequestHttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.http.filter.ServerFilterPhase;
import org.reactivestreams.Publisher;

@Filter("/**")
public class CustomFilter extends OncePerRequestHttpServerFilter {

    @Override
    protected Publisher<MutableHttpResponse<?>> doFilterOnce(final HttpRequest<?> request, final ServerFilterChain chain) {
        return chain.proceed(request);
    }

    @Override
    public int getOrder() {
        return ServerFilterPhase.FIRST.order();
    }
}
