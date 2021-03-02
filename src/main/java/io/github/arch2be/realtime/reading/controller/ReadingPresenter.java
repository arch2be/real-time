package io.github.arch2be.realtime.reading.controller;

import io.github.arch2be.realtime.reading.dto.ErrorInfoDto;
import io.github.arch2be.realtime.reading.dto.ReadingDto;
import io.github.arch2be.realtime.reading.service.ReadingService;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
class ReadingPresenter {
    private final ReadingService readingService;
    private final ReadingValidation readingValidation;

    ReadingPresenter(ReadingService readingService, ReadingValidation readingValidation) {
        this.readingService = readingService;
        this.readingValidation = readingValidation;
    }

    Either<ErrorInfoDto, ReadingDto> getLatestValueByDate() {
        return readingService.getLatestValueByDate();
    }

    Either<List<ErrorInfoDto>, ReadingAverageResponse> getAverageValueForCondition(ReadingRequest readingRequest) {
        List<ErrorInfoDto> errorInfoDtoList = readingValidation.validateAverageRequest(readingRequest);

        if (!errorInfoDtoList.isEmpty()) {
            return Either.left(errorInfoDtoList);
        }

        return readingService.getAverageValueForCondition(readingRequest.getDateFrom(), readingRequest.getDateTo(), readingRequest.isIncludeBadQuality())
                .fold(Either::left, right -> Either.right(new ReadingAverageResponse(right)));
    }

    Either<List<ErrorInfoDto>, ReadingSeriesValueResponse> getGoodQualityValuesBetweenDates(@Valid ReadingRequest readingRequest) {
        List<ErrorInfoDto> errorInfoDtoList = readingValidation.validateAverageRequest(readingRequest);

        if (!errorInfoDtoList.isEmpty()) {
            return Either.left(errorInfoDtoList);
        }

        return readingService.getGoodQualityValuesBetweenDates(readingRequest.getDateFrom(), readingRequest.getDateTo())
                .fold(Either::left, right -> Either.right(
                        new ReadingSeriesValueResponse(right, readingRequest.getDateFrom(), readingRequest.getDateTo())));
    }
}
