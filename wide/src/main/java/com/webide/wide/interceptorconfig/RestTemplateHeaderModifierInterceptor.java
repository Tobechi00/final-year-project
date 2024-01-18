package com.webide.wide.interceptorconfig;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.vaadin.firitin.util.WebStorage;

import java.io.IOException;

@Component
public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

    // interceptor to add auth header
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            StringBuilder token = new StringBuilder();

            WebStorage.getItem("Token", value ->{
                String authHeader = (value == null? "<no value stored>" : value );
                token.append(value);
            });

            //works but token empty
            request.getHeaders().add("authentication",token.toString());
            return execution.execute(request,body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
