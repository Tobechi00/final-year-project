package com.webide.wide.views.util;

import de.f0rce.ace.enums.AceMode;

import java.util.HashMap;

public class ExtensionMapper {

    private static final HashMap<AceMode,String> aceToExtensionMap = new HashMap<>();
    private static final HashMap<String,AceMode> extensionToAceMap = new HashMap<>();

    private static final HashMap<AceMode,String> aceToDefaultTextMap = new HashMap<>();


    static {
        // Mapping Ace modes to file extensions
        aceToExtensionMap.put(AceMode.python,".py");
        aceToExtensionMap.put(AceMode.lisp,".lsp");
        aceToExtensionMap.put(AceMode.java,".java");
        aceToExtensionMap.put(AceMode.text,".txt");
        aceToExtensionMap.put(AceMode.c_cpp,".c");

        // Mapping file extensions to Ace modes
        extensionToAceMap.put(".py",AceMode.python);
        extensionToAceMap.put(".lsp",AceMode.lisp);
        extensionToAceMap.put(".java",AceMode.java);
        extensionToAceMap.put(".txt",AceMode.text);
        extensionToAceMap.put(".c",AceMode.c_cpp);

        //Mapping Ace modes to default text

        aceToDefaultTextMap.put(
                AceMode.java,
                """
                        import java.util.*;
                        public class Main {
                            public static void main(String[] args) {
                                // Your Code Goes Here
                            }
                        }
                        """
        );

        aceToDefaultTextMap.put(
                AceMode.python,
                "# Your code goes here"
        );

        aceToDefaultTextMap.put(
                AceMode.c_cpp,
                """
                        #include <stdio.h>

                        int main(void) {
                        \t// your code goes here

                        }
                        """
        );

        aceToDefaultTextMap.put(
                AceMode.text,
                "#-- Your Text Goes Here --#"
        );


    }

    public static AceMode getAceModeByExtension(String extension){
        return extensionToAceMap.get(extension);
    }

    public static String getExtensionByAceMode(AceMode aceMode){
        return  aceToExtensionMap.get(aceMode);
    }

    public static String getDefaultTextByAceMode(AceMode aceMode){
        return aceToDefaultTextMap.get(aceMode);
    }


}
