package com.webide.wide.views.custom_components;

import com.vaadin.flow.component.select.Select;
import de.f0rce.ace.enums.AceMode;

import java.util.ArrayList;
import java.util.List;
/**
 * contains custom selector list implementations for languages and sizes
 * */
public class SelectorLists {

    public static Select<AceMode> getLanguageSelector(){

        Select<AceMode> aceModeSelect = new Select<>();
        List<AceMode> list = new ArrayList<>();

        list.add(AceMode.text);
        list.add(AceMode.python);
        list.add(AceMode.lisp);
        list.add(AceMode.java);
        list.add(AceMode.rust);
        list.add(AceMode.javascript);

        aceModeSelect.setItems(list);
        aceModeSelect.setValue(list.get(0));
        aceModeSelect.setLabel("language");
        aceModeSelect.setSizeFull();

        return aceModeSelect;
    }

    public static Select<Integer> getSizeSelector(){
        Select<Integer> integerSelect = new Select<>();
        List<Integer> list = new ArrayList<>();

        list.add(10);
        list.add(15);
        list.add(20);
        list.add(25);
        list.add(30);
        list.add(40);
        list.add(50);

        integerSelect.setItems(list);
        integerSelect.setValue(list.get(0));
        integerSelect.setLabel("font size");
        integerSelect.setSizeFull();

        return integerSelect;
    }
}
