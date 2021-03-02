package io.github.arch2be.realtime.measure.service;

import io.github.arch2be.realtime.measure.controller.ReadingRequest;
import io.github.arch2be.realtime.measure.controller.ReadingAverageResponse;
import io.github.arch2be.realtime.measure.controller.ReadingSeriesValueResponse;
import io.github.arch2be.realtime.measure.dto.ErrorInfoDto;
import io.github.arch2be.realtime.measure.dto.ReadingDto;
import io.github.arch2be.realtime.measure.enums.Quality;
import io.github.arch2be.realtime.measure.repository.ReadingDao;
import io.vavr.control.Either;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReadingService {

    private final ReadingDao readingDao;

    ReadingService(ReadingDao readingDao) {
        this.readingDao = readingDao;
    }

    public Either<ErrorInfoDto, ReadingDto> getLatestValueByDate() {
        Optional<ReadingDto> readingDto = readingDao.findFirstByOrderByDateDesc()
                .map(reading -> new ReadingDto.Builder()
                        .withDate(reading.getDate())
                        .withQuality(reading.getQuality())
                        .withValue(reading.getValue())
                        .build());

        return readingDto
                .<Either<ErrorInfoDto, ReadingDto>>map(Either::right)
                .orElseGet(() -> Either.left(new ErrorInfoDto("No values specified")));
    }

    public Either<List<ErrorInfoDto>, ReadingAverageResponse> getAverageValueForCondition(ReadingRequest readingRequest) {
        Float averageValueBetweenDate = readingDao.getAverageValueBetweenDate(
                readingRequest.getDateFrom(),
                readingRequest.getDateTo(),
                getQualitiesForSearching(readingRequest.isIncludeBadQuality()));

        return Objects.nonNull(averageValueBetweenDate)
                ? Either.right(new ReadingAverageResponse(averageValueBetweenDate))
                : Either.left(Collections.singletonList(new ErrorInfoDto("No average value specified")));
    }

    private Set<Quality> getQualitiesForSearching(boolean includeBadQuality) {
        return includeBadQuality
                ? new HashSet<>(Arrays.asList(Quality.values()))
                : new HashSet<>(Collections.singletonList(Quality.GOOD));
    }

    public Either<List<ErrorInfoDto>, ReadingSeriesValueResponse> getGoodQualityValuesBetweenDates(ReadingRequest readingRequest) {
        List<Float> allGoodQualityValuesBetweenDates = readingDao.getAllGoodQualityValuesBetweenDates(readingRequest.getDateFrom(), readingRequest.getDateTo());

        return Objects.nonNull(allGoodQualityValuesBetweenDates)
                ? Either.right(new ReadingSeriesValueResponse(allGoodQualityValuesBetweenDates, readingRequest.getDateFrom(), readingRequest.getDateTo()))
                : Either.left(Collections.singletonList(new ErrorInfoDto("No series values specified")));
    }
}
