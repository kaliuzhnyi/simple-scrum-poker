package com.simplescrumpoker.http.alert;

import lombok.Value;

import java.util.Objects;

@Value(staticConstructor = "of")
public class Alert {
    AlertType type;
    String message;

    public static Alert ofInfo(String message) {
        return of(AlertType.INFO, message);
    }

    public static Alert ofError(String message) {
        return of(AlertType.ERROR, message);
    }

    public static Alert ofWarn(String message) {
        return of(AlertType.WARN, message);
    }

    public static Alert ofSuccess(String message) {
        return of(AlertType.SUCCESS, message);
    }

    public boolean isInfo() {
        return Objects.equals(type, AlertType.INFO);
    }

    public boolean isError() {
        return Objects.equals(type, AlertType.ERROR);
    }

    public boolean isWarn() {
        return Objects.equals(type, AlertType.WARN);
    }

    public boolean isSuccess() {
        return Objects.equals(type, AlertType.SUCCESS);
    }

}
