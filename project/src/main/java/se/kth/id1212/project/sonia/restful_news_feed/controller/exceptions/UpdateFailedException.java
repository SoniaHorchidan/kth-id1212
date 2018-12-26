package se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class UpdateFailedException extends CustomException{
    private static final String UPDATE_FAILED_MESSAGE = "We are sorry, but the news entry was not updated. Please try again.";

    public UpdateFailedException() {
        super(UPDATE_FAILED_MESSAGE);
        this.status = HttpStatus.SERVICE_UNAVAILABLE;
    }

}
