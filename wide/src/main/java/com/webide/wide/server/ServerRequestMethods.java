package com.webide.wide.server;

import com.webide.wide.dao.LoginDao;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ServerRequestMethods {

    public ProgramOutputDto sendPostRequest(ProgramInputDao programInputDao){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/w-ide/api/python/submit";


        HttpEntity<ProgramInputDao> requestEntity = new HttpEntity<>(programInputDao);

        ResponseEntity<ProgramOutputDto> responseEntity = restTemplate.postForEntity(url,requestEntity, ProgramOutputDto.class);

        return responseEntity.getBody();
    }

    //should return session token and save to web storage
    public String loginAndReceiveToken(){

    }
}
