package pl.herfor.server.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.herfor.server.data.objects.Point;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.enums.Severity;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, String> {
    @Query("SELECT m FROM Report m WHERE m.location.latitude BETWEEN :#{#southWest.latitude} AND :#{#northEast.latitude} " +
            "AND m.location.longitude between :#{#southWest.longitude} AND :#{#northEast.longitude} AND m.properties.severity <> 'NONE'")
    List<Report> findBetween(Point northEast, Point southWest);

    @Query("SELECT m FROM Report m WHERE m.location.latitude BETWEEN :#{#southWest.latitude} AND :#{#northEast.latitude} " +
            "AND m.location.longitude BETWEEN :#{#southWest.longitude} AND :#{#northEast.longitude} " +
            "AND ((m.properties.creationDate > :#{#date} AND m.properties.severity <> 'NONE') " +
            "OR (m.properties.creationDate < :#{#date} " +
            "AND m.properties.modificationDate > :#{#date}))")
    List<Report> findBetweenSince(Point northEast, Point southWest, OffsetDateTime date);

    List<Report> findReportByIdIn(List<String> markerIds);

    @Query("SELECT m FROM Report m WHERE m.properties.severity <> 'NONE'" +
            "AND m.properties.expiryDate < :#{#date}")
    List<Report> findExpiredMarkers(OffsetDateTime date);

    @Query("SELECT m FROM Report m WHERE m.properties.severity <> :#{#severity} " +
            "AND m.properties.expiryDate > :#{#minDate} " +
            "AND m.properties.expiryDate < :#{#maxDate}")
    List<Report> findChangeableMarkers(Severity severity, OffsetDateTime minDate, OffsetDateTime maxDate);

    @Transactional
    @Modifying
    @Query("UPDATE Report m SET m.properties.severity =:#{#severity}, " +
            "m.properties.modificationDate = current_timestamp WHERE m.id in (:#{#idList})")
    void changeMarkerSeverity(List<String> idList, Severity severity);

    @Transactional
    @Modifying
    @Query(value = "UPDATE report SET " +
            "modification_date = current_timestamp, " +
            "expiry_date = expiry_date + cast((:#{#seconds} || ' seconds') as interval) " +
            "WHERE id = :#{#id}", nativeQuery = true)
    void changeReportExpiryDate(String id, int seconds);
}