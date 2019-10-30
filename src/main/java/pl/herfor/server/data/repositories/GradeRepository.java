package pl.herfor.server.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.herfor.server.data.objects.MarkerGrade;

import java.util.List;

public interface GradeRepository extends JpaRepository<MarkerGrade, String> {
    @Query("SELECT g FROM MarkerGrade g WHERE g.marker.id = :#{#markerId}")
    public List<MarkerGrade> getGradesFor(String markerId);
}