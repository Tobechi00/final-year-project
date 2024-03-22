package com.webide.wide.views.views.loginregistration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.webide.wide.views.views.main.EditorView;

@Route("/success")
@PageTitle("success")
public class RegistrationSuccessView extends VerticalLayout {
    H1 successMessage;
    Button successButton;

    public RegistrationSuccessView(){
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        successMessage = new H1("Your Account Has Been Created Successfully");
        successButton = new Button("Log in",buttonClickEvent ->
            UI.getCurrent().navigate(LoginView.class)
        );

        successMessage.getStyle().setColor("green");
        successButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);

        add(successMessage,successButton);
    }

}
