package pl.herfor.server.data.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.exceptions.notfound.MarkerNotFoundException;
import pl.herfor.server.data.objects.Marker;
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
    public List<Marker> all() {
        return repository.findAll();
    }

    @GetMapping(path = "/markers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Marker one(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new MarkerNotFoundException(id));
    }

    @PostMapping(path = "/markers", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Marker> markersInArea(@RequestBody MarkersLookupRequest request) {
        return repository.findBetween(request.northEast, request.southWest);
    }

    @PostMapping(path = "/markers/create", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Marker create(@RequestBody Marker marker) {
        return repository.save(marker);
    }

}