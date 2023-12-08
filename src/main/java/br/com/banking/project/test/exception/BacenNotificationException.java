package br.com.banking.project.test.exception;

import org.springframework.web.client.HttpStatusCodeException;

public class BacenNotificationException extends Throwable {
    public BacenNotificationException(String s, HttpStatusCodeException e) {
    }

    public BacenNotificationException(String s) {
    }
}
