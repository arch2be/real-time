package io.github.arch2be.realtime.reading.controller;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

class ReadingRequest {
    @NotNull(message = "Field dateFrom cannot be empty")
    private LocalDateTime dateFrom;
    @NotNull(message = "Field dateTo cannot be empty")
    private LocalDateTime dateTo;
    private boolean includeBadQuality;

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public boolean isIncludeBadQuality() {
        return includeBadQuality;
    }
}
