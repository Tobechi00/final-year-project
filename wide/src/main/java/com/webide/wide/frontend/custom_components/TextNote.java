package com.webide.wide.frontend.custom_components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;


//custom component for creating text notes.
public class TextNote extends Dialog {

    TextArea textArea;

    Button closeButton,saveButton;

    int fullStop = 0;

    int spaceCounter = 0;

    public TextNote(){
        setHeaderTitle("Note");
        setDraggable(true);
        setCloseOnEsc(true);
        setModal(false);
        setResizable(true);
        setCloseOnEsc(true);

        setHeight(400,Unit.PIXELS);
        setWidth(400,Unit.PIXELS);

        setMaxHeight(800, Unit.PIXELS);
        setMaxWidth(800,Unit.PIXELS);

        textArea = new TextArea();
        closeButton = new Button();
        saveButton = new Button();

        textArea.setPlaceholder("your notes");
        textArea.setSizeFull();
        textArea.setValueChangeMode(ValueChangeMode.EAGER);
        textArea.setMaxLength(300);



        textArea.addValueChangeListener(e -> {
                e.getSource().setHelperText(e.getValue().length() + "/" + 300);
                if (textArea.getValue().length() == 1){
                    textArea.setValue(textArea.getValue().toUpperCase());
                }
//                if (fullStop == 1){
//                    if (!textArea.isEmpty()) {
//                        if (!String.valueOf(textArea.getValue().charAt(textArea.getValue().length() - 1)).equals(" ")) {
//                            if (spaceCounter > 0) {
//                                StringBuilder s = new StringBuilder(textArea.getValue());
//                                s.setLength(textArea.getValue().length() - 2);
//                                for (int i = 0; i < spaceCounter; i++) {
//                                    s.append(" ");
//                                }
//                                s.append(String.valueOf(textArea.getValue().charAt(textArea.getValue().length() - 1)).toUpperCase());
//                                textArea.setValue(s.toString());
//                                spaceCounter = 0;
//                                setFullStop(0);
//                            } else {
//                                StringBuilder s = new StringBuilder(textArea.getValue());
//                                if (!s.isEmpty()) {
//                                    s.setLength(textArea.getValue().length() - 2);
//                                    s.append(String.valueOf(textArea.getValue().charAt(textArea.getValue().length() - 1)).toUpperCase());
//                                    textArea.setValue(s.toString());
//                                    setFullStop(0);
//                                }
//                            }
//                        } else {
//                            incrementSpaceCounter();
//                        }
//                    }
//                }
//                if (!textArea.isEmpty()) {
//                    if (textArea.getValue().charAt(textArea.getValue().length() - 1) == '.') {
//                        setFullStop(1);
//                    }
//                }
            });

        closeButton.setText("close");
        saveButton.setText("save");


        //close note on click
        closeButton.addClickListener(event -> {
           this.close();
        });


        getFooter().add(saveButton,closeButton);


        add(textArea);
    }

    private void setFullStop(int newValue){
        fullStop = newValue;
    }

    private void incrementSpaceCounter(){
        spaceCounter = spaceCounter+1;
    }
}
