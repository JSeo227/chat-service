package com.example.chat.chat_service.global.exception;

import lombok.Getter;

@Getter
public class OthersException extends RuntimeException {

    private final int statusCode;

    public OthersException(int statusCode) {
        this.statusCode = statusCode;
    }

    public OthersException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public OthersException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}
