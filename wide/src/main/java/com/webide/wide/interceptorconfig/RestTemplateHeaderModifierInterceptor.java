package com.webide.wide.interceptorconfig;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinSession;
import elemental.json.JsonValue;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.vaadin.firitin.util.WebStorage;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

@Component
public class RestTemplateHeaderModifierInterceptor implements ClientHttpRequestInterceptor {

    // interceptor to add auth header
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            String token = VaadinSession.getCurrent().getAttribute("Token").toString();
            //works but token empty
            request.getHeaders().add("authentication",token);
            return execution.execute(request,body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
