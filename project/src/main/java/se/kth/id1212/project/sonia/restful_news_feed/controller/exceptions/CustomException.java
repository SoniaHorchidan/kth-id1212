package se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    protected HttpStatus status;

    public CustomException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
