package com.webide.wide.frontend.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.frontend.custom_components.SelectorLists;
import com.webide.wide.frontend.custom_components.OutputDialog;
import com.webide.wide.frontend.custom_components.TextNote;
import com.webide.wide.server.ServerRequestMethods;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;


import java.util.ArrayList;
import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("mainpage")
public class MainPage extends VerticalLayout implements BeforeEnterObserver {
    AceEditor aceEditor;

    Button runButton,noteButton,dialogueCloseButton;

    TextNote textNote;
    List<AceMode> aceModeList;
    List<Integer> fontSizeList;
    Select<AceMode> aceModeSelector;

    Select<Integer> fontSizeSelector;
    ServerRequestMethods serverRequestMethods;
    ProgramOutputDto programOutputDto;

    HorizontalLayout horizontalLayout;

    SelectorLists selectorLists;
    OutputDialog outputDialog;




    ComponentEventListener<ClickEvent<MenuItem>> listener;

    public MainPage(){

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        aceEditor = new AceEditor();

        aceModeList = new ArrayList<>();
        fontSizeList = new ArrayList<>();


        runButton = new Button();
        noteButton = new Button();
        dialogueCloseButton = new Button();

        textNote = new TextNote();
        outputDialog = new OutputDialog();

        programOutputDto = new ProgramOutputDto();
        serverRequestMethods = new ServerRequestMethods();

        horizontalLayout = new HorizontalLayout();
        selectorLists = new SelectorLists();

        HorizontalLayout aceEditorLayout = new HorizontalLayout();
        aceEditorLayout.setAlignSelf(Alignment.CENTER);
        aceEditorLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        aceEditorLayout.setAlignItems(Alignment.CENTER);
        aceEditorLayout.setSizeFull();

        aceEditor.setTheme(AceTheme.katzenmilch);
        aceEditor.setFontSize(20);
        aceEditor.setAutoComplete(true);
        aceEditor.setLiveAutocompletion(true);

        aceEditorLayout.add(aceEditor);

        aceModeSelector = selectorLists.getLanguageSelector();
        fontSizeSelector = selectorLists.getSizeSelector();


        //save users code to browser local storage
//        aceEditor.addAceChangedListener((value -> {
//
//            CodeStorage.webKeyMap.put("current",aceEditor.getValue());
//        }));


        aceModeSelector.addValueChangeListener(event -> {
            aceEditor.setMode(event.getValue());
        });

        fontSizeSelector.addValueChangeListener(event->{
            aceEditor.setFontSize(event.getValue());
        });


        runButton.setText("run code");
        runButton.addClickListener(event -> {
           programOutputDto = serverRequestMethods.sendPostRequest(new ProgramInputDao("python",aceEditor.getValue()));

           outputDialog.setExitCodeField(programOutputDto.getExitCode());
           outputDialog.setOutputAreaValue(programOutputDto.getProgramOutput());
           outputDialog.open();
        });


        outputDialog.getFooter().add(dialogueCloseButton);


        noteButton.setText("note");
        noteButton.addClickListener(event -> {
            textNote.open();
        });

        horizontalLayout.add(fontSizeSelector,aceModeSelector,runButton,noteButton);
        horizontalLayout.setSizeFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.END);
        horizontalLayout.setPadding(true);



        add(horizontalLayout,aceEditorLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    }
}
