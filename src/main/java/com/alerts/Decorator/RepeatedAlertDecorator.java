package com.alerts.Decorator;

import java.util.List;

import com.alerts.Alert;

public class RepeatedAlertDecorator extends AlertDecorator {
    private long interval;
    private int repeatCount;

    public RepeatedAlertDecorator(Alert decoratedAlert, long interval, int repeatCount) {
        super(decoratedAlert);
        this.interval = interval;
        this.repeatCount = repeatCount;
    }

    @Override
    public void alertAction(List<Alert> alerts) {
        for (int i = 0; i < repeatCount; i++) {
            super.alertAction(alerts);
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}