package com.webide.wide.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webide.wide.dao.LoginDao;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.interceptorconfig.RestTemplateHeaderModifierInterceptor;
import org.apache.logging.log4j.LogBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.vaadin.firitin.util.WebStorage;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.List;


public class ServerRequestMethods {

    Logger logger = LoggerFactory.getLogger(ServerRequestMethods.class);


    public ProgramOutputDto sendCodeRunRequest(ProgramInputDao programInputDao) throws JsonProcessingException {

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

        return responseEntity.getBody();
    }

    public void sendLoginRequest(LoginDao loginDao) throws HttpClientErrorException {

            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8080/w-ide/api/login";

            HttpEntity<LoginDao> requestEntity = new HttpEntity<>(loginDao);

            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(url,requestEntity, String.class);
            WebStorage.setItem("Token",tokenResponse.getBody());

    }

    //todo: implement a way to deserialize request object and add token header
//    public String addAuthHeader(ProgramInputDao programInputDao) throws JsonProcessingException {
//
//        String currentHeaders = new ObjectMapper().writeValueAsString(programInputDao);
//
//        //!!         String v = "cat";
//        //        String s = "{\"authorization\":\"" + v +"\"}";
//
//        //use the above format to combine the two json objects you can also use object mapper to map them with a map datastructure.
//        StringBuilder token = new StringBuilder("");
//
//        //callback
//        WebStorage.getItem("Token",value ->{
//            String authHeader = (value == null? "<no value stored>" : value );
//        });
//
//        return token+"\n"+ currentHeaders;
//    }

}
