package io.github.arch2be.realtime.reading.repository;

import io.github.arch2be.realtime.reading.entity.Reading;
import io.github.arch2be.realtime.reading.enums.Quality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ReadingDao extends JpaRepository<Reading, Long> {

    Optional<Reading> findFirstByOrderByDateDesc();

    @Query(value = "SELECT AVG(r.value) FROM Reading r WHERE (r.date BETWEEN ?1 AND ?2) AND (r.quality in ?3)")
    Float getAverageValueBetweenDate(LocalDateTime dateFrom, LocalDateTime dateTo, Set<Quality> qualityToAverage);

    @Query(value = "SELECT r.value FROM Reading r WHERE (r.date BETWEEN ?1 AND ?2) AND (r.quality like 'GOOD')")
    List<Float> getAllGoodQualityValuesBetweenDates(LocalDateTime dateFrom, LocalDateTime dateTo);
}
