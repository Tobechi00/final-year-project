package com.webide.wide.views.customcomponents;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.webide.wide.views.views.loginregistration.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvatarCard extends Dialog{

    Avatar avatar;
    Paragraph fullname,username;
    Button logoutButton;
    VaadinSession session;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    VerticalLayout mainCardLayout;
    public AvatarCard(){
        setCloseOnOutsideClick(true);
        String firstname = "";
        String lastname = "";

        try{
            session = VaadinSession.getCurrent();
            firstname = session.getAttribute("FIRSTNAME").toString();
            lastname = session.getAttribute("LASTNAME").toString();
            fullname = new Paragraph(firstname+" "+lastname);
            username = new Paragraph(session.getAttribute("USERNAME").toString());
        }catch (Exception e){
            logger.warn("error occurred while retrieving userdata: "+e.getMessage());
        }

        avatar = new Avatar(firstname+" "+lastname);
        logoutButton = new Button("Logout");
        mainCardLayout = new VerticalLayout();

        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
        logoutButton.addClickListener(clickEvent ->{
            //close current and open confirm
            launchLogoutDialog();
            close();
        });

        mainCardLayout.setSizeFull();

        mainCardLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainCardLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        mainCardLayout.add(avatar,fullname,username,logoutButton);

        add(mainCardLayout);
    }

    public void launchLogoutDialog(){
        ConfirmDialog logoutDialog = new ConfirmDialog();
        Button cancelButton = new Button("Cancel");
        Button confirmButton = new Button("Logout");

        logoutDialog.setText("Are You Sure You Want To Logout?");
        logoutDialog.setHeader("Confirm Logout");

        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        cancelButton.addClickListener(clickEvent -> logoutDialog.close());
        confirmButton.addClickListener(clickEvent -> logout());

        logoutDialog.setCancelable(true);
        logoutDialog.setCancelButton(cancelButton);
        logoutDialog.setConfirmButton(confirmButton);

        logoutDialog.open();
    }

    public void logout(){
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().navigate(LoginView.class);
    }
}
