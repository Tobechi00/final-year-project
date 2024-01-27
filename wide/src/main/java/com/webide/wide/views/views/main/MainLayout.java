package com.webide.wide.views.views.main;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
public class MainLayout extends AppLayout implements AppShellConfigurator {

    SideNavItem saveFile,saveProjects,createProject,savedProjects,loadProjects;
    Avatar avatar;
    Paragraph paragraph;
    DrawerToggle drawerToggle;

    H2 applicationHeader;

    HorizontalLayout fileNameLayout,brandTitleLayout, avatarLayout,parentLayout;



    public MainLayout(){
        //todo: add functionality
        saveFile = new SideNavItem("download file");
        createProject = new SideNavItem("New project");
        savedProjects = new SideNavItem("Saved projects");
        loadProjects = new SideNavItem("Load projects");
        avatarLayout = new HorizontalLayout();


        drawerToggle = new DrawerToggle();



        //file name layout ( to change when a file is selected)
//        fileNameLayout = new HorizontalLayout();
//        fileNameLayout.setAlignItems(FlexComponent.Alignment.CENTER);
//        fileNameLayout.setSizeFull();
//        fileNameLayout.add(fileName);

        //layout for logged-in username and avatar (might change to fullname)
        String firstName = (String) VaadinSession.getCurrent().getAttribute("FIRSTNAME");
        String lastName = (String) VaadinSession.getCurrent().getAttribute("LASTNAME");

        avatar = new Avatar(firstName+" "+lastName);
        avatar.getStyle().setColor("white");
        paragraph = new Paragraph("  ");

        avatarLayout.add(avatar,paragraph);
        avatarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        avatarLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        avatarLayout.setSizeFull();



        //contains the drawer toggle and the application name
        brandTitleLayout = new HorizontalLayout();
        brandTitleLayout.add(drawerToggle);
        brandTitleLayout.setSizeFull();
        brandTitleLayout.setAlignItems(FlexComponent.Alignment.START);

        //parent layout which contains file and brand layout
        parentLayout = new HorizontalLayout();
        parentLayout.add(brandTitleLayout, avatarLayout);
        parentLayout.setSizeFull();


        addToDrawer(saveProjects,createProject,savedProjects,loadProjects);
        addToNavbar(parentLayout);
    }

}
