package io.github.arch2be.realtime.measure.controller;

public class ReadingAverageResponse {

    private Float averageValue;

    public ReadingAverageResponse(Float averageValue) {
        this.averageValue = averageValue;
    }

    public Float getAverageValue() {
        return averageValue;
    }
}
