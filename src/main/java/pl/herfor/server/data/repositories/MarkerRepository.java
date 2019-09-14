package pl.herfor.server.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.Point;

import java.util.Date;
import java.util.List;

public interface MarkerRepository extends JpaRepository<MarkerData, String> {
    @Query("SELECT m from MarkerData m where m.location.latitude between :#{#southWest.latitude} and :#{#northEast.latitude} " +
            "and m.location.longitude between :#{#southWest.longitude} and :#{#northEast.longitude}")
    List<MarkerData> findBetween(@Param("northEast") Point northEast, @Param("southWest") Point southWest);

    @Query("select m from MarkerData m where m.location.latitude between :#{#southWest.latitude} and :#{#northEast.latitude} " +
            "and m.location.longitude between :#{#southWest.longitude} and :#{#northEast.longitude} " +
            "and m.properties.modificationDate > :#{#date} and m.properties.severityType <> :#{#SeverityType.NONE}")
    List<MarkerData> findBetweenSince(@Param("northEast") Point northEast, @Param("southWest") Point southWest, @Param("date") Date date);

    List<MarkerData> findMarkerDataByIdIn(List<String> markerIds);
}