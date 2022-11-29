package htl.steyr.mood_tracking.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
