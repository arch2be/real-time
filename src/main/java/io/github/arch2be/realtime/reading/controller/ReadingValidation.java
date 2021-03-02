package io.github.arch2be.realtime.reading.controller;

import io.github.arch2be.realtime.reading.dto.ErrorInfoDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class ReadingValidation {

    List<ErrorInfoDto> validateAverageRequest(ReadingRequest readingRequest) {
        List<ErrorInfoDto> errors = new ArrayList<>();

        if (readingRequest.getDateFrom().isAfter(readingRequest.getDateTo())) {
            errors.add(new ErrorInfoDto("Field dateFrom cannot be after dateTo"));
        }

        return errors;
    }
}
