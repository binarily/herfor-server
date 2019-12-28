package pl.herfor.server.data.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.Constants;
import pl.herfor.server.data.objects.Report;
import pl.herfor.server.data.objects.User;
import pl.herfor.server.data.objects.requests.ReportAddRequest;
import pl.herfor.server.data.objects.requests.ReportSearchRequest;
import pl.herfor.server.data.repositories.ReportRepository;
import pl.herfor.server.data.repositories.UserRepository;
import pl.herfor.server.data.services.NotificationService;

import java.util.List;

import static pl.herfor.server.data.Constants.DEV_SWITCH;

@RestController
@AllArgsConstructor
@Slf4j
public class ReportController {
    private final ReportRepository repository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @GetMapping(path = "/reports", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Report> all() {
        return repository.findAll();
    }

    @GetMapping(path = "/reports/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Report one(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @CrossOrigin
    @PostMapping(path = "/reports", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Report> reportsInArea(@RequestBody ReportSearchRequest request) {
        if (request.getDate() == null) {
            return repository.findBetween(request.getNorthEast(), request.getSouthWest());
        } else {
            return repository.findBetweenSince(request.getNorthEast(), request.getSouthWest(), request.getDate());
        }
    }

    @PostMapping(path = "/reports/batch", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Report> batchQuery(@RequestBody List<String> markerIds) {
        return repository.findReportByIdIn(markerIds);
    }

    @PostMapping(path = "/reports/create", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Report> create(@RequestBody ReportAddRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (DEV_SWITCH) {
            if (!repository.findBetween(request.getLocation(), request.getLocation()).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            log.debug("Report location check disabled (dev switch)");
        }
        User creatingUser = userRepository.getOne(request.getUserId());
        Report toSave = request.toReport(userRepository.getOne(request.getUserId()));
        toSave.getProperties()
                .setModificationDate(toSave.getProperties()
                        .getCreationDate()
                        .plusSeconds((long) (Constants.REGULAR_EXPIRY_DURATION * creatingUser.calculateReliability()))
                );
        Report saved = repository.save(toSave);
        try {
            notificationService.notifyAboutNewReport(saved);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }
}