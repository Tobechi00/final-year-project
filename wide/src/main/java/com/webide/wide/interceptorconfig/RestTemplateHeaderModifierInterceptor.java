package com.webide.wide.interceptorconfig;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

    // interceptor to add auth header
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        try {
            String token = VaadinSession.getCurrent().getAttribute("USER_TOKEN").toString();
            //works but token empty
            request.getHeaders().add("authentication",token);
            return execution.execute(request,body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
