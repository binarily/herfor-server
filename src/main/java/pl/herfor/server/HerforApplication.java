package pl.herfor.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class HerforApplication {

    public static void main(String[] args) {

        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/static/obs-czerniakk-firebase-adminsdk-2gg4q-0a5d31668b.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://obs-czerniakk.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SpringApplication.run(HerforApplication.class, args);
    }

}
