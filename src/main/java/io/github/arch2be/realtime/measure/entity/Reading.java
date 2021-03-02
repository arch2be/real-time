package io.github.arch2be.realtime.measure.entity;

import io.github.arch2be.realtime.measure.enums.EngineeringUnit;
import io.github.arch2be.realtime.measure.enums.Quality;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reading {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime date;
    private Float value;
    @Enumerated(EnumType.STRING)
    private EngineeringUnit unit;
    @Enumerated(EnumType.STRING)
    private Quality quality;

    @PersistenceConstructor
    public Reading() {
    }

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

        public Reading build() {
            Reading reading = new Reading();
            reading.unit = this.unit;
            reading.quality = this.quality;
            reading.date = this.date;
            reading.value = this.value;
            return reading;
        }
    }
}
