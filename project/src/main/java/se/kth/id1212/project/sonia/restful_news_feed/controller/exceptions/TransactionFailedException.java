package se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class TransactionFailedException extends CustomException{
    private static final String TRANSACTION_FAILED_MESSAGE = "We are sorry, but the operation could not be performed.";

    public TransactionFailedException() {
        super(TRANSACTION_FAILED_MESSAGE);
        this.status = HttpStatus.SERVICE_UNAVAILABLE;
    }

}