package com.webide.wide.views.custom_components;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class CustomNotification extends Notification {

    public CustomNotification(String message, NotificationVariant warningLevel){
        setText(message);
        addThemeVariants(warningLevel);
        setPosition(Position.TOP_CENTER);
        setDuration(3000);
    }

    public CustomNotification(String message, NotificationVariant warningLevel,int duration,Position position){
        setText(message);
        addThemeVariants(warningLevel);
        setPosition(position);

        setDuration(duration);
    }
}
