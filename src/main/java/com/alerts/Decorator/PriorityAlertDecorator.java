package com.alerts.Decorator;

import java.util.List;

import com.alerts.Alert;

public class PriorityAlertDecorator extends AlertDecorator {
    private String priority;

    public PriorityAlertDecorator(Alert decoratedAlert, String priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    @Override
    public void alertAction(List<Alert> alerts) {
        System.out.println("PRIORITY: " + priority);
        super.alertAction(alerts);
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
