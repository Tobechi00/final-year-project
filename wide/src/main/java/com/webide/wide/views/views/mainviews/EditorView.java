package com.webide.wide.views.views.mainviews;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.server.ServerRequestMethods;
import com.webide.wide.views.custom_components.CustomNotification;
import com.webide.wide.views.custom_components.PlMaps;
import com.webide.wide.views.custom_components.SelectorLists;
import com.webide.wide.views.custom_components.TextNote;
import com.webide.wide.views.views.loginregistrationviews.LoginView;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import elemental.json.JsonValue;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;


@Route(value = "",layout = MainLayout.class)
@PreserveOnRefresh
@PageTitle("W-ide")
public class EditorView extends VerticalLayout implements BeforeEnterObserver {

    //todo: error with stacking error notifications ensure to rectify, also consider using an arrow to open and close the utility window
    //todo: store user id with token

    AceEditor aceEditor;
    VerticalLayout sideGutter,ioLayout,selectorLayout;
    Scroller utilityScroller;
    FileDownloadWrapper buttonDownloadWrapper;
    HorizontalLayout utilityButtonLayout;
    Button executeButton,downloadButton,noteButton;
    Select<AceMode> aceModeSelector;
    Select<Integer> fontSizeSelector;
    PlMaps plMaps;
    TextArea inputArea;
    H4 currentFileName;
    TextArea outputArea;
    TextNote textNote;
    SplitLayout splitLayout;
    public EditorView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        aceEditor = new AceEditor();

        aceEditor.setSizeFull();
        aceEditor.setAutoComplete(true);
        aceEditor.setLiveAutocompletion(true);
        aceEditor.setMode(AceMode.python);


        //layouts
        sideGutter = new VerticalLayout();
        utilityButtonLayout = new HorizontalLayout();
        selectorLayout = new VerticalLayout();
        ioLayout = new VerticalLayout();
        utilityScroller = new Scroller();

        //text areas
        inputArea = new TextArea();
        outputArea = new TextArea();

        //
        currentFileName = new H4("untitled document");
        textNote = new TextNote();
        plMaps = new PlMaps();


        //buttons
        executeButton = new Button("Run");
        downloadButton = new Button("Download");

        noteButton = new Button("Note",buttonClickEvent -> textNote.open());

        buttonDownloadWrapper = new FileDownloadWrapper(new StreamResource(currentFileName.getText()+plMaps.getExtensionByAcemode(aceEditor.getMode()), () -> new ByteArrayInputStream(aceEditor.getValue().getBytes())));
        buttonDownloadWrapper.wrapComponent(downloadButton);


        inputArea.setLabel("Input");
        inputArea.setMaxHeight(100,Unit.PIXELS);
        inputArea.setMinHeight(100,Unit.PIXELS);
        inputArea.setSizeFull();

        outputArea.setLabel("Output");
        outputArea.setMinHeight(270, Unit.PIXELS);
        outputArea.setMaxHeight(270,Unit.PIXELS);
        outputArea.setSizeFull();
        outputArea.setReadOnly(true);



        aceModeSelector = SelectorLists.getLanguageSelector();
        fontSizeSelector = SelectorLists.getSizeSelector();

        aceModeSelector.addValueChangeListener(event ->{
            aceEditor.setMode(event.getValue());
            utilityButtonLayout.remove(buttonDownloadWrapper);

            buttonDownloadWrapper = new FileDownloadWrapper(new StreamResource(currentFileName.getText()+plMaps.getExtensionByAcemode(aceEditor.getMode()), () -> new ByteArrayInputStream(aceEditor.getValue().getBytes())));
            buttonDownloadWrapper.wrapComponent(downloadButton);

            utilityButtonLayout.add(buttonDownloadWrapper);
        });

        utilityButtonLayout.add(executeButton,noteButton,buttonDownloadWrapper);
//        utilityButtonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        fontSizeSelector.addValueChangeListener(event-> aceEditor.setFontSize(event.getValue()));

        selectorLayout.add(aceModeSelector,fontSizeSelector);
//        selectorLayout.setJustifyContentMode(JustifyContentMode.CENTER);


        executeButton.addClickListener(buttonClickEvent -> {
            runCode(aceEditor.getValue(),aceModeSelector.getValue().toString(),outputArea,inputArea);
        });

        ioLayout.add(inputArea,outputArea);
        ioLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        sideGutter.setAlignItems(Alignment.CENTER);
        sideGutter.setJustifyContentMode(JustifyContentMode.CENTER);
        sideGutter.add(currentFileName,utilityButtonLayout,selectorLayout,ioLayout);
        sideGutter.setMinWidth(400,Unit.PIXELS);
        sideGutter.setPadding(true);

        utilityScroller = new Scroller(sideGutter);
        utilityScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        utilityScroller.setMaxWidth(800,Unit.PIXELS);

        splitLayout = new SplitLayout(aceEditor, utilityScroller);
        splitLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        splitLayout.setSizeFull();
        //sets position 75:25
        splitLayout.setSplitterPosition(75);


        add(splitLayout);
    }

    //todo: remove text area from method parameters
    public void runCode(String code,String language,TextArea outputArea,TextArea inputArea){
        ServerRequestMethods serverRequestMethods = new ServerRequestMethods();

            try {
                ProgramOutputDto programOutputDto;
                if (inputArea.getValue().isEmpty()) {
                    programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDao(language, code));
                }else {
                    //incase of user input append input to run after code is executed
                    programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDao(language,code,inputArea.getValue()));
                }

                String output = programOutputDto.getProgramOutput()+"\n"+"Exit Code: "+programOutputDto.getExitCode();

                if (programOutputDto.getExitCode() == 0){
                    outputArea.getStyle().remove("color");
                    outputArea.setReadOnly(false);
                    outputArea.setValue(output);
                    new CustomNotification("Program has been executed successfully", NotificationVariant.LUMO_SUCCESS).open();
                }else if(programOutputDto.getExitCode() >= 0 && programOutputDto.getExitCode() != 555){
                    outputArea.setValue(output);
                    outputArea.setReadOnly(false);
                    outputArea.getStyle().setColor("red");
                    new CustomNotification("Program has produced an error",NotificationVariant.LUMO_ERROR).open();
                }else {
                    outputArea.setValue(output);
                    outputArea.setReadOnly(false);
                    outputArea.getStyle().setColor("yellow");
                    new CustomNotification("Program took too long to execute",NotificationVariant.LUMO_WARNING).open();
                }

            }catch (Exception e){
                new CustomNotification("Error: Connection to the server could not be established", NotificationVariant.LUMO_ERROR).open();
            }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (VaadinSession.getCurrent().getAttribute("Token") == null){
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }
}
