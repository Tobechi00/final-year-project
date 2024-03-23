package com.webide.wide.views.views.main;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(themeClass = Lumo.class, variant = Lumo.LIGHT)
public class MainLayout extends AppLayout implements AppShellConfigurator {

    Avatar avatar;
    Paragraph spacerOne;
    Paragraph spacerTwo;
    H3 header;


    HorizontalLayout brandTitleLayout, avatarLayout,parentLayout;



    public MainLayout(){
        avatarLayout = new HorizontalLayout();
        header = new H3("Web_Weaver");

        header.getStyle().setPadding("5").setColor("gray");

        String firstName = (String) VaadinSession.getCurrent().getAttribute("FIRSTNAME");
        String lastName = (String) VaadinSession.getCurrent().getAttribute("LASTNAME");

        avatar = new Avatar(firstName+" "+lastName);

        avatar.getStyle().setPadding("5");

        spacerOne = new Paragraph("  ");
        spacerTwo = new Paragraph("  ");

        avatarLayout.add(avatar,spacerTwo);
        avatarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        avatarLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        avatarLayout.setSizeFull();



        //contains the drawer toggle and the application name
        brandTitleLayout = new HorizontalLayout();
        brandTitleLayout.add(spacerOne,header);
        brandTitleLayout.setSizeFull();
        brandTitleLayout.setAlignItems(FlexComponent.Alignment.START);

        //parent layout which contains file and brand layout
        parentLayout = new HorizontalLayout();
        parentLayout.add(brandTitleLayout, avatarLayout);
        parentLayout.setSizeFull();

        addToNavbar(parentLayout);
    }

}
