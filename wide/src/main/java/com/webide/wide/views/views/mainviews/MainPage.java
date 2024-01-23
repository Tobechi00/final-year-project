package com.webide.wide.views.views.mainviews;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.views.custom_components.*;
import com.webide.wide.server.ServerRequestMethods;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

//TODO: ADD FILE DOWNLOAD TO DOWNLOAD CURRENT CODE
@Route(value = "sss", layout = MainLayout.class)
@PreserveOnRefresh
@PageTitle("mainpage")
public class MainPage extends VerticalLayout implements BeforeEnterObserver {
    AceEditor aceEditor;

    Button runButton,noteButton,dialogueCloseButton,downloadCurrentButton;

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

    FileDownloadWrapper buttonDownloadWrapper;

    //disconnect button to convert to floating window

    ComponentEventListener<ClickEvent<MenuItem>> listener;

    public MainPage(){

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        aceEditor = new AceEditor();

        aceModeList = new ArrayList<>();
        fontSizeList = new ArrayList<>();


        runButton = new Button();
        noteButton = new Button();

        downloadCurrentButton = new Button("download");
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

        outputArea.getLabel();

        inputArea.setSizeFull();
        outputArea.setSizeFull();

        inputArea.setMaxHeight("150px");
        outputArea.setMaxHeight("150px");

        outputArea.setReadOnly(true);

        outputArea.setMaxHeight(outputArea.getHeight());

        aceEditorLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        aceEditorLayout.setAlignItems(Alignment.CENTER);
        aceEditorLayout.setSizeFull();

        aceEditor.setTheme(AceTheme.cloud9_night);

        //todo: implement way to set ace mode based on previous sessions
        aceEditor.setMode(AceMode.python);
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


        aceModeSelector.addValueChangeListener(event -> aceEditor.setMode(event.getValue()) );

        fontSizeSelector.addValueChangeListener(event-> aceEditor.setFontSize(event.getValue()));

        runButton.setText("run code");
        runButton.addClickListener(event -> {

            try {

                if (inputArea.getValue().isEmpty()) {
                    programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDao("python", aceEditor.getValue()));
                }else {

                    //incase of user input append input to run after code is executed
                    programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDao("python",aceEditor.getValue(),inputArea.getValue()));
                }


                String output = programOutputDto.getProgramOutput()+"\n"+"Exit Code: "+programOutputDto.getExitCode();

                if (programOutputDto.getExitCode() == 0){
                    outputArea.getStyle().remove("color");
                    outputArea.setReadOnly(false);
                    outputArea.setValue(output);
                    new CustomNotification("Program has been executed successfully",NotificationVariant.LUMO_SUCCESS).open();
                }else if(programOutputDto.getExitCode() >= 0 && programOutputDto.getExitCode() != 555){
                    outputArea.setValue(output);
                    outputArea.setReadOnly(false);
                    outputArea.getStyle().setColor("red");
                    new CustomNotification("Program has been executed with an error",NotificationVariant.LUMO_ERROR).open();
                }else {
                    outputArea.setValue(output);
                    outputArea.setReadOnly(false);
                    outputArea.getStyle().setColor("yellow");
                    new CustomNotification("Program took too long to execute",NotificationVariant.LUMO_WARNING).open();
                }

            }catch (Exception e){
                new CustomNotification("Error: Connection to the server could not be established", NotificationVariant.LUMO_ERROR).open();
            }
        });



        outputDialog.getFooter().add(dialogueCloseButton);


        noteButton.setText("note");
        noteButton.addClickListener(event -> textNote.open());

        //mapping ace mode and file extension
        PlMaps plMaps = new PlMaps();
        buttonDownloadWrapper = new FileDownloadWrapper(new StreamResource("code"+plMaps.getExtensionByAcemode(aceEditor.getMode()), () -> new ByteArrayInputStream(aceEditor.getValue().getBytes())));
        buttonDownloadWrapper.wrapComponent(downloadCurrentButton);

        horizontalLayout.add(fontSizeSelector,aceModeSelector,runButton,buttonDownloadWrapper,noteButton);
        horizontalLayout.setSizeFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        ioLayout.add(inputArea,outputArea);

        parentLayout.add(horizontalLayout,aceEditorLayout,ioLayout);
        parentLayout.setAlignItems(Alignment.START);
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
