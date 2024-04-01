package com.webide.wide;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.webide.wide.views.views.loginregistration.LoginView;
import com.webide.wide.views.views.main.EditorView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
public class WideApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		
	}

}
