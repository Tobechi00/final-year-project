package com.webide.wide.views.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import de.f0rce.ace.AceEditor;

import java.util.stream.Collectors;

@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
public class MainLayout extends AppLayout implements AppShellConfigurator {

    SideNavItem saveFile,saveProjects,createProject,savedProjects,loadProjects;
    DrawerToggle drawerToggle;

    H2 applicationHeader;

    HorizontalLayout fileNameLayout,brandTitleLayout,parentLayout;
    Paragraph fileName;



    public MainLayout(){
        saveFile = new SideNavItem("download file");
        createProject = new SideNavItem("New project");
        savedProjects = new SideNavItem("Saved projects");
        loadProjects = new SideNavItem("Load projects");
        fileName = new Paragraph("untitled document");

        drawerToggle = new DrawerToggle();



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
