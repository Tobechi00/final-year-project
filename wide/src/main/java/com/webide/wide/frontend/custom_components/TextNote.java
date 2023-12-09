package com.webide.wide.frontend.custom_components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextArea;


//custom component for creating text notes.
public class TextNote extends Dialog {

    TextArea textArea;

    Button closeButton,saveButton;

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

        closeButton.setText("close");
        saveButton.setText("save");


        //close note on click
        closeButton.addClickListener(event -> {
           this.close();
        });


        getFooter().add(saveButton,closeButton);


        add(textArea);
    }
}
