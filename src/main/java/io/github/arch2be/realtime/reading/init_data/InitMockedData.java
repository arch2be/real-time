package io.github.arch2be.realtime.reading.init_data;

import io.github.arch2be.realtime.reading.enums.Quality;
import io.github.arch2be.realtime.reading.entity.Reading;
import io.github.arch2be.realtime.reading.repository.ReadingDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Stream;

@Profile("DEV")
@Component
class InitMockedData implements CommandLineRunner {

    private final ReadingDao readingDao;
    private final Integer recordsAmount = 100;
    private final Random random = new Random();

    InitMockedData(ReadingDao readingDao) {
        this.readingDao = readingDao;
    }

    @Override
    public void run(String... args) {
        Stream.iterate(0, n -> n++)
                .limit(recordsAmount)
                .forEach(number -> readingDao.save(buildMockedReadingRecord()));
    }

    private Reading buildMockedReadingRecord() {
        return new Reading.Builder()
                .withQuality(getRandomQuality())
                .withValue(getRandomValue())
                .withDate(getRandomDate())
                .withUnit(null)
                .build();
    }

    private LocalDateTime getRandomDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime
                .plusDays(getRandomIntegerBetween(5, 10))
                .plusMinutes(getRandomIntegerBetween(5, 10))
                .plusSeconds(getRandomIntegerBetween(5, 10))
                .plusHours(getRandomIntegerBetween(5, 10));
    }

    private Integer getRandomIntegerBetween(Integer from, Integer to) {
        return random.nextInt(to - from + 1) + from;
    }

    private Float getRandomValue() {
        float leftLimit = 1F;
        float rightLimit = 100F;
        return leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);
    }

    private Quality getRandomQuality() {
        return random.nextInt(100) % 2 != 0
                ? Quality.BAD
                : Quality.GOOD;
    }
}
