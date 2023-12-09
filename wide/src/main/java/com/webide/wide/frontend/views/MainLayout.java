package com.webide.wide.frontend.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.io.File;

@Theme(themeClass = Lumo.class, variant = Lumo.LIGHT)
public class MainLayout extends AppLayout implements AppShellConfigurator {

    SideNavItem saveProjects,createProject,savedProjects,loadProjects,settings;
    DrawerToggle drawerToggle;

    H2 applicationHeader;

    HorizontalLayout fileNameLayout,brandTitleLayout,parentLayout;
    Paragraph fileName;



    public MainLayout(){
        savedProjects = new SideNavItem("save file");
        createProject = new SideNavItem("new project");
        savedProjects = new SideNavItem("saved projects");
        loadProjects = new SideNavItem("load projects");
        settings = new SideNavItem("settings");
        fileName = new Paragraph("untitled document");

        drawerToggle = new DrawerToggle();

//        applicationHeader = new H2("W -ide");



        //file name layout ( to change when a file is selected)
        fileNameLayout = new HorizontalLayout();
        fileNameLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        fileNameLayout.setSizeFull();
        fileNameLayout.add(fileName);


        //contains the drawer toggle and the application name
        brandTitleLayout = new HorizontalLayout();
        brandTitleLayout.add(drawerToggle);
        brandTitleLayout.setSizeFull();
        brandTitleLayout.setAlignItems(FlexComponent.Alignment.START);

        //parent layout which contains file and brand layout
        parentLayout = new HorizontalLayout();
        parentLayout.add(brandTitleLayout,fileNameLayout);
        parentLayout.setSizeFull();


        addToDrawer(saveProjects,createProject,savedProjects,loadProjects);
        addToNavbar(parentLayout);
    }
}
