package com.webide.wide.dataobjects.dao;

import java.util.Optional;

public class ProgramInputDAO {
    private String programmingLanguage;

    private String program;

    private Optional<String> userInput;


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


    @Override
    public String toString() {
        return "ProgramInputDao{" +
                "programmingLanguage='" + programmingLanguage + '\'' +
                ", program='" + program + '\'' +
                ", userInput=" + userInput +
                '}';
    }
}
