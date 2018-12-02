package server.integration;

public class FileCatalogDBException extends Exception {

    public FileCatalogDBException(String reason) {
        super(reason);
    }

    public FileCatalogDBException(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
}
