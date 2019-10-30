package pl.herfor.server.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.herfor.server.data.objects.ReportGrade;

import java.util.List;

public interface GradeRepository extends JpaRepository<ReportGrade, String> {
    @Query("SELECT g FROM ReportGrade g WHERE g.marker.id = :#{#markerId}")
    public List<ReportGrade> getGradesFor(String markerId);
}