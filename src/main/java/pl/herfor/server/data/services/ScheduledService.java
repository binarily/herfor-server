package pl.herfor.server.data.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.herfor.server.data.Constants;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.enums.Severity;
import pl.herfor.server.data.repositories.ReportRepository;
import pl.herfor.server.data.repositories.UserRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ScheduledService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 10 * 1000)
    public void invalidateExpiredMarkers() {
        if (Constants.DEV_SWITCH) {
            log.info("Marker expiry procedure ignored (dev switch).");
            return;
        }
        log.info("Marker expiry procedure triggered.");
        List<Report> markersToRemove = reportRepository.findExpiredMarkers(OffsetDateTime.now());
        if (!markersToRemove.isEmpty()) {
            reportRepository.changeMarkerSeverity(markersToRemove.stream()
                    .map(Report::getId)
                    .collect(Collectors.toList()), Severity.NONE
            );
        }
        markersToRemove.forEach(notificationService::notifyAboutRemovedReport);
        markersToRemove.forEach(report -> {
            long totalDuration = Duration.between(report.getProperties().getCreationDate(),
                    report.getProperties().getExpiryDate()).toNanos() / 1000000;
            userRepository.updateReliabilityMetric(report.getUser().getId(), totalDuration - Constants.REGULAR_EXPIRY_DURATION);
        });
        log.info("Expired {} markers.", markersToRemove.size());
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void switchGreenMarkerSeverities() {
        if (Constants.DEV_SWITCH) {
            log.info("Marker greening procedure ignored (dev switch).");
            return;
        }
        log.info("Marker greening procedure triggered.");
        List<Report> markersToGreen = reportRepository.findChangeableMarkers(Severity.GREEN,
                OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(20));
        markersToGreen.forEach(notificationService::notifyAboutUpdatedReport);
        if (!markersToGreen.isEmpty()) {
            reportRepository.changeMarkerSeverity(markersToGreen.stream()
                    .map(Report::getId)
                    .collect(Collectors.toList()), Severity.GREEN);
        }
        log.info("Changed {} markers.", markersToGreen.size());
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void switchYellowMarkerSeverities() {
        if (Constants.DEV_SWITCH) {
            log.info("Marker yellowing procedure ignored (dev switch).");
            return;
        }
        log.info("Marker yellowing procedure triggered.");
        List<Report> markersToYellow = reportRepository.findChangeableMarkers(Severity.YELLOW,
                OffsetDateTime.now().plusMinutes(20), OffsetDateTime.now().plusMinutes(35));
        markersToYellow.forEach(notificationService::notifyAboutUpdatedReport);
        if (!markersToYellow.isEmpty()) {
            reportRepository.changeMarkerSeverity(markersToYellow.stream()
                    .map(Report::getId)
                    .collect(Collectors.toList()), Severity.YELLOW);
        }
        log.info("Changed {} markers.", markersToYellow.size());
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void switchRedMarkerSeverities() {
        if (Constants.DEV_SWITCH) {
            log.info("Marker redding procedure ignored (dev switch).");
            return;
        }
        log.info("Marker redding procedure triggered.");
        List<Report> markersToRed = reportRepository.findChangeableMarkers(Severity.RED,
                OffsetDateTime.now().plusMinutes(35), OffsetDateTime.now().plusHours(48));
        markersToRed.forEach(notificationService::notifyAboutUpdatedReport);
        if (!markersToRed.isEmpty()) {
            reportRepository.changeMarkerSeverity(markersToRed.stream()
                    .map(Report::getId)
                    .collect(Collectors.toList()), Severity.RED);
        }
        log.info("Changed {} markers.", markersToRed.size());
    }
}
