package com.webide.wide.dao;

public class ProgramInputDao {
    private String programmingLanguage;

    private String program;


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
}
