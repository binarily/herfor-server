package pl.herfor.server.data.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.herfor.server.data.objects.Report;

import static pl.herfor.server.data.Constants.*;

@Service
@AllArgsConstructor
public class NotificationService {
    Gson gson;
    ObjectMapper mapper;

    public NotificationService() {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Autowired
    public NotificationService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void notifyAboutNewMarker(Report report) throws JsonProcessingException {
        Message message = Message.builder()
                .putData("action", "marker-new")
                .putData("id", report.getId())
                .putData("latitude", String.valueOf(report.getLocation().latitude))
                .putData("longitude", String.valueOf(report.getLocation().longitude))
                .putData("marker", mapper.writeValueAsString(report))
                .setTopic(NEW_MARKER_NOTIFICATION_TOPIC)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent addition message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void notifyAboutUpdatedMarker(Report report) {
        Message message = Message.builder()
                .putData("action", "marker-update")
                .putData("id", report.getId())
                .putData("severity", report.getProperties().getSeverity().name())
                .setTopic(UPDATE_MARKER_NOTIFICATION_TOPIC)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent update message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void notifyAboutRemovedMarker(Report report) {
        Message message = Message.builder()
                .putData("action", "marker-remove")
                .putData("id", report.getId())
                .setTopic(REMOVE_MARKER_NOTIFICATION_TOPIC)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent removal message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}
