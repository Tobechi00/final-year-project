package com.webide.wide.dataobjects.dao;

import javax.swing.text.html.Option;
import java.util.Optional;

public class ProgramInputDAO {
    private String programmingLanguage;

    private String program;

    private Optional<String> userInput;

    //used to name the java file that will be created on the docker container
    private Optional<String> fileName;


    public ProgramInputDAO(String programmingLanguage, String program, String userInput){
        this.programmingLanguage = programmingLanguage;
        this.program = program;
        this.userInput = Optional.of(userInput);
    }

    public ProgramInputDAO(String programmingLanguage, String program){
        this.programmingLanguage = programmingLanguage;
        this.program = program;
    }


    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Optional<String> getUserInput() {
        return userInput;
    }

    public void setUserInput(Optional<String> userInput) {
        this.userInput = userInput;
    }

    public Optional<String> getFileName() {
        return fileName;
    }

    public void setFileName(Optional<String> fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ProgramInputDAO{" +
                "programmingLanguage='" + programmingLanguage + '\'' +
                ", program='" + program + '\'' +
                ", userInput=" + userInput +
                ", fileName=" + fileName +
                '}';
    }
}
