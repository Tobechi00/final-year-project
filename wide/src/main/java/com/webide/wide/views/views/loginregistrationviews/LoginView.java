package com.webide.wide.views.views.loginregistrationviews;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("login")
@Route("/login")
public class LoginView extends VerticalLayout {

    VerticalLayout formLayout;
    H2 title;
    TextField usernameField, passwordField;
    Button loginButton;

    public LoginView(){
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        formLayout = new VerticalLayout();
        title = new H2("log in");
        usernameField = new TextField();
        passwordField = new TextField();
        loginButton = new Button("log in");

        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        usernameField.setPlaceholder("username");
        passwordField.setPlaceholder("password");

        formLayout.add(title,usernameField,passwordField,loginButton);
    }

}
