package io.github.arch2be.realtime.reading.controller;

import java.time.LocalDateTime;
import java.util.List;

class ReadingSeriesValueResponse {
    private final LocalDateTime dateFrom;
    private final LocalDateTime dateTo;
    private final List<Float> values;

    public ReadingSeriesValueResponse(List<Float> allGoodQualityValuesBetweenDates, LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.values = allGoodQualityValuesBetweenDates;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public List<Float> getValues() {
        return values;
    }
}
