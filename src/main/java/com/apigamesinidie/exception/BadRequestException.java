package com.apigamesinidie.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("Entity is necessary, Request on this endpoint without entity is not allowed");
    }
}
