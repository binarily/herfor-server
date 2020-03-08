package pl.herfor.server.data.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.herfor.server.data.objects.Report;

import static pl.herfor.server.data.Constants.*;

@Service
public class NotificationService {
    private ObjectMapper mapper;

    @Autowired
    public NotificationService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void notifyAboutNewReport(Report report) throws JsonProcessingException {
        Message message = Message.builder()
                .putData("action", "report-new")
                .putData("id", report.getId())
                .putData("latitude", String.valueOf(report.getLocation().getLatitude()))
                .putData("longitude", String.valueOf(report.getLocation().getLongitude()))
                .putData("marker", mapper.writeValueAsString(report))
                .setTopic(NEW_REPORT_NOTIFICATION_TOPIC)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent addition message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void notifyAboutUpdatedReport(Report report) {
        Message message = Message.builder()
                .putData("action", "report-update")
                .putData("id", report.getId())
                .putData("severity", report.getProperties().getSeverity().name())
                .setTopic(UPDATE_REPORT_NOTIFICATION_TOPIC)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent update message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void notifyAboutRemovedReport(Report report) {
        Message message = Message.builder()
                .putData("action", "report-remove")
                .putData("id", report.getId())
                .setTopic(REMOVE_REPORT_NOTIFICATION_TOPIC)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent removal message: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}
