package io.github.arch2be.realtime.measure.controller;

import io.github.arch2be.realtime.measure.dto.ErrorInfoDto;
import io.github.arch2be.realtime.measure.dto.ReadingDto;
import io.github.arch2be.realtime.measure.service.ReadingService;
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

    public Either<ErrorInfoDto, ReadingDto> getLatestValueByDate() {
        return readingService.getLatestValueByDate();
    }

    public Either<List<ErrorInfoDto>, ReadingAverageResponse> getAverageValueForCondition(ReadingRequest readingRequest) {
        List<ErrorInfoDto> errorInfoDtoList = readingValidation.validateAverageRequest(readingRequest);

        return errorInfoDtoList.isEmpty()
            ? readingService.getAverageValueForCondition(readingRequest)
            : Either.left(errorInfoDtoList);
    }

    public Either<List<ErrorInfoDto>, ReadingSeriesValueResponse> getGoodQualityValuesBetweenDates(@Valid ReadingRequest readingRequest) {
        List<ErrorInfoDto> errorInfoDtoList = readingValidation.validateAverageRequest(readingRequest);

        return errorInfoDtoList.isEmpty()
                ? readingService.getGoodQualityValuesBetweenDates(readingRequest)
                : Either.left(errorInfoDtoList);
    }
}
