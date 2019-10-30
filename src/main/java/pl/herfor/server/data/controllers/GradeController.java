package pl.herfor.server.data.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.MarkerGrade;
import pl.herfor.server.data.objects.enums.Grade;
import pl.herfor.server.data.objects.requests.MarkerGradeRequest;
import pl.herfor.server.data.repositories.GradeRepository;
import pl.herfor.server.data.repositories.MarkerRepository;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class GradeController {
    private final GradeRepository repository;
    private final MarkerRepository markerRepository;

    @GetMapping(path = "/grades", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MarkerGrade> getAll() {
        return repository.findAll();
    }

    @GetMapping(path = "/grades/marker/{markerId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MarkerGrade> getForMarker(@PathVariable String markerId) {
        return repository.getGradesFor(markerId);
    }

    @PostMapping(path = "/grades/add", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MarkerGrade> add(@RequestBody MarkerGradeRequest gradeRequest) {
        MarkerData marker = markerRepository.findById(gradeRequest.markerId).orElse(null);
        if (marker == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MarkerGrade addedGrade = repository.save(gradeRequest.toMarkerGrade(marker));
        modifyMarkerWithGrade(gradeRequest, marker);
        markerRepository.save(marker);
        return new ResponseEntity<>(addedGrade, HttpStatus.CREATED);
    }

    private void modifyMarkerWithGrade(MarkerGradeRequest gradeRequest, MarkerData marker) {
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
