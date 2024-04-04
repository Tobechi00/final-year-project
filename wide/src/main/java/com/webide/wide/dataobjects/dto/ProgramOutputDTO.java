package com.webide.wide.dataobjects.dto;

public class ProgramOutputDTO {

    private String programOutput;

    private int exitCode;

    public String getProgramOutput() {
        return programOutput;
    }

    public void setProgramOutput(String programOutput) {
        this.programOutput = programOutput;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }
}
