package com.vitaly.crudjdbcapp.exceptions;

public class NoLabelsInDbException extends Exception {
    public NoLabelsInDbException(String message) {
        super(message);
    }
}
