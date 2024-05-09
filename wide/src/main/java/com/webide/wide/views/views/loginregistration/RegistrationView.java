package com.webide.wide.views.views.loginregistration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.webide.wide.dataobjects.dto.RegistrationDTO;
import com.webide.wide.server.ServerRequestMethods;
import com.webide.wide.views.customcomponents.CustomNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;

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
    Logger logger;

    public RegistrationView(){
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        setPadding(true);

        title = new H2("Create an Account");
        firstnameField = new TextField("First name");
        lastnameField = new TextField("Last name");
        emailField = new EmailField("Email");
        passwordField = new PasswordField("Password");
        confirmPasswordField = new PasswordField("Confirm password");
        logger = LoggerFactory.getLogger(RegistrationView.class);


        firstnameField.setRequired(true);
        lastnameField.setRequired(true);
        emailField.setRequired(true);
        passwordField.setRequired(true);
        confirmPasswordField.setRequired(true);

        firstnameField.setRequiredIndicatorVisible(true);
        lastnameField.setRequiredIndicatorVisible(true);
        emailField.setRequiredIndicatorVisible(true);
        passwordField.setRequiredIndicatorVisible(true);
        confirmPasswordField.setRequiredIndicatorVisible(true);


        registerButton = new Button("Register");
        registerButton.addClickListener(onRegistrationAttempt -> register(
                firstnameField,
                lastnameField,
                emailField,
                passwordField,
                confirmPasswordField
        ));
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

        //set email and title to take up two columns
        registrationFormLayout.setColspan(emailField,2);
        registrationFormLayout.setColspan(title,2);

        registrationFormLayout.add(title,firstnameField,lastnameField
                ,emailField,passwordField,
                confirmPasswordField);


        formWrapper = new HorizontalLayout(registrationFormLayout);

        add(formWrapper,registerButton);
    }

    public void register(
            TextField firstnameField,
            TextField lastnameField,
            EmailField emailField,
            PasswordField passwordField,
            PasswordField confirmPasswordField
    ){
        if (!firstnameField.isEmpty()
                        &&!lastnameField.isEmpty()
                        &&!emailField.isEmpty()
                        &&!emailField.isInvalid()
                        &&!passwordField.isEmpty()
                        &&!confirmPasswordField.isEmpty()
                &&passwordField.getValue().equals(confirmPasswordField.getValue())
        ){
            try{
               ServerRequestMethods serverRequestMethods = new ServerRequestMethods();
               //call registration method
                RegistrationDTO registrationDTO = new RegistrationDTO(
                        firstnameField.getValue(),
                        lastnameField.getValue(),
                        emailField.getValue(),
                        passwordField.getValue()
                        );
                HttpStatusCode statusCode = serverRequestMethods.sendRegistrationRequest(registrationDTO);

                //create full check
                UI.getCurrent().navigate(RegistrationSuccessView.class);

            }catch (Exception e){
                new CustomNotification(
                        "An Error Occurred While Trying to Create Your Account",
                        NotificationVariant.LUMO_ERROR,
                        3000,
                        Notification.Position.TOP_CENTER)
                        .open();

                logger.error(e.getMessage());
            }
        }else{
            new CustomNotification(
                    "Please Ensure All Fields Are Filled Correctly",
                    NotificationVariant.LUMO_WARNING,
                    3000,
                    Notification.Position.TOP_CENTER)
                    .open();
        }
    }


}
