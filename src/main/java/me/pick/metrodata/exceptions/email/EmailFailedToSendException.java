package me.pick.metrodata.exceptions.email;

public class EmailFailedToSendException extends RuntimeException {
    public EmailFailedToSendException(String email) {
        super("Failed to send email to " + email);
    }
}
