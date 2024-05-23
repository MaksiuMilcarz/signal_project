package com.alerts.Factory;

import com.alerts.Alert;

public abstract class AlertFactory {
    public abstract Alert createAlert(String condition);
}

