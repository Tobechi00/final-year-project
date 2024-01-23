package com.webide.wide.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.server.VaadinSession;
import com.webide.wide.dao.LoginDao;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.interceptorconfig.RestTemplateHeaderModifierInterceptor;
import elemental.json.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.vaadin.firitin.util.WebStorage;

import java.util.ArrayList;
import java.util.List;


public class ServerRequestMethods {

    Logger logger = LoggerFactory.getLogger(ServerRequestMethods.class);


    public ProgramOutputDto sendCodeRunRequest(ProgramInputDao programInputDao) throws HttpClientErrorException {
        ProgramOutputDto programOutputDto = new ProgramOutputDto();
        try {
            String token = (String) VaadinSession.getCurrent().getAttribute("Token");
            //adding auth header to rest template
            RestTemplate restTemplate = new RestTemplate();
            List<ClientHttpRequestInterceptor> interceptors
                    = restTemplate.getInterceptors();
            if (CollectionUtils.isEmpty(interceptors)) {
                interceptors = new ArrayList<>();
            }

            interceptors.add(new RestTemplateHeaderModifierInterceptor());
            restTemplate.setInterceptors(interceptors);

            String url = "http://localhost:8080/w-ide/api/python/submit";
            ResponseEntity<ProgramOutputDto> responseEntity = restTemplate.postForEntity(url,programInputDao, ProgramOutputDto.class);

            programOutputDto = responseEntity.getBody();

            return programOutputDto;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public void sendLoginRequestAndReceiveToken(LoginDao loginDao) throws HttpClientErrorException {

            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8080/w-ide/api/login";

            HttpEntity<LoginDao> requestEntity = new HttpEntity<>(loginDao);

            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(url,requestEntity, String.class);

            //attaching token to session
        VaadinSession.getCurrent().setAttribute("Token",tokenResponse.getBody());
    }


}
