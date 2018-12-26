package se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends CustomException{
    private static final String UNAUTHORIZED_MESSAGE = "Incorrect username or password. Please try again.";

    public UnauthorizedException() {
        super(UNAUTHORIZED_MESSAGE);
        this.status = HttpStatus.UNAUTHORIZED;
    }
}