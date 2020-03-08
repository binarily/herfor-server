package pl.herfor.server.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.herfor.server.data.objects.User;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, String> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE report_user SET " +
            "report_durations_count = report_durations_count + 1, " +
            "report_durations = report_durations + :#{#duration} " +
            "WHERE id = :#{#id}", nativeQuery = true)
    void updateReliabilityMetric(String id, long duration);
}
