package pl.herfor.server.data.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.requests.ReportAddRequest;
import pl.herfor.server.data.objects.requests.ReportSearchRequest;
import pl.herfor.server.data.repositories.ReportRepository;
import pl.herfor.server.data.services.NotificationService;

import java.util.List;

@RestController
@AllArgsConstructor
public class MarkerController {
    private final ReportRepository repository;
    private final NotificationService notificationService;

    @GetMapping(path = "/markers", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Report> all() {
        return repository.findAll();
    }

    @GetMapping(path = "/markers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Report one(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @CrossOrigin
    @PostMapping(path = "/markers", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Report> markersInArea(@RequestBody ReportSearchRequest request) {
        if (request.date == null) {
            return repository.findBetween(request.northEast, request.southWest);
        } else {
            return repository.findBetweenSince(request.northEast, request.southWest, request.date);
        }
    }

    @PostMapping(path = "/markers/batch", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Report> batchQuery(@RequestBody List<String> markerIds) {
        return repository.findMarkerDataByIdIn(markerIds);
    }

    @PostMapping(path = "/markers/create", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Report> create(@RequestBody ReportAddRequest request) {
        if (repository.findBetween(request.getLocation(), request.getLocation()).isEmpty()) {
            Report saved = repository.save(request.toMarker());
            try {
                notificationService.notifyAboutNewMarker(saved);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}