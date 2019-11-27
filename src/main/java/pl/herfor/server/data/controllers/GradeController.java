package pl.herfor.server.data.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.ReportGrade;
import pl.herfor.server.data.objects.enums.Grade;
import pl.herfor.server.data.objects.requests.ReportGradeRequest;
import pl.herfor.server.data.repositories.GradeRepository;
import pl.herfor.server.data.repositories.ReportRepository;
import pl.herfor.server.data.repositories.UserRepository;

import java.time.OffsetDateTime;
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
        Report report = reportRepository.findById(gradeRequest.reportId).orElse(null);
        if (report == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!userRepository.existsById(gradeRequest.getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ReportGrade addedGrade = repository.save(gradeRequest.toReportGrade(report));
        modifyReportWithGrade(gradeRequest, report);
        reportRepository.save(report);
        return new ResponseEntity<>(addedGrade, HttpStatus.OK);
    }

    private void modifyReportWithGrade(ReportGradeRequest gradeRequest, Report report) {
        OffsetDateTime dateToModify = report.getProperties().getExpiryDate();
        if (gradeRequest.getGrade() == Grade.RELEVANT) {
            dateToModify = dateToModify.plusSeconds(30);
        } else if (gradeRequest.getGrade() == Grade.NOT_RELEVANT) {
            dateToModify = dateToModify.minusSeconds(30);
        }
        report.getProperties().setExpiryDate(dateToModify);
        report.getProperties().setModificationDate(OffsetDateTime.now());
    }
}
