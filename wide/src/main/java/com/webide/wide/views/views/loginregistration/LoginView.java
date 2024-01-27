package com.webide.wide.views.views.loginregistration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.webide.wide.dao.LoginDao;
import com.webide.wide.server.ServerRequestMethods;
import com.webide.wide.views.views.main.EditorView;


@PageTitle("login")
@Route("/login")
public class LoginView extends VerticalLayout {

    VerticalLayout formLayout;
    H2 title;
    Paragraph paragraph;
    EmailField usernameField;
    PasswordField passwordField;
    Button loginButton;
    ServerRequestMethods serverRequestMethods;

    public LoginView(){
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        serverRequestMethods = new ServerRequestMethods();

        formLayout = new VerticalLayout();
        paragraph = new Paragraph();
        paragraph.getStyle().setColor("red");

        title = new H2("log in");
        usernameField = new EmailField();
        passwordField = new PasswordField();
        loginButton = new Button("log in");

        usernameField.setPlaceholder("username");
        passwordField.setPlaceholder("password");

        //todo:change catch block error to more specific exception
        loginButton.addClickListener(login->{
            if (!usernameField.isEmpty() || !passwordField.isEmpty()){
                try {
                    serverRequestMethods.sendLoginRequestAndReceivePayload(

                            new LoginDao(usernameField.getValue(),passwordField.getValue()));
                    UI.getCurrent().navigate(EditorView.class);

                } catch (Exception e) {
                    paragraph.setText("please check your details");
                }
            }
        });

        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        formLayout.setPadding(true);

        formLayout.add(title,paragraph,usernameField,passwordField,loginButton);
        add(formLayout);
    }

}
