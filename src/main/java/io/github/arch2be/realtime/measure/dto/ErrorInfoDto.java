package io.github.arch2be.realtime.measure.dto;

public class ErrorInfoDto {
    private String message;

    public ErrorInfoDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
