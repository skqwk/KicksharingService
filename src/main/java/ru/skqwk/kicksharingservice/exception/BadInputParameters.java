package ru.skqwk.kicksharingservice.exception;

public class BadInputParameters extends RuntimeException{
    public BadInputParameters(String message) {
        super(message);
    }
}
