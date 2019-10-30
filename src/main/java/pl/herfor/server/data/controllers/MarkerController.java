package pl.herfor.server.data.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.requests.MarkerAddRequest;
import pl.herfor.server.data.objects.requests.MarkersLookupRequest;
import pl.herfor.server.data.repositories.MarkerRepository;
import pl.herfor.server.data.services.NotificationService;

import java.util.List;

@RestController
@AllArgsConstructor
public class MarkerController {
    private final MarkerRepository repository;
    private final NotificationService notificationService;

    @GetMapping(path = "/markers", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MarkerData> all() {
        return repository.findAll();
    }

    @GetMapping(path = "/markers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public MarkerData one(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

    @CrossOrigin
    @PostMapping(path = "/markers", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MarkerData> markersInArea(@RequestBody MarkersLookupRequest request) {
        if (request.date == null) {
            return repository.findBetween(request.northEast, request.southWest);
        } else {
            return repository.findBetweenSince(request.northEast, request.southWest, request.date);
        }
    }

    @PostMapping(path = "/markers/batch", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MarkerData> batchQuery(@RequestBody List<String> markerIds) {
        return repository.findMarkerDataByIdIn(markerIds);
    }

    @PostMapping(path = "/markers/create", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MarkerData> create(@RequestBody MarkerAddRequest request) {
        if (repository.findBetween(request.getLocation(), request.getLocation()).isEmpty()) {
            MarkerData saved = repository.save(request.toMarker());
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