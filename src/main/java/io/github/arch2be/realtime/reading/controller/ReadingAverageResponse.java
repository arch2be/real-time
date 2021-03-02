package io.github.arch2be.realtime.reading.controller;

class ReadingAverageResponse {
    private final Float averageValue;

    public ReadingAverageResponse(Float averageValue) {
        this.averageValue = averageValue;
    }

    public Float getAverageValue() {
        return averageValue;
    }
}
