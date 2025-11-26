package com.rihem.users_microservice.util;

public interface EmailSender {
    void sendEmail(String toEmail, String body);
}
