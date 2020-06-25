package com.app.a.config;

import javax.inject.Named;
import javax.inject.Singleton;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.discovery.DiscoveryClient;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.loadbalance.DiscoveryClientLoadBalancerFactory;
import io.micronaut.http.client.netty.DefaultHttpClient;

@Factory
@Requires(beans = DiscoveryClient.class)
public class ClientConfiguration {

    public static final String UAA_SERVICE = "uaa";

    @Named(UAA_SERVICE)
    @Singleton
    public RxHttpClient authClient(final BeanContext beanContext,
                                   final @Named(UAA_SERVICE) HttpClientConfiguration configuration) {
        final var loadBalancer = beanContext.getBean(DiscoveryClientLoadBalancerFactory.class).create(UAA_SERVICE);
        return new DefaultHttpClient(loadBalancer, configuration);
    }
}
