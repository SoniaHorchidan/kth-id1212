package se.kth.id1212.project.sonia.restful_news_feed.service;

public class ServiceError extends Exception {

    public ServiceError() {}

    public ServiceError(String reason) {
        super(reason);
    }

    public ServiceError(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
}
