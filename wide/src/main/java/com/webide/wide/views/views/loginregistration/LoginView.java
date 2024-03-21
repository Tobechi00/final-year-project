package com.webide.wide.views.views.loginregistration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.webide.wide.dao.LoginDao;
import com.webide.wide.server.ServerRequestMethods;
import com.webide.wide.views.views.main.EditorView;


@PageTitle("login")
@Route("/login")
public class LoginView extends VerticalLayout {

    LoginForm loginForm;
    RouterLink registerLink;
    ServerRequestMethods serverRequestMethods;

    public LoginView(){
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        serverRequestMethods = new ServerRequestMethods();
        registerLink = new RouterLink("Register",RegistrationView.class);

        loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false);

        loginForm.addLoginListener(loginEvent -> {
            String username = loginEvent.getUsername();
            String password = loginEvent.getPassword();

            if (!username.isEmpty() && !password.isEmpty()){
                try {
                    serverRequestMethods.sendLoginRequest(
                            new LoginDao(username,password)
                    );

                    UI.getCurrent().navigate(EditorView.class);
                }catch (Exception e){
                    //log err
                    loginForm.setError(true);
                }
            }
        });

        add(loginForm,registerLink);
    }

}
