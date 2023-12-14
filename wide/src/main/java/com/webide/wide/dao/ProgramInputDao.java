package com.webide.wide.dao;

import java.util.Optional;

public class ProgramInputDao {
    private String programmingLanguage;

    private String program;

    private Optional<String> userInput;


    public ProgramInputDao(String programmingLanguage, String program,String userInput){
        this.programmingLanguage = programmingLanguage;
        this.program = program;
        this.userInput = Optional.of(userInput);
    }

    public ProgramInputDao(String programmingLanguage, String program){
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
}
