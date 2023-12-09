package com.webide.wide.frontend.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.frontend.custom_components.CustomNotification;
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

    HorizontalLayout horizontalLayout,aceEditorLayout;

    VerticalLayout ioLayout,parentLayout;

    SelectorLists selectorLists;
    OutputDialog outputDialog;
    TextArea inputArea,outputArea;


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

        inputArea = new TextArea();
        outputArea = new TextArea();

        horizontalLayout = new HorizontalLayout();
        parentLayout = new VerticalLayout();
        ioLayout = new VerticalLayout();
        aceEditorLayout = new HorizontalLayout();

        selectorLists = new SelectorLists();

        inputArea.setLabel("(optional) Input");
        outputArea.setLabel("Output");

        inputArea.setSizeFull();
        outputArea.setSizeFull();

        inputArea.setMaxHeight("150px");
        outputArea.setMaxHeight("150px");

        outputArea.setEnabled(false);

        aceEditorLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        aceEditorLayout.setAlignItems(Alignment.CENTER);
        aceEditorLayout.setSizeFull();

        aceEditor.setTheme(AceTheme.dreamweaver);
//        aceEditor.setFontSize(20);
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

            try {
                programOutputDto = serverRequestMethods.sendPostRequest(new ProgramInputDao("python",aceEditor.getValue()));

                outputDialog.setExitCodeField(programOutputDto.getExitCode());
                outputDialog.setOutputAreaValue(programOutputDto.getProgramOutput());
                outputDialog.open();
            }catch (Exception e){
                new CustomNotification("Error: Connection to the server could not be established", NotificationVariant.LUMO_ERROR).open();
            }
        });


        outputDialog.getFooter().add(dialogueCloseButton);


        noteButton.setText("note");
        noteButton.addClickListener(event -> {
            textNote.open();
        });

        horizontalLayout.add(fontSizeSelector,aceModeSelector,runButton,noteButton);
        horizontalLayout.setSizeFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
//        horizontalLayout.setPadding(true);

        ioLayout.add(inputArea,outputArea);

        parentLayout.add(horizontalLayout,aceEditorLayout,ioLayout);
        parentLayout.setAlignItems(Alignment.CENTER);
        parentLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        parentLayout.setMaxWidth("900px");
        parentLayout.setPadding(true);
        parentLayout.setSizeFull();

        add(parentLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    }
}
