package com.webide.wide.server;

import com.vaadin.flow.server.VaadinSession;
import com.webide.wide.dataobjects.dao.LoginDAO;
import com.webide.wide.dataobjects.dao.ProgramInputDAO;
import com.webide.wide.dataobjects.dto.ProgramOutputDTO;
import com.webide.wide.dataobjects.dao.UserPayloadDAO;
import com.webide.wide.dataobjects.dto.RegistrationDTO;
import com.webide.wide.dataobjects.dto.SaveAsFileDTO;
import com.webide.wide.dataobjects.dto.SaveFileDTO;
import com.webide.wide.interceptorconfig.RestTemplateHeaderModifierInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
    public ProgramOutputDTO sendCodeRunRequest(ProgramInputDAO programInputDao) throws HttpClientErrorException {
        ProgramOutputDTO programOutputDto;
        try {
            RestTemplate restTemplate = new RestTemplate();

            //adding auth header to rest template
            setInterceptors(restTemplate);

            String url = apiURL+"/"+programInputDao.getProgrammingLanguage()+"/exec";
            ResponseEntity<ProgramOutputDTO> responseEntity = restTemplate.postForEntity(url,programInputDao, ProgramOutputDTO.class);

            programOutputDto = responseEntity.getBody();

            return programOutputDto;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    //main login method
    public void sendLoginRequest(LoginDAO loginDao) throws HttpClientErrorException {
            String url = apiURL+"/login";

            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<LoginDAO> requestEntity = new HttpEntity<>(loginDao);
            ResponseEntity<UserPayloadDAO> payloadResponse = restTemplate.postForEntity(url,requestEntity, UserPayloadDAO.class);
            UserPayloadDAO userPayloadDao = payloadResponse.getBody();

            //binding userdata to session
        if (userPayloadDao != null) {
            VaadinSession session = VaadinSession.getCurrent();

            session.setAttribute("USER_TOKEN", userPayloadDao.getToken());
            session.setAttribute("USERNAME", userPayloadDao.getUsername());
            session.setAttribute("FIRSTNAME", userPayloadDao.getFirstName());
            session.setAttribute("LASTNAME", userPayloadDao.getLastName());
            session.setAttribute("ID", userPayloadDao.getId());
        }
    }

    //registration method
    public HttpStatusCode sendRegistrationRequest(RegistrationDTO registrationDTO){
        String url = apiURL+"/register";
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<RegistrationDTO> requestEntity = new HttpEntity<>(registrationDTO);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,requestEntity,String.class);

        return responseEntity.getStatusCode();
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


    //save as
    public void saveFileAs(String oldFilePath,String newFilePath,String fileContent){
        RestTemplate restTemplate = new RestTemplate();
        setInterceptors(restTemplate);

        String url = apiURL+"/files/save-as/"+VaadinSession.getCurrent().getAttribute("ID");
        SaveAsFileDTO saveAsFileDTO = new SaveAsFileDTO(oldFilePath,newFilePath,fileContent);

        HttpEntity<SaveAsFileDTO> httpEntity = new HttpEntity<>(saveAsFileDTO);
        ResponseEntity<String> response = restTemplate.postForEntity(url,httpEntity,String.class);
    }



    //get a list of userfiles
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
