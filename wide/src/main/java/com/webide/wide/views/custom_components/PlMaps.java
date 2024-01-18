package com.webide.wide.views.custom_components;

import de.f0rce.ace.enums.AceMode;

import java.util.HashMap;

public class PlMaps {
    private final HashMap<AceMode, String> plMap = new HashMap<>();
    public PlMaps() {
        plMap.put(AceMode.python,".py");
        plMap.put(AceMode.lisp,".lsp");
        plMap.put(AceMode.java,".java");
        plMap.put(AceMode.text,".txt");
    }

    public String getExtensionByAcemode(AceMode aceMode){
        return plMap.get(aceMode);
    }
}
