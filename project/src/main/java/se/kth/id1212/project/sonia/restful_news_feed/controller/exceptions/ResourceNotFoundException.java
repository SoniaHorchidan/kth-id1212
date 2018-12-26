package se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends CustomException {
    private static final String RESOURCE_NOT_FOUND_MESSAGE = "The resource which you requested does not exist.";

    public ResourceNotFoundException() {
        super(RESOURCE_NOT_FOUND_MESSAGE);
        this.status = HttpStatus.NOT_FOUND;
    }

}
