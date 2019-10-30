package pl.herfor.server.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.Point;
import pl.herfor.server.data.objects.enums.Severity;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

public interface MarkerRepository extends JpaRepository<MarkerData, String> {
    @Query("SELECT m from MarkerData m where m.location.latitude between :#{#southWest.latitude} and :#{#northEast.latitude} " +
            "and m.location.longitude between :#{#southWest.longitude} and :#{#northEast.longitude} and m.properties.severity <> 'NONE'")
    List<MarkerData> findBetween(Point northEast, Point southWest);

    @Query("select m from MarkerData m where m.location.latitude between :#{#southWest.latitude} and :#{#northEast.latitude} " +
            "and m.location.longitude between :#{#southWest.longitude} and :#{#northEast.longitude} " +
            "and ((m.properties.creationDate > :#{#date} and m.properties.severity <> 'NONE') " +
            "or (m.properties.creationDate < :#{#date} " +
            "and m.properties.modificationDate > :#{#date}))")
    List<MarkerData> findBetweenSince(Point northEast, Point southWest, OffsetDateTime date);

    List<MarkerData> findMarkerDataByIdIn(List<String> markerIds);

    @Query("SELECT m FROM MarkerData m WHERE m.properties.severity <> 'NONE'" +
            "AND m.properties.expiryDate < :#{#date}")
    List<MarkerData> findExpiredMarkers(OffsetDateTime date);

    @Query("SELECT m FROM MarkerData m WHERE m.properties.severity <> :#{#severity} " +
            "AND m.properties.expiryDate > :#{#minDate} " +
            "AND m.properties.expiryDate < :#{#maxDate}")
    List<MarkerData> findChangeableMarkers(Severity severity, OffsetDateTime minDate, OffsetDateTime maxDate);

    @Transactional
    @Modifying
    @Query("UPDATE MarkerData m SET m.properties.severity =:#{#severity}, " +
            "m.properties.modificationDate = :#{#date} WHERE m.id in (:#{#idList})")
    void changeMarkerSeverity(List<String> idList, OffsetDateTime date, Severity severity);
}