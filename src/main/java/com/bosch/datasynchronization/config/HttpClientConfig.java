package com.bosch.datasynchronization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient httpClient(){
        return HttpClient.newBuilder().connectTimeout(Duration.of(10, ChronoUnit.SECONDS)).followRedirects(HttpClient.Redirect.NEVER).build();
    }

}
