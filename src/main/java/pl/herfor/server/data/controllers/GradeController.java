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

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class GradeController {
    private final GradeRepository repository;
    private final ReportRepository reportRepository;

    @GetMapping(path = "/grades", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ReportGrade> getAll() {
        return repository.findAll();
    }

    @GetMapping(path = "/grades/marker/{markerId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ReportGrade> getForMarker(@PathVariable String markerId) {
        return repository.getGradesFor(markerId);
    }

    @PostMapping(path = "/grades/add", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ReportGrade> add(@RequestBody ReportGradeRequest gradeRequest) {
        Report marker = reportRepository.findById(gradeRequest.reportId).orElse(null);
        if (marker == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ReportGrade addedGrade = repository.save(gradeRequest.toMarkerGrade(marker));
        modifyMarkerWithGrade(gradeRequest, marker);
        reportRepository.save(marker);
        return new ResponseEntity<>(addedGrade, HttpStatus.CREATED);
    }

    private void modifyMarkerWithGrade(ReportGradeRequest gradeRequest, Report marker) {
        OffsetDateTime dateToModify = marker.getProperties().getExpiryDate();
        if (gradeRequest.getGrade() == Grade.RELEVANT) {
            dateToModify = dateToModify.plusSeconds(30);
        } else if (gradeRequest.getGrade() == Grade.NOT_RELEVANT) {
            dateToModify = dateToModify.minusSeconds(30);
        }
        marker.getProperties().setExpiryDate(dateToModify);
        marker.getProperties().setModificationDate(OffsetDateTime.now());
    }
}
