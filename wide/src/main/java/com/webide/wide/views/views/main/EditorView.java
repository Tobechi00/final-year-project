package com.webide.wide.views.views.main;


import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.HttpStatusCode;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.webide.wide.dao.ProgramInputDao;
import com.webide.wide.dao.ProgramOutputDto;
import com.webide.wide.server.ServerRequestMethods;
import com.webide.wide.views.custom_components.CustomNotification;
import com.webide.wide.views.custom_components.SelectorLists;
import com.webide.wide.views.custom_components.TextNote;
import com.webide.wide.views.util.ExtensionMapper;
import com.webide.wide.views.views.loginregistration.LoginView;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.util.Map;


@Route(value = "",layout = MainLayout.class)
@PreserveOnRefresh
@PageTitle("W-ide")
public class EditorView extends VerticalLayout implements BeforeEnterObserver {

    //todo: error with stacking error notifications ensure to rectify, also consider using an arrow to open and close the utility window

    AceEditor aceEditor;
    VerticalLayout sideGutter,ioLayout,selectorLayout;
    Scroller utilityScroller;
    FileDownloadWrapper buttonDownloadWrapper;
    HorizontalLayout utilityButtonLayout;
    Button executeButton,downloadButton,noteButton;
    MenuBar fileButton;
    Select<AceMode> aceModeSelector;
    Select<Integer> fontSizeSelector;
    ExtensionMapper extensionMapper;
    TextArea inputArea;
    H4 currentFileName;
    TextArea outputArea;
    TextNote textNote;
    String currentFilePath;
    SplitLayout splitLayout;
    ServerRequestMethods serverRequestMethods;
    Logger logger = LoggerFactory.getLogger(EditorView.class);
    public EditorView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        aceEditor = new AceEditor();

        aceEditor.setSizeFull();
        aceEditor.setAutoComplete(true);
        aceEditor.setLiveAutocompletion(true);
        aceEditor.setMode(AceMode.text);


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
        serverRequestMethods = new ServerRequestMethods();

        //
        currentFileName = new H4("Untitled Document");
        currentFilePath = "";

        textNote = new TextNote();


        //buttons
        executeButton = new Button("Run");
        downloadButton = new Button("Download");
        noteButton = new Button("Note",buttonClickEvent -> textNote.open());
        fileButton = new MenuBar();
        MenuItem menuItem = fileButton.addItem("file");

        extensionMapper = new ExtensionMapper();


        //wrapping button with a download wrapper
        buttonDownloadWrapper = new FileDownloadWrapper(new StreamResource(currentFileName.getText()+extensionMapper.getExtensionByAceMode(aceEditor.getMode()), () -> new ByteArrayInputStream(aceEditor.getValue().getBytes())));
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

            buttonDownloadWrapper = new FileDownloadWrapper(new StreamResource(currentFileName.getText()+extensionMapper.getExtensionByAceMode(aceEditor.getMode()), () -> new ByteArrayInputStream(aceEditor.getValue().getBytes())));
            buttonDownloadWrapper.wrapComponent(downloadButton);

            utilityButtonLayout.add(buttonDownloadWrapper);
        });

        utilityButtonLayout.add(executeButton,noteButton,fileButton,buttonDownloadWrapper);

        fontSizeSelector.addValueChangeListener(event->
                aceEditor.setFontSize(event.getValue()));

        selectorLayout.add(aceModeSelector,fontSizeSelector);

        executeButton.addClickListener(buttonClickEvent ->
                runCode(aceEditor.getValue(),aceModeSelector.getValue().toString(),outputArea,inputArea));

        //
        SubMenu subMenu = menuItem.getSubMenu();


        subMenu.addItem("Save",menuItemClickEvent ->
                save(currentFileName.getText(),currentFilePath));


        subMenu.addItem("Save As",onClick -> launchSaveDialogue());
        subMenu.addItem("Open Recent",onClick->{
            try {
                ServerRequestMethods serverRequestMethods = new ServerRequestMethods();
                launchRecentFilesDialog(serverRequestMethods.getUserFiles((Long) VaadinSession.getCurrent().getAttribute("ID")),aceEditor);
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        });

        subMenu.addItem("New Project",onClick -> {
            aceEditor.clear();
            currentFileName.setText("Untitled Document");
            currentFilePath = "";
                }
        );




        ioLayout.add(inputArea,outputArea);
        ioLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        //side gutter
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

    /**
     * sends code run request
     * @param code user cade collected from aceEditor
     * @param language language used by user
     * @param outputArea code result output area
     * @param inputArea optional input area
     * */
    public void runCode(String code, String language, TextArea outputArea, TextArea inputArea){
        ServerRequestMethods serverRequestMethods = new ServerRequestMethods();
        ProgramOutputDto programOutputDto;

            try {
                if (inputArea.getValue().isEmpty()) {
                    //if code does not require input during execution
                    programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDao(language, code));
                }else {
                    //if code requires input
                    programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDao(language,code,inputArea.getValue()));
                }

                String output = programOutputDto.getProgramOutput()+"\n"+"Exit Code: "+programOutputDto.getExitCode();

                //edit environmental details based on received error codes
                if (programOutputDto.getExitCode() == 0){
                    outputArea.getStyle().remove("color");
                    outputArea.setReadOnly(false);
                    outputArea.setValue(output);
                    new CustomNotification("Program has been executed successfully", NotificationVariant.LUMO_SUCCESS).open();
                }else if(programOutputDto.getExitCode() >= 0){
                    outputArea.setValue(output);
                    outputArea.setReadOnly(false);
                    outputArea.getStyle().setColor("red");
                    new CustomNotification("Program has produced an error",NotificationVariant.LUMO_ERROR).open();
                }else if (programOutputDto.getExitCode() != HttpStatusCode.EXPECTATION_FAILED.getCode()){
                    outputArea.setValue(output);
                    outputArea.setReadOnly(false);
                    outputArea.getStyle().setColor("yellow");
                    new CustomNotification("Program took too long to execute",NotificationVariant.LUMO_WARNING).open();
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
                new CustomNotification("Error: Connection to the server could not be established", NotificationVariant.LUMO_ERROR).open();
            }
    }


    //save content to existing file
    public void save(String currentFileName,String currentFilePath){
        if ( !currentFileName.isEmpty() || !currentFilePath.isEmpty()){
            try {
                serverRequestMethods.saveFile(currentFilePath,aceEditor.getValue());
            }catch (Exception e){
                //todo: create error dialogue
            }
        }else{
            new CustomNotification("you are attempting to save content to a file that doesn't exist",NotificationVariant.LUMO_WARNING);
        }
    }

    //save name can't be untitled document!!!
    public void launchSaveDialogue(){
        ConfirmDialog dialog = new ConfirmDialog();
        TextField fileNameField  = new TextField();

        ServerRequestMethods serverRequestMethods = new ServerRequestMethods();

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.addClickListener(buttonClickEvent -> {
            try {
                ExtensionMapper extensionMapper = new ExtensionMapper();
                String basePath = "C:\\Users\\tobec\\ServerData\\user-files\\";
                String completePath = basePath+fileNameField.getValue()+extensionMapper.getExtensionByAceMode(aceEditor.getMode());

                serverRequestMethods.saveFileAs(currentFilePath,completePath,aceEditor.getValue());

                currentFilePath = basePath+fileNameField.getValue();
                currentFileName.setText(fileNameField.getValue()+extensionMapper.getExtensionByAceMode(aceEditor.getMode()));
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        });

        fileNameField.setLabel("File Name");
        dialog.setHeader("Save As");
        dialog.setConfirmButton(saveButton);
        dialog.setCancelButton(cancelButton);
        dialog.add(fileNameField);

        if (fileNameField.isEmpty()){
            saveButton.setEnabled(false);
        }

        //enable save button only when a file name has been entered
        fileNameField.addValueChangeListener(onChange->
                saveButton.setEnabled(!onChange.getValue().isEmpty()));

        dialog.open();
    }


    //dialog for recent files
    public void launchRecentFilesDialog(Map<String,String> fileList,AceEditor aceEditor){
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Recent Files");
        ServerRequestMethods serverRequestMethods = new ServerRequestMethods();
        StringBuilder stringBuilder = new StringBuilder();


        ListBox<String> listBox = new ListBox<>();
        listBox.addValueChangeListener(value->{
            stringBuilder.setLength(0);
            stringBuilder.append(fileList.get(value.getValue()));
        });
        listBox.setItems(fileList.keySet().stream().toList());
        dialog.add(listBox);

        //todo:extract to method
        Button openFileButton = new Button("open");


        openFileButton.addClickListener(buttonClickEvent -> {
            if (!stringBuilder.isEmpty()) {
                try {
                    aceEditor.setValue(serverRequestMethods.getFileContent(stringBuilder.toString()));

                    //setting current filepath and name
                    currentFilePath = stringBuilder.toString();
                    currentFileName.setText(stringBuilder.substring(stringBuilder.lastIndexOf("\\")+1));
                    setAceModeByFileExtension(stringBuilder.substring(stringBuilder.indexOf(".")),aceModeSelector);

                    dialog.close();
                }catch (Exception e){
                    //log error
                }
            }
        });

        dialog.getFooter().add(openFileButton);
        dialog.open();
    }

    public void setAceModeByFileExtension(String extension,Select<AceMode> selector){
        extensionMapper = new ExtensionMapper();
        selector.setValue(extensionMapper.getAceModeByExtension(extension));
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (VaadinSession.getCurrent().getAttribute("USER_TOKEN") == null){
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }

}
