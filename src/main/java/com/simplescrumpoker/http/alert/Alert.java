package com.simplescrumpoker.http.alert;

import lombok.Value;

import java.util.Objects;

@Value(staticConstructor = "of")
public class Alert {
    AlertType type;
    String message;

    AlertMessageType messageType;

    public static Alert ofInfo(String message) {
        return of(AlertType.INFO, message, AlertMessageType.PROPERTY);
    }

    public static Alert ofError(String message) {
        return of(AlertType.ERROR, message, AlertMessageType.PROPERTY);
    }

    public static Alert ofWarn(String message) {
        return of(AlertType.WARN, message, AlertMessageType.PROPERTY);
    }

    public static Alert ofSuccess(String message) {
        return of(AlertType.SUCCESS, message, AlertMessageType.PROPERTY);
    }


    public static Alert ofInfo(String message, AlertMessageType messageType) {
        return of(AlertType.INFO, message, messageType);
    }

    public static Alert ofError(String message, AlertMessageType messageType) {
        return of(AlertType.ERROR, message, messageType);
    }

    public static Alert ofWarn(String message, AlertMessageType messageType) {
        return of(AlertType.WARN, message, messageType);
    }

    public static Alert ofSuccess(String message, AlertMessageType messageType) {
        return of(AlertType.SUCCESS, message, messageType);
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
