package com.example.demo.exception;

public class AppException extends RuntimeException{
    private ErrolCode errolCode;

    public AppException(ErrolCode errolCode) {
        super(errolCode.getMessage());
        this.errolCode = errolCode;
    }

    public ErrolCode getErrolCode() {
        return errolCode;
    }

    public void setErrolCode(ErrolCode errolCode) {
        this.errolCode = errolCode;
    }
}
