package com.webide.wide.views.views.main;


import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.HttpStatusCode;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.webide.wide.dataobjects.dao.ProgramInputDAO;
import com.webide.wide.dataobjects.dto.ProgramOutputDTO;
import com.webide.wide.server.ServerRequestMethods;
import com.webide.wide.views.customcomponents.CustomNotification;
import com.webide.wide.views.customcomponents.SelectorLists;
import com.webide.wide.views.customcomponents.TextNote;
import com.webide.wide.views.util.ExtensionMapper;
import com.webide.wide.views.views.loginregistration.LoginView;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.*;
import java.util.Map;
import java.util.Optional;


@Route(value = "")
@PreserveOnRefresh
@PageTitle("W-ide")
public class EditorView extends VerticalLayout implements BeforeEnterObserver {

    //todo: error with stacking error notifications ensure to rectify, also consider using an arrow to open and close the utility window

    AceEditor aceEditor, outputArea;
    MenuItem menuItem;
    SubMenu subMenu;
    Avatar avatar;
    VerticalLayout bottomGutter,ioLayout,selectorLayout;
    HorizontalLayout navLayout,navStart,navCenter,navEnd,utilityButtonLayout;
    Icon runIcon,downloadIcon,noteIcon,settingIcon,fileIcon,minimizeIcon;
    Scroller utilityScroller;
    FileDownloadWrapper buttonDownloadWrapper;
    Button minimizeButton,executeButton,downloadButton,noteButton,settingsButton;
    MenuBar fileButton;
    Select<AceMode> aceModeSelector;
    Select<Integer> fontSizeSelector;
    TextArea inputArea;
    H4 currentFileName;
    TextNote textNote;
    String currentFilePath,firstName,lastName;
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
        aceEditor.setDisplayIndentGuides(true);
        aceEditor.setTheme(AceTheme.nord_dark);
        aceEditor.setShowInvisibles(true);
        aceEditor.setValue(ExtensionMapper.getDefaultTextByAceMode(
                aceEditor.getMode())
        );

        //layouts
        bottomGutter = new VerticalLayout();
        selectorLayout = new VerticalLayout();
        ioLayout = new VerticalLayout();

        utilityScroller = new Scroller();

        utilityButtonLayout = new HorizontalLayout();
        navLayout = new HorizontalLayout();
        navStart = new HorizontalLayout();
        navCenter = new HorizontalLayout();
        navEnd = new HorizontalLayout();

        inputArea = new TextArea();

        serverRequestMethods = new ServerRequestMethods();


        currentFileName = new H4("Untitled Document");
        //maps to the full path of the currently open file
        currentFilePath = "";

        textNote = new TextNote();

        //icons runIcon,downloadIcon,noteIcon,SettingIcon,FileIcon;
        runIcon = LumoIcon.PLAY.create();
        downloadIcon = LumoIcon.DOWNLOAD.create();
        noteIcon = VaadinIcon.NOTEBOOK.create();
        settingIcon = LumoIcon.COG.create();
        fileIcon = VaadinIcon.FOLDER.create();
        minimizeIcon = LumoIcon.MINUS.create();

        runIcon.setColor("green");
        downloadIcon.setColor("white");
        noteIcon.setColor("white");
        settingIcon.setColor("white");
        fileIcon.setColor("white");
        minimizeIcon.setColor("white");


        //buttons
        executeButton = new Button(runIcon);
        settingsButton = new Button(settingIcon);
        downloadButton = new Button(downloadIcon);
        noteButton = new Button(noteIcon,buttonClickEvent -> textNote.open());
        fileButton = new MenuBar();
        minimizeButton = new Button(minimizeIcon);
        menuItem = fileButton.addItem(fileIcon);


        //wrapping button with a download wrapper
        buttonDownloadWrapper = new FileDownloadWrapper(
                new StreamResource(
                        currentFileName.getText()+ExtensionMapper.getExtensionByAceMode(
                                aceEditor.getMode()),
                        () -> new ByteArrayInputStream(aceEditor.getValue().getBytes()))
        );
        buttonDownloadWrapper.wrapComponent(downloadButton);

        inputArea.setPlaceholder("Input");
        inputArea.setMaxHeight(100,Unit.PIXELS);
        inputArea.setSizeFull();

        aceModeSelector = SelectorLists.getLanguageSelector();
        fontSizeSelector = SelectorLists.getSizeSelector();

        aceModeSelector.addValueChangeListener(event ->{

            try {

                launchAceModeChangeDialog(event.getValue(),aceEditor);

                //button needs to be removed before being added again
                utilityButtonLayout.remove(buttonDownloadWrapper);

                buttonDownloadWrapper = new FileDownloadWrapper(
                        new StreamResource(
                                currentFileName.getText()+ExtensionMapper.getExtensionByAceMode(aceEditor.getMode()),
                                () -> new ByteArrayInputStream(aceEditor.getValue().getBytes())
                        ));
                buttonDownloadWrapper.wrapComponent(downloadButton);

                utilityButtonLayout.add(buttonDownloadWrapper);
            }catch (Exception e){
                logger.warn(e.getMessage());
            }

        });

        utilityButtonLayout.add(executeButton,noteButton,fileButton,buttonDownloadWrapper);

        fontSizeSelector.addValueChangeListener(event->
                aceEditor.setFontSize(event.getValue()));

        selectorLayout.add(aceModeSelector,fontSizeSelector);

        executeButton.addClickListener(buttonClickEvent ->
                runCode(
                        aceEditor.getValue(),
                        aceModeSelector.getValue(),
                        outputArea,inputArea,
                        currentFilePath,
                        currentFileName.getText())
        );

        settingsButton.addClickListener(buttonClickEvent -> launchSettingsDialog(selectorLayout));



        subMenu = menuItem.getSubMenu();

        subMenu.addItem("Save",menuItemClickEvent ->
                save(currentFileName.getText(),currentFilePath));

        subMenu.addItem("Save As",onClick -> launchSaveDialogue());

        subMenu.addItem("Open Recent",onClick->{
            try {
                ServerRequestMethods serverRequestMethods = new ServerRequestMethods();
                launchRecentFilesDialog(
                        serverRequestMethods.getUserFiles(
                                (Long) VaadinSession.getCurrent().getAttribute("ID")),
                        aceEditor);
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        });

        subMenu.addItem("Upload File",onClick -> launchFileUploadDialogue(
                currentFileName,
                aceEditor,
                aceModeSelector,
                currentFilePath
                ));

        subMenu.addItem("New Project",onClick -> {
            aceEditor.setValue(
                    ExtensionMapper.getDefaultTextByAceMode(
                    aceModeSelector.getValue()
            ));
            try {
                currentFileName.setText("Untitled Document");
            }catch (Exception e){
                logger.error(e.getMessage());
            }
            currentFilePath = "";
                });

        outputArea = new AceEditor();
        outputArea.setShowGutter(false);
        outputArea.setTheme(aceEditor.getTheme());
        outputArea.setHighlightActiveLine(false);
        outputArea.setReadOnly(true);
        outputArea.setMode(AceMode.text);
        outputArea.setValue("Output");

        ioLayout.add(inputArea, outputArea);
        ioLayout.setJustifyContentMode(JustifyContentMode.START);
        ioLayout.setAlignItems(Alignment.START);

        bottomGutter.setAlignItems(Alignment.START);
        bottomGutter.setJustifyContentMode(JustifyContentMode.START);
        bottomGutter.add(minimizeButton,ioLayout);

        bottomGutter.setHorizontalComponentAlignment(Alignment.END,minimizeButton);

        utilityScroller = new Scroller(bottomGutter);
        utilityScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);

        utilityScroller.setMaxHeight(400,Unit.PIXELS);

        splitLayout = new SplitLayout(aceEditor, utilityScroller);
        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        splitLayout.setSizeFull();
        //sets position 70:30
        splitLayout.setSplitterPosition(70);

        //temp fix for splitter not closing
        splitLayout.addSplitterDragendListener(splitterDragendEvent ->
                splitLayout.setSplitterPosition(splitLayout.getSplitterPosition()));

        minimizeButton.addClickListener(buttonClickEvent ->{
            if (splitLayout.getSplitterPosition() != 100){
                splitLayout.setSplitterPosition(100);
            }
        });

        navLayout.setWidthFull();

        navStart.add(fileButton,currentFileName);
        navStart.setWidthFull();
        navStart.setJustifyContentMode(JustifyContentMode.START);
        navStart.setAlignSelf(Alignment.START);
        navStart.setVerticalComponentAlignment(Alignment.CENTER,currentFileName);

        navCenter.add(executeButton,settingsButton,noteButton,buttonDownloadWrapper);
        navCenter.setWidthFull();
        navCenter.setJustifyContentMode(JustifyContentMode.CENTER);
        navCenter.setAlignSelf(Alignment.CENTER);

        firstName = (String) VaadinSession.getCurrent().getAttribute("FIRSTNAME");
        lastName = (String) VaadinSession.getCurrent().getAttribute("LASTNAME");

        avatar = new Avatar(firstName+" "+lastName);
        avatar.getStyle().setColor("white");

        navEnd.add(avatar);
        navEnd.setWidthFull();
        navEnd.setJustifyContentMode(JustifyContentMode.END);
        navEnd.setAlignSelf(Alignment.END);
        navEnd.setVerticalComponentAlignment(Alignment.CENTER,avatar);

        navLayout.setVerticalComponentAlignment(Alignment.CENTER,navStart);
        navLayout.add(navStart,navCenter,navEnd);
        add(navLayout,splitLayout);
    }

    /**
     * sends code run request
     * @param code user cade collected from aceEditor
     * @param language language used by user
     * @param outputArea code result output area
     * @param inputArea optional input area
     * */
    private void runCode(
            String code,
            AceMode language,
            AceEditor outputArea,
            TextArea inputArea,
            String currentFilePath,
            String currentFileName){

        ServerRequestMethods serverRequestMethods = new ServerRequestMethods();

        switch (language){
            case java ->{

                if (currentFilePath.isEmpty()){
                    runJavaCode(
                            code,
                            inputArea,
                            outputArea,
                            serverRequestMethods,
                            Optional.empty());
                }else {
                    runJavaCode(
                            code,
                            inputArea,
                            outputArea,
                            serverRequestMethods,
                            Optional.of(currentFileName));
                }
            }
            case python ->
                runPythonCode(
                        code,
                        inputArea,
                        outputArea,
                        serverRequestMethods
                );
            case text ->
                    new CustomNotification(
                    "It Is Not Possible To Compile Text",
                    NotificationVariant.LUMO_ERROR
                    ).open();

            case c_cpp ->
                runCCode(
                        code,
                        inputArea,
                        outputArea,
                        serverRequestMethods
                );
            default ->
                    new CustomNotification(
                    "Compilation For This Language Isn't Available",
                    NotificationVariant.LUMO_ERROR
            ).open();

        }
    }

    private void runJavaCode(
            String code,
            TextArea inputArea,
            AceEditor outputArea,
            ServerRequestMethods serverRequestMethods,
            Optional<String> fileName
    ){
        ProgramOutputDTO programOutputDto;
        String language = AceMode.java.toString();

        code = code.replace("\"","\\\"")
                .replace("'", "'\\''");



        String output;
        try {
            if (inputArea.getValue().isEmpty()) {
                //if code does not require input during execution
                ProgramInputDAO programInputDAO = new ProgramInputDAO(language, code);
                programInputDAO.setFileName(fileName);

                programOutputDto = serverRequestMethods.sendCodeRunRequest(programInputDAO);

                output = programOutputDto.getProgramOutput() + "\n"
                        + "Exit Code: " + programOutputDto.getExitCode() + "\n"
                        + "Execution Time: "+nanoSecondsToSeconds(
                        programOutputDto.getExecutionTime(), 4);
            } else {

                ProgramInputDAO programInputDAO = new ProgramInputDAO(
                        language, code, inputArea.getValue());

                programInputDAO.setFileName(fileName);
                programOutputDto = serverRequestMethods.sendCodeRunRequest(programInputDAO);

                String[] outputArray = programOutputDto.getProgramOutput().split("\\r?\\n");
                StringBuilder sb = new StringBuilder();

                int i = 0;
                for (String s : outputArray){
                    if (i%2 == 0) {
                        sb.append(s).append("\n");
                    }
                    i++;
                }
                sb.append("Exit Code: ")
                        .append(programOutputDto.getExitCode())
                        .append("\n")
                        .append("Execution Time: ")
                        .append(nanoSecondsToSeconds(
                                programOutputDto.getExecutionTime(), 4));
                output = sb.toString();
            }



            //edit environmental details based on received error codes
            displayProgramExecutionStatus(
                    programOutputDto.getExitCode(),
                    output,outputArea);

        } catch (Exception e) {
            logger.error(e.getMessage());
            new CustomNotification(
                    "An Error Occurred While Connecting To Server",
                    NotificationVariant.LUMO_ERROR).open();
        }
    }

    private void runPythonCode(
            String code,
            TextArea inputArea,
            AceEditor outputArea,
            ServerRequestMethods serverRequestMethods
    ){
        ProgramOutputDTO programOutputDto;
        code = code.replaceAll("\"","'");
        String language = AceMode.python.toString();
        try {
            if (inputArea.getValue().isEmpty()) {
                //if code does not require input during execution
                programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDAO(language, code));
            } else {
                //if code requires input
                programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDAO(language, code, inputArea.getValue()));
            }

            String output = programOutputDto.getProgramOutput() + "\n"
                    + "Exit Code: " + programOutputDto.getExitCode() + "\n"
                    + "Execution Time: "+nanoSecondsToSeconds(
                    programOutputDto.getExecutionTime(), 4);

            //edit environmental details based on received error codes
            displayProgramExecutionStatus(programOutputDto.getExitCode(), output, outputArea);

        } catch (Exception e) {
            logger.error(e.getMessage());
            new CustomNotification("Error: Connection to the server could not be established", NotificationVariant.LUMO_ERROR).open();
        }
    }

    private void runCCode(
            String code,
            TextArea inputArea,
            AceEditor outputArea,
            ServerRequestMethods serverRequestMethods
    ){
        ProgramOutputDTO programOutputDto;
        code = code.replace("\"","\\\"")
                .replace("'", "'\\''");
        String language = "c";
        String output;
        try {
            if (inputArea.getValue().isEmpty()) {
                //if code does not require input during execution
                programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDAO(language, code));
                output = programOutputDto.getProgramOutput() + "\n"
                        + "Exit Code: " + programOutputDto.getExitCode() + "\n"
                        + "Execution Time: "+nanoSecondsToSeconds(
                        programOutputDto.getExecutionTime(), 4);
            } else {
                //if code requires input
                programOutputDto = serverRequestMethods.sendCodeRunRequest(new ProgramInputDAO(language, code, inputArea.getValue()));
                output = programOutputDto.getProgramOutput() + "\n"
                        + "Exit Code: " + programOutputDto.getExitCode() + "\n"
                        + "Execution Time: "+nanoSecondsToSeconds(
                        programOutputDto.getExecutionTime(), 4);
            }


            //edit environmental details based on received error codes
            displayProgramExecutionStatus(programOutputDto.getExitCode(), output, outputArea);

        } catch (Exception e) {
            logger.error(e.getMessage());
            new CustomNotification("Error: Connection to the server could not be established", NotificationVariant.LUMO_ERROR).open();
        }
    }

    private void displayProgramExecutionStatus(int exitCode,String output,AceEditor outputArea){
        if (exitCode == 0) {
//            outputArea.getStyle().remove("color");
            outputArea.setValue(output);
            new CustomNotification("Program has been executed successfully", NotificationVariant.LUMO_SUCCESS).open();
        } else if (exitCode >= 0) {
            outputArea.setValue(output);
//            outputArea.getStyle().setColor("red");
            new CustomNotification("Program has produced an error", NotificationVariant.LUMO_ERROR).open();
        } else if (exitCode == HttpStatusCode.EXPECTATION_FAILED.getCode()) {
            outputArea.setValue(output);
//            outputArea.getStyle().setColor("yellow");
            new CustomNotification("Program took too long to execute", NotificationVariant.LUMO_WARNING).open();
        }
    }


    //save content to existing file
    private void save(String currentFileName,String currentFilePath){
        System.out.println(currentFilePath);
        if ( !currentFileName.isEmpty() && !currentFileName.equals("Untitled Document") && !currentFilePath.isEmpty()){
            try {
                serverRequestMethods.saveFile(currentFilePath,aceEditor.getValue());
                launchSaveSuccessMessage();
            }catch (Exception e){
                launchSaveErrorNotification();
            }
        }else{
            new CustomNotification(
                    "you are attempting to save content to a file that doesn't exist",
                    NotificationVariant.LUMO_WARNING).open();
        }
    }

    //save name can't be untitled document!!!
    private void launchSaveDialogue(){
        ConfirmDialog dialog = new ConfirmDialog();
        TextField fileNameField  = new TextField();

        fileNameField.setValue(currentFileName.getText());

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.addClickListener(buttonClickEvent -> saveAs(fileNameField));
        cancelButton.addClickListener(buttonClickEvent -> dialog.close());

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

    private void saveAs(TextField fileNameField){
        try {
            ServerRequestMethods serverRequestMethods = new ServerRequestMethods();

            String basePath = "C:\\Users\\tobec\\ServerData\\user-files\\";
            String completePath;

            if (aceEditor.getMode() == AceMode.c_cpp){
                completePath = basePath+fileNameField.getValue()+".c";
            }else {
                completePath = basePath+fileNameField.getValue()+
                        ExtensionMapper.getExtensionByAceMode(aceEditor.getMode());
            }

            serverRequestMethods.saveFileAs(
                    currentFilePath,
                    completePath,
                    aceEditor.getValue());

            currentFilePath = basePath+fileNameField.getValue();
            currentFileName.setText(fileNameField.getValue()+
                    ExtensionMapper.getExtensionByAceMode(aceEditor.getMode()));

            launchSaveSuccessMessage();
        }catch (Exception e){
            launchSaveErrorNotification();
            logger.error(e.getMessage());
        }
    }

    private void launchSaveErrorNotification(){
        new CustomNotification(
                "An Error Occurred While Trying To Save Your File",
                NotificationVariant.LUMO_ERROR,
                3000,
                Notification.Position.TOP_CENTER)
                .open();
    }

    private void launchSaveSuccessMessage(){
        new CustomNotification(
                "File Saved Successfully",
                NotificationVariant.LUMO_SUCCESS,
                3000,
                Notification.Position.TOP_CENTER)
                .open();
    }


    //dialog for recent files
    private void launchRecentFilesDialog(
            Map<String,String> fileList,
            AceEditor aceEditor){
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
                    logger.error(e.getMessage());
                }
            }
        });

        dialog.getFooter().add(openFileButton);
        dialog.open();
    }

    private void setAceModeByFileExtension(String extension,Select<AceMode> selector){
        if (extension.equals(".c")){
            selector.setValue(AceMode.c_cpp);
        }else {
            selector.setValue(ExtensionMapper.getAceModeByExtension(extension));
        }
    }

    private void launchSettingsDialog(VerticalLayout layout){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Settings");
        dialog.add(layout);
        dialog.open();
    }

    private void launchFileUploadDialogue(
            H4 fileHeader,
            AceEditor aceEditor,
            Select<AceMode> aceModeSelector,
            String currentFilePath
    ){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Upload");

        FileBuffer fileBuffer = new FileBuffer();
        Upload upload = new Upload(fileBuffer);

        upload.addSucceededListener(succeededEvent -> {
            String fileName = succeededEvent.getFileName();
            fileHeader.setText(fileName);

            StringBuilder fileContent = new StringBuilder();
            try (
                    InputStream inputStream = fileBuffer.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(inputStreamReader)
            ){
                String line;
                while ((line = reader.readLine()) != null){
                    fileContent.append(line).append("\n");
                }

                setAceModeByFileExtension(
                        fileName.substring(fileName.lastIndexOf('.')),
                        aceModeSelector
                );

                aceEditor.setValue(fileContent.toString());
                dialog.close();
                new CustomNotification("file uploaded successfully",NotificationVariant.LUMO_SUCCESS).open();
            }catch (IOException e){
                new CustomNotification("an error occurred while uploading file",NotificationVariant.LUMO_ERROR).open();
                logger.error(e.getMessage());
            }
        });
        currentFilePath = "";
        dialog.add(upload);
        dialog.open();
    }

    private void launchAceModeChangeDialog(AceMode aceMode,AceEditor aceEditor){
        ConfirmDialog dialog = new ConfirmDialog();

        Button confirmButton = new Button("Confirm",onClick -> {
            try {
                aceEditor.setMode(aceMode);
                aceEditor.setValue(
                        ExtensionMapper.getDefaultTextByAceMode(aceMode)
                );
            }catch (Exception e){
                //log err
            }finally {
                dialog.close();
                new CustomNotification("language mode changed",NotificationVariant.LUMO_SUCCESS).open();
            }
        });

        Button cancelButton = new Button("Cancel",onClick-> {
            aceModeSelector.setValue(aceEditor.getMode());
            dialog.close();
        }
        );

        cancelButton.getStyle().setColor("red");

        dialog.setHeader("Are You Sure?");
        dialog.setText("Doing this will clear your current work");
        dialog.setCloseOnEsc(false);

        dialog.setCancelable(true);

        dialog.setConfirmButton(confirmButton);
        dialog.setCancelButton(cancelButton);


        dialog.open();
    }

    private String nanoSecondsToSeconds(long nanoSeconds, int decimalPlaces){
        double seconds = (double) nanoSeconds / 1_000_000_000.0;
        String formatString = "%." + decimalPlaces + "f";
        return String.format(formatString, seconds);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (VaadinSession.getCurrent().getAttribute("USER_TOKEN") == null){
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }

}
