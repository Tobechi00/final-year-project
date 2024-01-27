package com.webide.wide.server;

import com.vaadin.flow.server.VaadinSession;
import com.webide.wide.dao.LoginDao;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.dao.UserPayloadDao;
import com.webide.wide.interceptorconfig.RestTemplateHeaderModifierInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class ServerRequestMethods {

    Logger logger = LoggerFactory.getLogger(ServerRequestMethods.class);


    public ProgramOutputDto sendCodeRunRequest(ProgramInputDao programInputDao) throws HttpClientErrorException {
        ProgramOutputDto programOutputDto = new ProgramOutputDto();
        try {
            String token = (String) VaadinSession.getCurrent().getAttribute("USER_TOKEN");

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

    public void sendLoginRequestAndReceivePayload(LoginDao loginDao) throws HttpClientErrorException {

            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8080/w-ide/api/login";

            HttpEntity<LoginDao> requestEntity = new HttpEntity<>(loginDao);

            ResponseEntity<UserPayloadDao> payloadResponse = restTemplate.postForEntity(url,requestEntity, UserPayloadDao.class);

            UserPayloadDao userPayloadDao = payloadResponse.getBody();

            //binding to session
        if (userPayloadDao != null) {
            VaadinSession.getCurrent().setAttribute("USER_TOKEN", userPayloadDao.getToken());
            VaadinSession.getCurrent().setAttribute("USERNAME", userPayloadDao.getUsername());
            VaadinSession.getCurrent().setAttribute("FIRSTNAME", userPayloadDao.getFirstName());
            VaadinSession.getCurrent().setAttribute("LASTNAME", userPayloadDao.getLastName());
            VaadinSession.getCurrent().setAttribute("ID", userPayloadDao.getId());
        }

    }


}
