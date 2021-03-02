package io.github.arch2be.realtime.reading.service;

import io.github.arch2be.realtime.reading.dto.ErrorInfoDto;
import io.github.arch2be.realtime.reading.dto.ReadingDto;
import io.github.arch2be.realtime.reading.enums.Quality;
import io.github.arch2be.realtime.reading.repository.ReadingDao;
import io.vavr.control.Either;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Either<List<ErrorInfoDto>, List<Float>> getGoodQualityValuesBetweenDates(LocalDateTime dateFrom, LocalDateTime dateTo) {
        List<Float> allGoodQualityValuesBetweenDates = readingDao.getAllGoodQualityValuesBetweenDates(dateFrom, dateTo);

        return Objects.nonNull(allGoodQualityValuesBetweenDates)
                ? Either.right(allGoodQualityValuesBetweenDates)
                : Either.left(Collections.singletonList(new ErrorInfoDto("No series values specified")));
    }

    public Either<List<ErrorInfoDto>, Float> getAverageValueForCondition(LocalDateTime dateFrom, LocalDateTime dateTo, boolean includedBadQuality) {
        Float averageValueBetweenDate = readingDao.getAverageValueBetweenDate(dateFrom, dateTo, getQualitiesForSearching(includedBadQuality));

        return Objects.nonNull(averageValueBetweenDate)
                ? Either.right(averageValueBetweenDate)
                : Either.left(Collections.singletonList(new ErrorInfoDto("No average value specified")));
    }

    private Set<Quality> getQualitiesForSearching(boolean includeBadQuality) {
        return includeBadQuality
                ? new HashSet<>(Arrays.asList(Quality.values()))
                : new HashSet<>(Collections.singletonList(Quality.GOOD));
    }
}
