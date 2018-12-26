package se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RegistrationFailedException extends CustomException{
    private static final String REGISTRATION_FAILED_MESSAGE = "Username already exists. Please enter a valid username.";

    public RegistrationFailedException() {
        super(REGISTRATION_FAILED_MESSAGE);
        this.status = HttpStatus.CONFLICT;
    }
}
