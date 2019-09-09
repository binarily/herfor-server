package pl.herfor.server.data.controllers;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.herfor.server.data.objects.MarkerData;
import pl.herfor.server.data.objects.MarkersLookupRequest;
import pl.herfor.server.data.repositories.MarkerRepository;

import java.util.List;

@RestController
public class MarkerController {
    private final MarkerRepository repository;
    private static final String NOTIFICATION_TOPIC = "marker";

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
        MarkerData saved = repository.save(markerData);
        sendNotification(markerData);
        return saved;
    }

    private void sendNotification(@RequestBody MarkerData markerData) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        Message message = Message.builder()
                .putData("id", markerData.getId())
                .putData("latitude", String.valueOf(markerData.getLocation().latitude))
                .putData("longitude", String.valueOf(markerData.getLocation().longitude))
                .putData("marker", gson.toJson(markerData))
                .setTopic(NOTIFICATION_TOPIC)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}