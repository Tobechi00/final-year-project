package com.webide.wide.frontend.custom_components;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class OutputDialog extends Dialog {


    TextArea outputArea;

    TextField exitCodeField;

    VerticalLayout verticalLayout;
    
    Button closeButton;

    public OutputDialog(){
        setHeaderTitle("Output");

        setDraggable(true);
        setCloseOnEsc(true);
        setModal(false);
        setResizable(true);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        setMaxHeight(800, Unit.PIXELS);
        setMaxWidth(800,Unit.PIXELS);
        setHeight(200,Unit.PIXELS);
        setWidth(600,Unit.PIXELS);


        outputArea = new TextArea();
        exitCodeField = new TextField();
        closeButton = new Button();


        outputArea.setLabel("code output");
        outputArea.setReadOnly(true);
        outputArea.setSizeFull();

        exitCodeField.setLabel("exit code");
        exitCodeField.setReadOnly(true);
        exitCodeField.setSizeFull();

        closeButton.setText("close");
        closeButton.addClickListener(event -> {
            this.close();
        });

        verticalLayout = new VerticalLayout();
        verticalLayout.setAlignSelf(FlexComponent.Alignment.CENTER);
        verticalLayout.setSizeFull();

        verticalLayout.add(outputArea,exitCodeField);


        add(verticalLayout);

    }

    public void setOutputAreaValue(String value){
        this.outputArea.setValue(value);
    }

    public void setExitCodeField(String value){

        if (Integer.parseInt(value) > 0){
            this.outputArea.getStyle().setColor("red");
        }else {
            this.outputArea.getStyle().setColor("green");
        }

        this.exitCodeField.setValue(value);
    }
}
