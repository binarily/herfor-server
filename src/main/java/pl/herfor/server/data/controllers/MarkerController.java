package pl.herfor.server.data.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.MarkersLookupRequest;
import pl.herfor.server.data.repositories.MarkerRepository;

import java.util.List;

@RestController
public class MarkerController {
    private final MarkerRepository repository;

    MarkerController(MarkerRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/markers", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MarkerData> all() {
        return repository.findAll();
    }

    @GetMapping(path = "/markers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public MarkerData one(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }

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
    public MarkerData create(@RequestBody MarkerData markerData) {
        return repository.save(markerData);
    }

}