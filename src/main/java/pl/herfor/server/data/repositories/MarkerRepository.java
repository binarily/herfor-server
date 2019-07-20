package pl.herfor.server.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.herfor.server.data.objects.Marker;
import pl.herfor.server.data.objects.Point;

import java.util.List;

public interface MarkerRepository extends JpaRepository<Marker, String> {
    @Query("select m from Marker m where m.location.latitude between :#{#southWest.latitude} and :#{#northEast.latitude} " +
            "and m.location.longitude between :#{#southWest.longitude} and :#{#northEast.longitude}")
    List<Marker> findBetween(@Param("northEast") Point northEast, @Param("southWest") Point southWest);
}