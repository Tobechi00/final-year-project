package com.webide.wide.views.views.loginregistration;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.awt.*;

@Route(value = "/register")
@PageTitle("register")
public class RegistrationView extends VerticalLayout {

    H2 title;

    EmailField emailField;
    TextField firstnameField,lastnameField;
    PasswordField passwordField,confirmPasswordField;
    FormLayout registrationFormLayout;
    HorizontalLayout formWrapper;
    Button registerButton;

    public RegistrationView(){
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        setPadding(true);

        title = new H2("Register");
        firstnameField = new TextField("First name");
        lastnameField = new TextField("Last name");
        emailField = new EmailField("Email");
        passwordField = new PasswordField("Password");
        confirmPasswordField = new PasswordField("Confirm password");


        registerButton = new Button("Register");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //set listener


        registrationFormLayout = new FormLayout();
        registrationFormLayout.setMaxWidth("800px");
        registrationFormLayout.setResponsiveSteps(
                //use one column by default
                new FormLayout.ResponsiveStep("0",1),
                //use two columns when width exceeds 500px
                new FormLayout.ResponsiveStep("500px",2)
        );
        registrationFormLayout.setColspan(emailField,2);
        registrationFormLayout.add(firstnameField,lastnameField
                ,emailField,passwordField,
                confirmPasswordField);

        formWrapper = new HorizontalLayout(registrationFormLayout);
        HorizontalLayout layout = new HorizontalLayout(title);
        layout.setJustifyContentMode(JustifyContentMode.START);
        layout.setAlignItems(Alignment.START);

        add(layout,formWrapper,registerButton);
    }



}
