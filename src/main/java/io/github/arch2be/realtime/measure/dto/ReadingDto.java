package io.github.arch2be.realtime.measure.dto;

import io.github.arch2be.realtime.measure.enums.EngineeringUnit;
import io.github.arch2be.realtime.measure.enums.Quality;

import java.time.LocalDateTime;

public class ReadingDto {
    private LocalDateTime date;
    private Float value;
    private EngineeringUnit unit;
    private Quality quality;

    public LocalDateTime getDate() {
        return date;
    }

    public Float getValue() {
        return value;
    }

    public EngineeringUnit getUnit() {
        return unit;
    }

    public Quality getQuality() {
        return quality;
    }


    public static final class Builder {
        private LocalDateTime date;
        private Float value;
        private EngineeringUnit unit;
        private Quality quality;

        public Builder() {
        }

        public Builder withDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder withValue(Float value) {
            this.value = value;
            return this;
        }

        public Builder withUnit(EngineeringUnit unit) {
            this.unit = unit;
            return this;
        }

        public Builder withQuality(Quality quality) {
            this.quality = quality;
            return this;
        }

        public ReadingDto build() {
            ReadingDto readingDto = new ReadingDto();
            readingDto.quality = this.quality;
            readingDto.date = this.date;
            readingDto.unit = this.unit;
            readingDto.value = this.value;
            return readingDto;
        }
    }
}
