package com.webide.wide.views.views.loginregistration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.webide.wide.dataobjects.dao.LoginDAO;
import com.webide.wide.server.ServerRequestMethods;
import com.webide.wide.views.views.main.EditorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@PageTitle("login")
@Route("/login")
public class LoginView extends VerticalLayout {

    LoginForm loginForm;
    RouterLink registerLink;
    ServerRequestMethods serverRequestMethods;
    Logger logger;

    public LoginView(){
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        serverRequestMethods = new ServerRequestMethods();
        logger = LoggerFactory.getLogger(LoginView.class);
        registerLink = new RouterLink("Create an Account",RegistrationView.class);

        loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false);

        loginForm.addLoginListener(loginEvent -> {
            String username = loginEvent.getUsername();
            String password = loginEvent.getPassword();

            if (!username.isEmpty() && !password.isEmpty()){
                try {
                    serverRequestMethods.sendLoginRequest(
                            new LoginDAO(username,password)
                    );

                    UI.getCurrent().navigate(EditorView.class);
                }catch (Exception e){
                    logger.error(e.getMessage());
                    loginForm.setError(true);
                }
            }
        });

        add(loginForm,registerLink);
    }

}
