package com.vitaly.crudjdbcapp.exceptions;

public class NoWritersInDbException extends Exception {
    public NoWritersInDbException(String message) {
        super(message);
    }
}
