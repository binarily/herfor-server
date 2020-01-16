package pl.herfor.server.data.controllers;

import lombok.AllArgsConstructor;
import org.apache.lucene.util.SloppyMath;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.Constants;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.ReportGrade;
import pl.herfor.server.data.objects.User;
import pl.herfor.server.data.objects.enums.Accident;
import pl.herfor.server.data.objects.enums.Grade;
import pl.herfor.server.data.objects.requests.ReportGradeRequest;
import pl.herfor.server.data.repositories.GradeRepository;
import pl.herfor.server.data.repositories.ReportRepository;
import pl.herfor.server.data.repositories.UserRepository;

import java.util.List;

@RestController
@AllArgsConstructor
public class GradeController {
    private final GradeRepository repository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @GetMapping(path = "/grades", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ReportGrade> getAll() {
        return repository.findAll();
    }

    @GetMapping(path = "/grades/report/{reportId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ReportGrade> getForMarker(@PathVariable String reportId) {
        return repository.getGradesFor(reportId);
    }

    @PostMapping(path = "/grades/add", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ReportGrade> add(@RequestBody ReportGradeRequest gradeRequest) {
        if (!userRepository.existsById(gradeRequest.getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Report report = reportRepository.findById(gradeRequest.getReportId()).orElse(null);
        if (report == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ReportGrade addedGrade = repository.save(gradeRequest.toReportGrade(report));

        int timeDiff = calculateTimeDifference(gradeRequest, report);
        reportRepository.changeReportExpiryDate(report.getId(), timeDiff);
        return new ResponseEntity<>(addedGrade, HttpStatus.OK);
    }

    private int calculateTimeDifference(ReportGradeRequest gradeRequest, Report report) {
        int gradeCoefficient = gradeRequest.getGrade() == Grade.RELEVANT ? 1 : -1;
        double gradeReportDistance = SloppyMath.haversinMeters(gradeRequest.getLocation().getLatitude(),
                gradeRequest.getLocation().getLongitude(),
                report.getLocation().getLatitude(), report.getLocation().getLongitude());
        double accidentRadius = distanceFromAccident(report.getProperties().getAccident());
        double distanceCoefficient = (accidentRadius - gradeReportDistance) / accidentRadius;
        double timeCoefficient = ((double) Constants.REGULAR_EXPIRY_DURATION -
                (((double) System.currentTimeMillis() / 1000) - report.getProperties().getCreationDate().toEpochSecond()))
                / Constants.REGULAR_EXPIRY_DURATION;
        User submittingUser = userRepository.getOne(gradeRequest.getUserId());
        double userCoefficient = 1 + submittingUser.calculateReliability() / Constants.REGULAR_EXPIRY_DURATION;
        return (int) (gradeCoefficient * (1 + Math.tanh(Math.tanh(distanceCoefficient) + Math.tanh(timeCoefficient)
                + Math.tanh(userCoefficient))) * Constants.GRADE_IMPACT_DURATION);
    }

    private int distanceFromAccident(Accident accident) {
        switch (accident) {
            case BUS:
            case METRO:
            case RAIL:
            case TRAM:
                return 1600;
            case BIKE:
                return 800;
            case PEDESTRIAN:
                return 400;
        }
        return 200;
    }
}
