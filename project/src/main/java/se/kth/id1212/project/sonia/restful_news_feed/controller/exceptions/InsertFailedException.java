package se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class InsertFailedException extends CustomException{
    private static final String INSERT_FAILED_MESSAGE = "We are sorry, but the news item was not inserted and was deleted. Please try again.";

    public InsertFailedException() {
        super(INSERT_FAILED_MESSAGE);
        this.status = HttpStatus.SERVICE_UNAVAILABLE;
    }

}
