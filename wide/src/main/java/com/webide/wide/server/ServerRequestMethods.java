package com.webide.wide.server;

import com.vaadin.flow.server.VaadinSession;
import com.webide.wide.dao.LoginDao;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.dao.UserPayloadDao;
import com.webide.wide.interceptorconfig.RestTemplateHeaderModifierInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerRequestMethods {

    String apiURL = "http://localhost:8080/w-ide/api";


    //sends a request to run code
    public ProgramOutputDto sendCodeRunRequest(ProgramInputDao programInputDao) throws HttpClientErrorException {
        ProgramOutputDto programOutputDto;
        try {
            RestTemplate restTemplate = new RestTemplate();

            //adding auth header to rest template
            setInterceptors(restTemplate);

            String url = apiURL+"/python/submit";
            ResponseEntity<ProgramOutputDto> responseEntity = restTemplate.postForEntity(url,programInputDao, ProgramOutputDto.class);

            programOutputDto = responseEntity.getBody();

            return programOutputDto;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    //main login method
    public void sendLoginRequest(LoginDao loginDao) throws HttpClientErrorException {
            String url = apiURL+"/login";

            RestTemplate restTemplate = new RestTemplate();

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

    //save
    public void saveFile(String fileName,String fileContent){
        RestTemplate restTemplate = new RestTemplate();
        setInterceptors(restTemplate);

        String url = apiURL+"/files/save/"+VaadinSession.getCurrent().getAttribute("ID");

        SaveFileDTO saveFileDTO = new SaveFileDTO(fileName,fileContent);
        HttpEntity<SaveFileDTO> httpEntity = new HttpEntity<>(saveFileDTO);

        ResponseEntity<String> response = restTemplate.postForEntity(url,httpEntity,String.class);
    }
    //custom file dto
    record SaveFileDTO(String fileName,String fileContent){}


    //save as
    public void saveFileAs(String oldFilePath,String newFilePath,String fileContent){
        RestTemplate restTemplate = new RestTemplate();
        setInterceptors(restTemplate);

        String url = apiURL+"/files/save-as/"+VaadinSession.getCurrent().getAttribute("ID");
        SaveAsFileDTO saveAsFileDTO = new SaveAsFileDTO(oldFilePath,newFilePath,fileContent);

        HttpEntity<SaveAsFileDTO> httpEntity = new HttpEntity<>(saveAsFileDTO);
        ResponseEntity<String> response = restTemplate.postForEntity(url,httpEntity,String.class);
    }

    record SaveAsFileDTO(String oldFilePath,String newFilePath,String fileContent){}


    //get a list of userfiles
    //request entity is null because get request
    //ParameterizedTypeReference<List<String>> tells RestTemplate to expect a list of strings in the response body.
    public Map<String,String> getUserFiles(Long userId){
        RestTemplate restTemplate = new RestTemplate();
        String url =  apiURL+"/files/getAllFiles/"+userId;

        setInterceptors(restTemplate);
        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {});

        List<String> fileNameList;
        Map<String,String> filePathMap = new HashMap<>();
        fileNameList = responseEntity.getBody();

        assert fileNameList != null;
        for (String s : fileNameList){
            filePathMap.put(extractFileName(s),s);
        }

        return filePathMap;
    }


    //retrieves file content
    public String getFileContent(String path){
        RestTemplate restTemplate = new RestTemplate();
        String url = apiURL+"/files/getFileContent?path="+path;
        setInterceptors(restTemplate);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
        return responseEntity.getBody();
    }

    //adds token to header
    public void setInterceptors(RestTemplate restTemplate){
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }

        interceptors.add(new RestTemplateHeaderModifierInterceptor());
        restTemplate.setInterceptors(interceptors);
    }

    public String extractFileName(String path){
        int beginning = path.lastIndexOf("\\")+1;
        int end = path.length();

        path = path.substring(beginning,end);
        return path;
    }

}
