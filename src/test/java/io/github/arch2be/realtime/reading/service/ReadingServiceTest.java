package io.github.arch2be.realtime.reading.service;


import io.github.arch2be.realtime.reading.dto.ErrorInfoDto;
import io.github.arch2be.realtime.reading.dto.ReadingDto;
import io.github.arch2be.realtime.reading.entity.Reading;
import io.github.arch2be.realtime.reading.enums.Quality;
import io.github.arch2be.realtime.reading.repository.ReadingDao;
import io.vavr.control.Either;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ReadingServiceTest {
    private ReadingService readingService;

    @Nested
    class ReadingServiceLatestValueByDateEmptyData {

        @BeforeEach
        void mockedReadingRepository() {
            ReadingDao readingDao = Mockito.mock(ReadingDao.class);

            Mockito
                    .when(readingDao.findFirstByOrderByDateDesc())
                    .thenReturn(Optional.empty());

            readingService = new ReadingService(readingDao);
        }

        @Test
        void getLatestValueByDate() {
            Either<ErrorInfoDto, ReadingDto> latestValueByDate = readingService.getLatestValueByDate();

            assertTrue(latestValueByDate.isLeft());

            assertEquals(latestValueByDate.getLeft().getMessage(), "No values specified");
        }
    }

    @Nested
    class ReadingServiceLatestValueByDateCorrectData {

        @BeforeEach
        void mockedReadingRepository() {
            ReadingDao readingDao = Mockito.mock(ReadingDao.class);
            Reading reading = new Reading.Builder()
                    .withValue(9.89f)
                    .withDate(LocalDateTime.of(2021, 3, 2, 10, 44, 44))
                    .withUnit(null)
                    .withQuality(Quality.GOOD)
                    .build();

            Mockito
                    .when(readingDao.findFirstByOrderByDateDesc())
                    .thenReturn(Optional.of(reading));

            readingService = new ReadingService(readingDao);
        }

        @Test
        void getLatestValueByDate() {
            Either<ErrorInfoDto, ReadingDto> latestValueByDate = readingService.getLatestValueByDate();

            assertTrue(latestValueByDate.isRight());

            assertEquals(latestValueByDate.get().getValue(), 9.89f);
            assertEquals(latestValueByDate.get().getQuality(), Quality.GOOD);
            assertEquals(latestValueByDate.get().getDate(), LocalDateTime.of(2021, 3, 2, 10, 44, 44));
        }
    }

    @Nested
    class ReadingServiceGoodQualityValuesBetweenDates {

        @BeforeEach
        void mockedReadingRepository() {
            ReadingDao readingDao = Mockito.mock(ReadingDao.class);

            Mockito
                    .when(readingDao.getAllGoodQualityValuesBetweenDates(
                            LocalDateTime.of(2021, 3, 2, 10, 44, 44),
                            LocalDateTime.of(2021, 3, 4, 10, 44, 44)))
                    .thenReturn(Arrays.asList(1f, 33f, 45.5f, 23.89f));

            readingService = new ReadingService(readingDao);
        }

        @Test
        void getGoodQualityValuesBetweenDatesCorrectRange() {
            Either<List<ErrorInfoDto>, List<Float>> goodQualityValuesBetweenDates = readingService.getGoodQualityValuesBetweenDates(
                    LocalDateTime.of(2021, 3, 2, 10, 44, 44),
                    LocalDateTime.of(2021, 3, 4, 10, 44, 44));

            assertTrue(goodQualityValuesBetweenDates.isRight());

            assertEquals(goodQualityValuesBetweenDates.get().size(), 4);
            assertIterableEquals(goodQualityValuesBetweenDates.get(), Arrays.asList(1f, 33f, 45.5f, 23.89f));
        }

        @Test
        void getGoodQualityValuesBetweenDatesWrongRange() {
            Either<List<ErrorInfoDto>, List<Float>> goodQualityValuesBetweenDates = readingService.getGoodQualityValuesBetweenDates(
                    LocalDateTime.of(2021, 4, 2, 10, 44, 44),
                    LocalDateTime.of(2021, 4, 4, 10, 44, 44));

            assertTrue(goodQualityValuesBetweenDates.isRight());
            assertTrue(goodQualityValuesBetweenDates.get().isEmpty());
        }
    }

    @Nested
    class ReadingServiceAverageValueForCondition {

        @BeforeEach
        void mockedReadingRepository() {
            ReadingDao readingDao = Mockito.mock(ReadingDao.class);

            Mockito
                    .when(readingDao.getAverageValueBetweenDate(
                            LocalDateTime.of(2021, 3, 2, 10, 44, 44),
                            LocalDateTime.of(2021, 3, 4, 10, 44, 44),
                            new HashSet<>(Arrays.asList(Quality.values()))))
                    .thenReturn(76.14f);

            Mockito
                    .when(readingDao.getAverageValueBetweenDate(
                            LocalDateTime.of(2021, 3, 2, 10, 44, 44),
                            LocalDateTime.of(2021, 3, 4, 10, 44, 44),
                            new HashSet<>(Collections.singletonList(Quality.GOOD))))
                    .thenReturn(45.56f);

            Mockito
                    .when(readingDao.getAverageValueBetweenDate(
                            LocalDateTime.of(2021, 4, 5, 10, 44, 44),
                            LocalDateTime.of(2021, 4, 6, 10, 44, 44),
                            new HashSet<>(Collections.singletonList(Quality.GOOD))))
                    .thenReturn(null);

            readingService = new ReadingService(readingDao);
        }

        @Test
        void getAverageValueForConditionForCorrectRanges() {
            Either<List<ErrorInfoDto>, Float> averageValueForConditionWithoutBadQuality = readingService.getAverageValueForCondition(
                    LocalDateTime.of(2021, 3, 2, 10, 44, 44),
                    LocalDateTime.of(2021, 3, 4, 10, 44, 44),
                    false);

            Either<List<ErrorInfoDto>, Float> averageValueForConditionWithBadQuality = readingService.getAverageValueForCondition(
                    LocalDateTime.of(2021, 3, 2, 10, 44, 44),
                    LocalDateTime.of(2021, 3, 4, 10, 44, 44),
                    true);

            assertTrue(averageValueForConditionWithBadQuality.isRight());
            assertTrue(averageValueForConditionWithoutBadQuality.isRight());

            assertEquals(averageValueForConditionWithoutBadQuality.get(), 45.56f);
            assertEquals(averageValueForConditionWithBadQuality.get(), 76.14f);
        }

        @Test
        void getGoodQualityValuesBetweenDatesWrongRange() {
            Either<List<ErrorInfoDto>, Float> averageValueForCondition = readingService.getAverageValueForCondition(
                    LocalDateTime.of(2021, 4, 5, 10, 44, 44),
                    LocalDateTime.of(2021, 4, 6, 10, 44, 44),
                    false);

            assertTrue(averageValueForCondition.isLeft());
            assertEquals(averageValueForCondition.getLeft().get(0).getMessage(), "No average value specified");
        }
    }
}