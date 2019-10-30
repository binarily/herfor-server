package pl.herfor.server.data.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.enums.Severity;
import pl.herfor.server.data.repositories.MarkerRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ScheduledService {
    private final MarkerRepository markerRepository;
    private final NotificationService notificationService;

    //TODO: change before release
    //@Scheduled(fixedRate = 10 * 1000)
    public void invalidateExpiredMarkers() {
        log.debug("Marker expiry procedure triggered.");
        List<MarkerData> markersToRemove = markerRepository.findExpiredMarkers(OffsetDateTime.now());
        markersToRemove.forEach(notificationService::notifyAboutRemovedMarker);
        if (!markersToRemove.isEmpty()) {
            markerRepository.changeMarkerSeverity(markersToRemove.stream()
                    .map(MarkerData::getId)
                    .collect(Collectors.toList()), OffsetDateTime.now(), Severity.NONE
            );
        }
        log.debug("Invalidated {} markers.", markersToRemove.size());
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void switchGreenMarkerSeverities() {
        log.debug("Marker greening procedure triggered.");
        List<MarkerData> markersToGreen = markerRepository.findChangeableMarkers(Severity.GREEN,
                OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(20));
        markersToGreen.forEach(notificationService::notifyAboutUpdatedMarker);
        if (!markersToGreen.isEmpty()) {
            markerRepository.changeMarkerSeverity(markersToGreen.stream()
                    .map(MarkerData::getId)
                    .collect(Collectors.toList()), OffsetDateTime.now(), Severity.GREEN
            );
        }
        log.debug("Changed {} markers.", markersToGreen.size());
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void switchYellowMarkerSeverities() {
        log.debug("Marker yellowing procedure triggered.");
        List<MarkerData> markersToYellow = markerRepository.findChangeableMarkers(Severity.YELLOW,
                OffsetDateTime.now().plusMinutes(20), OffsetDateTime.now().plusMinutes(35));
        markersToYellow.forEach(notificationService::notifyAboutUpdatedMarker);
        if (!markersToYellow.isEmpty()) {
            markerRepository.changeMarkerSeverity(markersToYellow.stream()
                    .map(MarkerData::getId)
                    .collect(Collectors.toList()), OffsetDateTime.now(), Severity.YELLOW
            );
        }
        log.debug("Changed {} markers.", markersToYellow.size());
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void switchRedMarkerSeverities() {
        log.debug("Marker redding procedure triggered.");
        List<MarkerData> markersToRed = markerRepository.findChangeableMarkers(Severity.RED,
                OffsetDateTime.now().plusMinutes(35), OffsetDateTime.now().plusHours(48));
        markersToRed.forEach(notificationService::notifyAboutUpdatedMarker);
        if (!markersToRed.isEmpty()) {
            markerRepository.changeMarkerSeverity(markersToRed.stream()
                    .map(MarkerData::getId)
                    .collect(Collectors.toList()), OffsetDateTime.now(), Severity.RED
            );
        }
        log.debug("Changed {} markers.", markersToRed.size());
    }
}
