package htl.steyr.mood_tracking.application.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    Optional<Entry> findByDateAndUser(@Temporal(TemporalType.DATE) Date date, User user);

    @Query("SELECT e FROM Entry e " +
           "WHERE e.date >= :startDate AND e.date <= :endDate " +
           "AND e.user = :user")
    List<Entry> findAllBetweenDates(User user, Date startDate, Date endDate);
}
