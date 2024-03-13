package com.webide.wide.views.util;

import de.f0rce.ace.enums.AceMode;

import java.util.HashMap;

public class ExtensionMapper {

    private HashMap<AceMode,String> aceExtensionMap = new HashMap<>();
    private HashMap<String,AceMode> extensionAceMap = new HashMap<>();

    public ExtensionMapper(){
        // Mapping Ace modes to file extensions
        aceExtensionMap.put(AceMode.python,".py");
        aceExtensionMap.put(AceMode.lisp,".lsp");
        aceExtensionMap.put(AceMode.java,".java");
        aceExtensionMap.put(AceMode.text,".txt");

        // Mapping file extensions to Ace modes
        extensionAceMap.put(".py",AceMode.python);
        extensionAceMap.put(".lsp",AceMode.lisp);
        extensionAceMap.put(".java",AceMode.java);
        extensionAceMap.put(".txt",AceMode.text);
    }

    public AceMode getAceModeByExtension(String extension){
        return extensionAceMap.get(extension);
    }

    public String getExtensionByAceMode(AceMode aceMode){
        return  aceExtensionMap.get(aceMode);
    }


}
