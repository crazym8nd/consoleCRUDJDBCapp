package com.vitaly.crudjdbcapp.exceptions;

public class NoPostsInDbException extends Exception {
    public NoPostsInDbException(String message) {
        super(message);
    }
}
